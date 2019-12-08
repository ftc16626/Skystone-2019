package org.firstinspires.ftc.teamcode.hardware;

import android.support.annotation.NonNull;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.firstinspires.ftc.teamcode.util.RadicalMecanumLocalizer;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.RevBulkData;

public class DriveBaseMecanum extends SampleMecanumDriveBase {
  private ExpansionHubEx hub;
  private ExpansionHubMotor leftFront, leftRear, rightRear, rightFront;
  private List<ExpansionHubMotor> motors;
  private BNO055IMU imu;

  public DriveBaseMecanum(HardwareMap hardwareMap) {
    super();

    // TODO: adjust the names of the following hardware devices to match your configuration
    // for simplicity, we assume that the desired IMU and drive motors are on the same hub
    // if your motors are split between hubs, **you will need to add another bulk read**
    hub = hardwareMap.get(ExpansionHubEx.class, "Expansion Hub 9");

    imu = hardwareMap.get(BNO055IMU.class, "imu");
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
    parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
    imu.initialize(parameters);

    // TODO: if your hub is mounted vertically, remap the IMU axes so that the z-axis points
    // upward (normal to the floor) using a command like the following:
    // BNO055IMUUtil.remapAxes(imu, AxesOrder.XYZ, AxesSigns.NPN);

    leftFront = hardwareMap.get(ExpansionHubMotor.class, "motorFrontLeft");
    leftRear = hardwareMap.get(ExpansionHubMotor.class, "motorBackLeft");
    rightRear = hardwareMap.get(ExpansionHubMotor.class, "motorBackRight");
    rightFront = hardwareMap.get(ExpansionHubMotor.class, "motorFrontRight");

    motors = Arrays.asList(leftFront, leftRear, rightRear, rightFront);

    for (ExpansionHubMotor motor : motors) {
      if (DriveConstants.RUN_USING_ENCODER) {
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      }
      motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    if (DriveConstants.RUN_USING_ENCODER && DriveConstants.MOTOR_VELO_PID != null) {
      setPIDCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, DriveConstants.MOTOR_VELO_PID);
    }

    // TODO: reverse any motors using DcMotor.setDirection()
    leftFront.setDirection(Direction.REVERSE);
    leftRear.setDirection(Direction.REVERSE);

    // TODO: if desired, use setLocalizer() to change the localization method
    // for instance, setLocalizer(new ThreeTrackingWheelLocalizer(...));
    setLocalizer(new RadicalMecanumLocalizer(this, true));
  }

  @Override
  public PIDCoefficients getPIDCoefficients(DcMotor.RunMode runMode) {
    PIDFCoefficients coefficients = leftFront.getPIDFCoefficients(runMode);
    return new PIDCoefficients(coefficients.p, coefficients.i, coefficients.d);
  }

  @Override
  public void setPIDCoefficients(DcMotor.RunMode runMode, PIDCoefficients coefficients) {
    for (ExpansionHubMotor motor : motors) {
      motor.setPIDFCoefficients(runMode, new PIDFCoefficients(
          coefficients.kP, coefficients.kI, coefficients.kD, DriveConstants.getMotorVelocityF()
      ));
    }
  }

  @NonNull
  @Override
  public List<Double> getWheelPositions() {
    RevBulkData bulkData = hub.getBulkInputData();

    if (bulkData == null) {
      return Arrays.asList(0.0, 0.0, 0.0, 0.0);
    }

    List<Double> wheelPositions = new ArrayList<>();
    for (ExpansionHubMotor motor : motors) {
      wheelPositions.add(DriveConstants.encoderTicksToInches(bulkData.getMotorCurrentPosition(motor)));
    }
    return wheelPositions;
  }

  @Override
  public List<Double> getWheelVelocities() {
    RevBulkData bulkData = hub.getBulkInputData();

    if (bulkData == null) {
      return Arrays.asList(0.0, 0.0, 0.0, 0.0);
    }

    List<Double> wheelVelocities = new ArrayList<>();
    for (ExpansionHubMotor motor : motors) {
      wheelVelocities.add(DriveConstants.encoderTicksToInches(bulkData.getMotorVelocity(motor)));
    }
    return wheelVelocities;
  }

  @Override
  public void setMotorPowers(double v, double v1, double v2, double v3) {
    leftFront.setPower(v);
    leftRear.setPower(v1);
    rightRear.setPower(v2);
    rightFront.setPower(v3);
  }

  @Override
  public double getRawExternalHeading() {
    return imu.getAngularOrientation().firstAngle;
  }
}