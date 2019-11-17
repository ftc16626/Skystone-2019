package org.firstinspires.ftc.teamcode.hardware;

import static org.firstinspires.ftc.teamcode.hardware.DriveConstants.encoderTicksToInches;
import static org.firstinspires.ftc.teamcode.hardware.DriveConstants.getMotorVelocityF;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.acmerobotics.roadrunner.drive.DriveSignal;
import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.followers.HolonomicPIDVAFollower;
import com.acmerobotics.roadrunner.followers.TrajectoryFollower;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.profile.MotionProfile;
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator;
import com.acmerobotics.roadrunner.profile.MotionState;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumConstraints;
import com.acmerobotics.roadrunner.util.NanoClock;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMU.Parameters;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.RevBulkData;

public class DriveBaseMecanum extends MecanumDrive {

  // Hardware
  private ExpansionHubMotor motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight;
  private List<ExpansionHubMotor> motors;
  private BNO055IMU imu;
  private ExpansionHubEx hubMain;

  // PID
  public static PIDCoefficients TRANSLATIONAL_PID = new PIDCoefficients(0, 0, 0);
  public static PIDCoefficients HEADING_PID = new PIDCoefficients(0, 0, 0);

  public enum Mode {
    IDLE,
    TURN,
    FOLLOW_TRAJECTORY
  }

  private NanoClock clock;
  private Mode mode;

  private PIDFController turnController;
  private MotionProfile turnProfile;
  private double turnStart;

  private DriveConstraints constraints;
  private TrajectoryFollower follower;

//  private List<Double> lastWheelPositions;
//  private double lastTimestamp;

  public DriveBaseMecanum(HardwareMap hardwareMap) {
    // PID
    super(DriveConstants.kV, DriveConstants.kA, DriveConstants.kStatic, DriveConstants.TRACK_WIDTH);

    clock = NanoClock.system();

    mode = Mode.IDLE;

    turnController = new PIDFController(HEADING_PID);
    turnController.setInputBounds(0, 2 * Math.PI);

    constraints = new MecanumConstraints(DriveConstants.BASE_CONSTRAINTS,
        DriveConstants.TRACK_WIDTH);
    follower = new HolonomicPIDVAFollower(TRANSLATIONAL_PID, TRANSLATIONAL_PID, HEADING_PID);

    // Hardware
    hubMain = hardwareMap.get(ExpansionHubEx.class, "Expansion Hub 9");

    imu = hardwareMap.get(BNO055IMU.class, "imu");
    BNO055IMU.Parameters parameters = new Parameters();
    parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
    imu.initialize(parameters);

    motorFrontLeft = hardwareMap.get(ExpansionHubMotor.class, "motorFrontLeft");
    motorFrontRight = hardwareMap.get(ExpansionHubMotor.class, "motorFrontRight");
    motorBackLeft = hardwareMap.get(ExpansionHubMotor.class, "motorBackLeft");
    motorBackRight = hardwareMap.get(ExpansionHubMotor.class, "motorBackRight");

    motors = Arrays.asList(motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight);

    for (DcMotorEx motor : motors) {
      if (DriveConstants.RUN_USING_ENCODER) {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      }
      motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    if (DriveConstants.RUN_USING_ENCODER && DriveConstants.MOTOR_VELO_PID != null) {
      setPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, DriveConstants.MOTOR_VELO_PID);
    }

    motorFrontLeft.setDirection(Direction.REVERSE);
    motorBackLeft.setDirection(Direction.REVERSE);
  }

  public TrajectoryBuilder trajectoryBuilder() {
    return new TrajectoryBuilder(getPoseEstimate(), constraints);
  }

  public void turn(double angle) {
    double heading = getPoseEstimate().getHeading();
    turnProfile = MotionProfileGenerator.generateSimpleMotionProfile(
        new MotionState(heading, 0, 0, 0),
        new MotionState(heading + angle, 0, 0, 0),
        constraints.maxAngVel,
        constraints.maxAngAccel,
        constraints.maxAngJerk
    );
    turnStart = clock.seconds();
    mode = Mode.TURN;
  }

  public void turnSync(double angle) {
    turn(angle);
    waitForIdle();
  }

  public void followTrajectory(Trajectory trajectory) {
    follower.followTrajectory(trajectory);
    mode = Mode.FOLLOW_TRAJECTORY;
  }

  public void followTrajectorySync(Trajectory trajectory) {
    followTrajectory(trajectory);
    waitForIdle();
  }

  public Pose2d getLastError() {
    switch (mode) {
      case FOLLOW_TRAJECTORY:
        return follower.getLastError();
      case TURN:
        return new Pose2d(0, 0, turnController.getLastError());
      case IDLE:
        return new Pose2d();
    }
    throw new AssertionError();
  }

