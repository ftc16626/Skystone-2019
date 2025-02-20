package org.firstinspires.ftc.teamcode.hardware.roadrunner;

import android.util.Log;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.acmerobotics.roadrunner.control.PIDFController;
import com.acmerobotics.roadrunner.drive.Drive;
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
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveConstants;
import org.firstinspires.ftc.teamcode.util.DashboardUtil;

/*
 * Base class with shared functionality for sample mecanum drives. All hardware-specific details are
 * handled in subclasses.
 */
@Config
public abstract class SampleMecanumDriveBase extends MecanumDrive {

  public enum Mode {
    IDLE,
    TURN,
    FOLLOW_TRAJECTORY
  }

  private FtcDashboard dashboard;
  private NanoClock clock;

  private Mode mode;

  private PIDFController turnController;
  private MotionProfile turnProfile;
  private double turnStart;

  private DriveConstraints constraints;
  private TrajectoryFollower follower;

  private List<Double> lastWheelPositions;
  private double lastTimestamp;

  private double maxErrorX = 0;
  private double maxErrorY = 0;
  private double maxErrorHeading = 0;

  public SampleMecanumDriveBase() {
    super(DriveConstants.kV, DriveConstants.kA, DriveConstants.kStatic, DriveConstants.TRACK_WIDTH,
        DriveConstants.WHEEL_BASE);

    dashboard = FtcDashboard.getInstance();
    dashboard.setTelemetryTransmissionInterval(1);

    clock = NanoClock.system();

    mode = Mode.IDLE;

    turnController = new PIDFController(DriveConstants.HEADING_PID);
    turnController.setInputBounds(0, 2 * Math.PI);

    constraints = new MecanumConstraints(DriveConstants.BASE_CONSTRAINTS,
        DriveConstants.TRACK_WIDTH, DriveConstants.WHEEL_BASE);
    follower = new HolonomicPIDVAFollower(DriveConstants.AXIAL_PID, DriveConstants.LATERAL_PID,
        DriveConstants.HEADING_PID);
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

    maxErrorX = Math.max(lastError.getX(), maxErrorX);
    maxErrorY = Math.max(lastError.getY(), maxErrorY);
    maxErrorHeading = Math.max(lastError.getHeading(), maxErrorHeading);

    TelemetryPacket packet = new TelemetryPacket();
    Canvas fieldOverlay = packet.fieldOverlay();

    packet.put("mode", mode);

    packet.put("x", currentPose.getX());
    packet.put("y", currentPose.getY());
    packet.put("heading", currentPose.getHeading());
    packet.put("headingDeg", Math.toDegrees(currentPose.getHeading()));

    packet.put("xError", lastError.getX());
    packet.put("yError", lastError.getY());
    packet.put("headingError", lastError.getHeading());

    packet.put("maxXError", maxErrorX);
    packet.put("maxYError", maxErrorY);
    packet.put("maxHeadingError", maxErrorHeading);

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

        fieldOverlay.setStrokeWidth(1);
        fieldOverlay.setStroke("4CAF50");
        DashboardUtil.drawSampledPath(fieldOverlay, trajectory.getPath());

        fieldOverlay.setStroke("#F44336");
        double t = follower.elapsedTime();
        DashboardUtil.drawRobot(fieldOverlay, trajectory.get(t));

        fieldOverlay.setStroke("#3F51B5");
        fieldOverlay.fillCircle(currentPose.getX(), currentPose.getY(), 3);

        if (!follower.isFollowing()) {
          mode = Mode.IDLE;
          setDriveSignal(new DriveSignal());
        }

        break;
      }
    }

    dashboard.sendTelemetryPacket(packet);
  }

  public void waitForIdle() {
    while (!Thread.currentThread().isInterrupted() && isBusy()) {
      update();
    }
  }

  public boolean isBusy() {
    return mode != Mode.IDLE;
  }

  public List<Double> getWheelVelocities() {
    List<Double> positions = getWheelPositions();
    double currentTimestamp = clock.seconds();

    List<Double> velocities = new ArrayList<>(positions.size());

    if (lastWheelPositions != null) {
      double dt = currentTimestamp - lastTimestamp;
      for (int i = 0; i < positions.size(); i++) {
        velocities.add((positions.get(i) - lastWheelPositions.get(i)) / dt);
      }
    } else {
      for (int i = 0; i < positions.size(); i++) {
        velocities.add(0.0);
      }
    }

    lastTimestamp = currentTimestamp;
    lastWheelPositions = positions;

    return velocities;
  }

  public abstract PIDCoefficients getPIDCoefficients(DcMotor.RunMode runMode);

  public abstract void setPIDCoefficients(DcMotor.RunMode runMode, PIDCoefficients coefficients);
}