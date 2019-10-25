package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.ArrayList;
import java.util.List;

public class MecanumDrive {

  public DcMotor motorFrontLeft = null;
  public DcMotor motorFrontRight = null;
  public DcMotor motorBackLeft = null;
  public DcMotor motorBackRight = null;

  private List<DcMotor> motorList = new ArrayList<DcMotor>();

  private double angle = 0;
  private double power = 0;
  private double turn = 0;

  private boolean dirty = false;

  public MecanumDrive(HardwareMap hwMap, String motorFrontLeftId, String motorFrontRightId,
      String motorBackLeftId, String motorBackRightId, boolean runWithEncoders) {
    motorFrontLeft = hwMap.get(DcMotor.class, motorFrontLeftId);
    motorFrontRight = hwMap.get(DcMotor.class, motorFrontRightId);
    motorBackLeft = hwMap.get(DcMotor.class, motorBackLeftId);
    motorBackRight = hwMap.get(DcMotor.class, motorBackRightId);

    motorFrontLeft.setDirection(Direction.REVERSE);
    motorBackLeft.setDirection(Direction.REVERSE);

    motorList.add(motorFrontLeft);
    motorList.add(motorFrontRight);
    motorList.add(motorBackLeft);
    motorList.add(motorBackRight);

    stopMotors();

    if (runWithEncoders) {
      for (DcMotor motor : motorList) {
        motor.setMode(RunMode.RUN_USING_ENCODER);
      }
    } else {
      for (DcMotor motor : motorList) {
        motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
      }
    }

    for(DcMotor motor: motorList) {
      motor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    }
  }

  public void stopMotors() {
    for (DcMotor motor : motorList) {
      motor.setPower(0);
    }
  }

  public void resetEncoders() {
    for(DcMotor motor : motorList) {
      motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
    }

    for(DcMotor motor : motorList) {
      motor.setMode(RunMode.RUN_USING_ENCODER);
    }
  }

  public void update() {
    if (dirty) {
      refreshMotors();
    }
  }

  public void setAngle(double angle) {
    this.angle = angle;
    this.dirty = true;
  }

  public void setPower(double power) {
    this.power = power;
    refreshMotors();
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

  public double getAngle() {
    return angle;
  }

  public double getPower() {
    return power;
  }

  public double getTurn() {
    return turn;
  }
}