  public void update() {
    updatePoseEstimate();

    Pose2d currentPose = getPoseEstimate();
    Pose2d lastError = getLastError();

    switch (mode) {
      case IDLE:
        // do nothing
        break;
      case TURN: {
        double t = clock.seconds() - turnStart;

        MotionState targetState = turnProfile.get(t);

        turnController.setTargetPosition(targetState.getX());

        double targetOmega = targetState.getV();
        double targetAlpha = targetState.getA();
        double correction = turnController.update(currentPose.getHeading(), targetOmega);

        setDriveSignal(new DriveSignal(new Pose2d(
            0, 0, targetOmega + correction
        ), new Pose2d(
            0, 0, targetAlpha
        )));

        if (t >= turnProfile.duration()) {
          mode = Mode.IDLE;
          setDriveSignal(new DriveSignal());
        }

        break;
      }
      case FOLLOW_TRAJECTORY: {
        setDriveSignal(follower.update(currentPose));

        Trajectory trajectory = follower.getTrajectory();

        if (!follower.isFollowing()) {
          mode = Mode.IDLE;
          setDriveSignal(new DriveSignal());
        }

        break;
      }
    }
  }

  public void waitForIdle() {
    while (!Thread.currentThread().isInterrupted() && isBusy()) {
      update();
    }
  }

  public boolean isBusy() {
    return mode != Mode.IDLE;
  }

  public PIDCoefficients getPIDCoefficients(DcMotor.RunMode runMode) {
    PIDFCoefficients coefficients = motorFrontLeft.getPIDFCoefficients(runMode);
    return new PIDCoefficients(coefficients.p, coefficients.i, coefficients.d);
  }

  public void setPIDCoefficients(DcMotor.RunMode runMode, PIDCoefficients coefficients) {
    for (DcMotorEx motor : motors) {
      motor.setPIDFCoefficients(runMode, new PIDFCoefficients(
          coefficients.kP, coefficients.kI, coefficients.kD, getMotorVelocityF()
      ));
    }
  }

  @NotNull
  @Override
  public List<Double> getWheelPositions() {
    RevBulkData bulkData = hubMain.getBulkInputData();

    if(bulkData == null) return Arrays.asList(0.0, 0.0, 0.0, 0.0);

    List<Double> wheelPositions = new ArrayList<>();
    for (ExpansionHubMotor motor : motors) {
      wheelPositions.add(encoderTicksToInches(bulkData.getMotorCurrentPosition(motor)));
    }

    return wheelPositions;
  }

  public List<Double> getWheelVelocities() {
    RevBulkData bulkData = hubMain.getBulkInputData();

    if (bulkData == null) {
      return Arrays.asList(0.0, 0.0, 0.0, 0.0);
    }

    List<Double> wheelVelocities = new ArrayList<>();
    for (ExpansionHubMotor motor : motors) {
      wheelVelocities.add(encoderTicksToInches(bulkData.getMotorVelocity(motor)));
    }
    return wheelVelocities;
  }

//  @NotNull
//  @Override
//  public List<Double> getWheelPositions() {
//    List<Double> wheelPositions = new ArrayList<>();
//    for (DcMotorEx motor : motors) {
//      wheelPositions.add(encoderTicksToInches(motor.getCurrentPosition()));
//    }
//    return wheelPositions;
//  }

//  public List<Double> getWheelVelocities() {
//    List<Double> wheelVelocities = new ArrayList<>();
//    for (DcMotorEx motor : motors) {
//      wheelVelocities.add(encoderTicksToInches(motor.getVelocity()));
//    }
//    return wheelVelocities;
//  }

//  public List<Double> getWheelVelocities() {
//    List<Double> positions = getWheelPositions();
//    double currentTimestamp = clock.seconds();
//
//    List<Double> velocities = new ArrayList<>(positions.size());
//    ;
//    if (lastWheelPositions != null) {
//      double dt = currentTimestamp - lastTimestamp;
//      for (int i = 0; i < positions.size(); i++) {
//        velocities.add((positions.get(i) - lastWheelPositions.get(i)) / dt);
//      }
//    } else {
//      for (int i = 0; i < positions.size(); i++) {
//        velocities.add(0.0);
//      }
//    }
//
//    lastTimestamp = currentTimestamp;
//    lastWheelPositions = positions;
//
//    return velocities;
//  }

  @Override
  public void setMotorPowers(double v, double v1, double v2, double v3) {
    motorFrontLeft.setPower(v);
    motorBackLeft.setPower(v1);
    motorBackRight.setPower(v2);
    motorFrontRight.setPower(v3);
  }

  @Override
  public double getRawExternalHeading() {
    return imu.getAngularOrientation().firstAngle;
  }
}

