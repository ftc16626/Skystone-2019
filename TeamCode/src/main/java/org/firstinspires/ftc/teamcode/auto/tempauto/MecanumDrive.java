package org.firstinspires.ftc.teamcode.auto.tempauto;


import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.ArrayList;
import java.util.List;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.RevBulkData;

public class MecanumDrive {

  public ExpansionHubMotor motorFrontLeft;
  public ExpansionHubMotor motorFrontRight;
  public ExpansionHubMotor motorBackLeft;
  public ExpansionHubMotor motorBackRight;

  private List<ExpansionHubMotor> motorList = new ArrayList<>();

  private RevBulkData bulkData;

  private double lastMotorVelFrontLeft = 0;
  private double lastMotorVelFrontRight = 0;
  private double lastMotorVelBackLeft = 0;
  private double lastMotorVelBackRight = 0;

  public double motorVelFrontLeft = 0;
  public double motorVelFrontRight = 0;
  public double motorVelBackLeft = 0;
  public double motorVelBackRight = 0;

  private double angle = 0;
  private double power = 0;
  private double turn = 0;

  private boolean dirty = false;

  public MecanumDrive(
      HardwareMap hwMap,
      String motorFrontLeftId, String motorFrontRightId,
      String motorBackLeftId, String motorBackRightId) {
    motorFrontLeft = (ExpansionHubMotor) hwMap.dcMotor.get(motorFrontLeftId);
    motorFrontRight = (ExpansionHubMotor) hwMap.dcMotor.get(motorFrontRightId);
    motorBackLeft = (ExpansionHubMotor) hwMap.dcMotor.get(motorBackLeftId);
    motorBackRight = (ExpansionHubMotor) hwMap.dcMotor.get(motorBackRightId);

    motorFrontLeft.setDirection(Direction.REVERSE);
    motorBackLeft.setDirection(Direction.REVERSE);

    motorList.add(motorFrontLeft);
    motorList.add(motorFrontRight);
    motorList.add(motorBackLeft);
    motorList.add(motorBackRight);

    stopMotors();

    for (ExpansionHubMotor motor : motorList) {
      motor.setMode(RunMode.RUN_USING_ENCODER);
    }

    for (ExpansionHubMotor motor : motorList) {
      motor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    }
  }

  public void stopMotors() {
    for (ExpansionHubMotor motor : motorList) {
      motor.setPower(0);
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
}