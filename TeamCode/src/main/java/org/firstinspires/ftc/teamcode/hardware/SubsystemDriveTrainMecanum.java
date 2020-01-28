package org.firstinspires.ftc.teamcode.hardware;

import android.util.Log;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveConstants;
import org.firstinspires.ftc.teamcode.hardware.util.DcMotorCached;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.openftc.revextensions2.ExpansionHubMotor;

public class SubsystemDriveTrainMecanum extends HardwareSubsystem {

  private final DcMotorCached motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight;
  private final String[] motorIds = new String[]{
      "motorFrontLeft", "motorFrontRight",
      "motorBackLeft", "motorBackRight"
  };
  private final List<DcMotorCached> motorList = new ArrayList<>();

  private double angle = 0;
  private double power = 0;
  private double turn = 0;

  private boolean dirty = false;

  private double CACHE_THRESHOLD = 0.0;

  public SubsystemDriveTrainMecanum(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);

    motorFrontLeft = new DcMotorCached(robot.hwMap.get(ExpansionHubMotor.class, motorIds[0]),
        CACHE_THRESHOLD);
    motorFrontRight = new DcMotorCached(robot.hwMap.get(ExpansionHubMotor.class, motorIds[1]),
        CACHE_THRESHOLD);
    motorBackLeft = new DcMotorCached(robot.hwMap.get(ExpansionHubMotor.class, motorIds[2]),
        CACHE_THRESHOLD);
    motorBackRight = new DcMotorCached(robot.hwMap.get(ExpansionHubMotor.class, motorIds[3]),
        CACHE_THRESHOLD);

    motorFrontRight.getMotor().setDirection(Direction.REVERSE);
    motorBackRight.getMotor().setDirection(Direction.REVERSE);

    motorList.add(motorFrontLeft);
    motorList.add(motorFrontRight);
    motorList.add(motorBackLeft);
    motorList.add(motorBackRight);

    stopMotors();

    for (DcMotorCached motor : motorList) {
      motor.getMotor().setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    }
  }

  public void stopMotors() {
    for (DcMotorCached motor : motorList) {
      motor.setPower(0);
    }
  }

  public void setRunUsingEncoders() {
    for (DcMotorCached motor : motorList) {
      motor.getMotor().setMode(RunMode.RUN_USING_ENCODER);
    }
  }

  public void setRunWithoutEncoders() {
    for (DcMotorCached motor : motorList) {
      motor.getMotor().setMode(RunMode.RUN_WITHOUT_ENCODER);
    }
  }

  public void setBrakeMode() {
    for (DcMotorCached motor : motorList) {
      motor.getMotor().setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    }
  }

  public void setFloatMode() {
    for (DcMotorCached motor : motorList) {
      motor.getMotor().setZeroPowerBehavior(ZeroPowerBehavior.FLOAT);
    }
  }

  @Override
  public void update() {
    if (dirty) {
      refreshMotors();
    }
  }

  @Override
  public void onStop() {
    stopMotors();
  }

  public void setAngle(double angle) {
    this.angle = angle;
    this.dirty = true;
  }

  public void setPower(double power) {
    this.power = power;
    this.dirty = true;
  }

  public void setTurn(double turn) {
    this.turn = turn;
    this.dirty = true;
  }

  public void refreshMotors() {
    double motorPowerFrontLeft = Math.cos(angle);
    double motorPowerFrontRight = Math.sin(angle);
    double motorPowerBackLeft = Math.sin(angle);
    double motorPowerBackRight = Math.cos(angle);

    // Normalize values
    double maxValue = Math.abs(Math.max(motorPowerFrontLeft,
        Math.max(motorPowerFrontRight, Math.max(motorPowerBackLeft, motorPowerBackRight))));

    motorPowerFrontLeft /= maxValue;
    motorPowerFrontRight /= maxValue;
    motorPowerBackLeft /= maxValue;
    motorPowerBackRight /= maxValue;

    motorPowerFrontLeft = motorPowerFrontLeft * power + turn;
    motorPowerFrontRight = motorPowerFrontRight * power - turn;
    motorPowerBackLeft = motorPowerBackLeft * power + turn;
    motorPowerBackRight = motorPowerBackRight * power - turn;

    motorFrontLeft.setPower(motorPowerFrontLeft);
    motorFrontRight.setPower(motorPowerFrontRight);
    motorBackLeft.setPower(motorPowerBackLeft);
    motorBackRight.setPower(motorPowerBackRight);

    this.dirty = false;
  }

  public PIDCoefficients getPIDCoefficients(DcMotor.RunMode mode) {
    PIDFCoefficients coeff = motorFrontLeft.getMotor().getPIDFCoefficients(mode);
    return new PIDCoefficients(coeff.p, coeff.i, coeff.d);
  }

  public void setPIDCoefficients(DcMotor.RunMode runMode, PIDCoefficients coeff) {
    for (DcMotorCached motor : motorList) {
      motor.getMotor()
          .setPIDFCoefficients(runMode, new PIDFCoefficients(coeff.kP, coeff.kI, coeff.kD,
              DriveConstants.getMotorVelocityF()));
    }
  }

  public ExpansionHubMotor[] getMotors() {
    return new ExpansionHubMotor[]{motorFrontLeft.getMotor(), motorFrontRight.getMotor(),
        motorBackLeft.getMotor(), motorBackRight.getMotor()};
  }
}
