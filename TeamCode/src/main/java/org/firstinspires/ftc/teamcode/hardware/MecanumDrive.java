package org.firstinspires.ftc.teamcode.hardware;

import android.util.Log;
import com.ftc16626.missioncontrol.math.Vector2;
import com.ftc16626.missioncontrol.math.kinematics.Kinematics;
import com.ftc16626.missioncontrol.math.kinematics.KinematicsIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.ArrayList;
import java.util.List;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.RevBulkData;

public class MecanumDrive {

  public ExpansionHubMotor motorFrontLeft;
  public ExpansionHubMotor motorFrontRight;
  public ExpansionHubMotor motorBackLeft;
  public ExpansionHubMotor motorBackRight;

  private List<ExpansionHubMotor> motorList = new ArrayList<>();

  public ExpansionHubEx expansionHub;

  private RevBulkData bulkData;

  private double lastMotorVelFrontLeft = 0;
  private double lastMotorVelFrontRight = 0;
  private double lastMotorVelBackLeft = 0;
  private double lastMotorVelBackRight = 0;

  public double motorVelFrontLeft = 0;
  public double motorVelFrontRight = 0;
  public double motorVelBackLeft = 0;
  public double motorVelBackRight = 0;

  private double gearRatio;
  private double encoderCounts;

  private double angle = 0;
  private double power = 0;
  private double turn = 0;

  private boolean dirty = false;

  private Kinematics kinematics;

  public double velocityX = 0;
  public double velocityY = 0;
  public double angularVelocity = 0;

  private KinematicsIntegrator kinematicsIntegrator = new KinematicsIntegrator(new Vector2(), 0);

  private boolean updatedFirstTime = false;
  private double lastTime = 0;

  public MecanumDrive(
      HardwareMap hwMap, ExpansionHubEx expansionHub,
      String motorFrontLeftId, String motorFrontRightId,
      String motorBackLeftId, String motorBackRightId,
      boolean runWithEncoders,
      double width, double length, double wheelRadius,
      double gearRatio, double encoderCounts) {
    motorFrontLeft = (ExpansionHubMotor) hwMap.dcMotor.get(motorFrontLeftId);
    motorFrontRight = (ExpansionHubMotor) hwMap.dcMotor.get(motorFrontRightId);
    motorBackLeft = (ExpansionHubMotor) hwMap.dcMotor.get(motorBackLeftId);
    motorBackRight = (ExpansionHubMotor) hwMap.dcMotor.get(motorBackRightId);

    this.expansionHub = expansionHub;

    motorFrontLeft.setDirection(Direction.REVERSE);
    motorBackLeft.setDirection(Direction.REVERSE);

    motorList.add(motorFrontLeft);
    motorList.add(motorFrontRight);
    motorList.add(motorBackLeft);
    motorList.add(motorBackRight);

    stopMotors();

    if (runWithEncoders) {
      for (ExpansionHubMotor motor : motorList) {
        motor.setMode(RunMode.RUN_USING_ENCODER);
      }
    } else {
      for (ExpansionHubMotor motor : motorList) {
        motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
      }
    }

    for (ExpansionHubMotor motor : motorList) {
      motor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    }

    this.gearRatio = gearRatio;
    this.encoderCounts = encoderCounts;

    kinematics = new Kinematics(
        length / 2, width / 2,
        wheelRadius
    );
  }

  public void stopMotors() {
    for (ExpansionHubMotor motor : motorList) {
      motor.setPower(0);
    }
  }

  public void resetEncoders() {
    for (ExpansionHubMotor motor : motorList) {
      motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
    }

    for (ExpansionHubMotor motor : motorList) {
      motor.setMode(RunMode.RUN_USING_ENCODER);
    }
  }

  public void update() {
    bulkData = expansionHub.getBulkInputData();
    double now = System.currentTimeMillis();

    if (!updatedFirstTime) {
      lastTime = now;
      updatedFirstTime = true;
    }

    double timeDelta = (now - lastTime) / 1000;

    motorVelFrontLeft = bulkData.getMotorCurrentPosition(motorFrontLeft) - lastMotorVelFrontLeft;
    motorVelFrontRight = bulkData.getMotorCurrentPosition(motorFrontRight) - lastMotorVelFrontRight;
    motorVelBackLeft = bulkData.getMotorCurrentPosition(motorBackLeft) - lastMotorVelBackLeft;
    motorVelBackRight = bulkData.getMotorCurrentPosition(motorBackRight) - lastMotorVelBackRight;

//    motorVelFrontLeft *= timeDelta;
//    motorVelFrontRight *= timeDelta;
//    motorVelBackLeft *= timeDelta;
//    motorVelBackRight *= timeDelta;

    // Convert ticks to radians then distance
//    motorVelFrontLeft *= (2 * Math.PI / encoderCounts) * kinematics.getWheelRadius() * GEAR_RATIO;
//    motorVelFrontRight *= (2 * Math.PI / encoderCounts) * kinematics.getWheelRadius() * GEAR_RATIO;
//    motorVelBackLeft *= (2 * Math.PI / encoderCounts) * kinematics.getWheelRadius() * GEAR_RATIO;
//    motorVelBackRight *= (2 * Math.PI / encoderCounts) * kinematics.getWheelRadius() * GEAR_RATIO;

    motorVelFrontLeft *= (2 * Math.PI / encoderCounts);
    motorVelFrontRight *= (2 * Math.PI / encoderCounts);
    motorVelBackLeft *= (2 * Math.PI / encoderCounts);
    motorVelBackRight *= (2 * Math.PI / encoderCounts);

    double[] motion = kinematics.mecanumDrive(
        motorVelFrontLeft, motorVelFrontRight,
        motorVelBackLeft, motorVelBackRight
    );

    velocityX = motion[0];
    velocityY = motion[1];
    angularVelocity = motion[2];

//    Log.i("vx", Double.toString(velocityX));
//    Log.i("vy", Double.toString(velocityY));
//    Log.i("av", Double.toString(angularVelocity));

//    Log.i("VX", Double.toString(velocityX));
//    Log.i("VY", Double.toString(velocityY));
//    Log.i("AV", Double.toString(angularVelocity));

    kinematicsIntegrator.update(new Vector2(velocityX, velocityY), angularVelocity, now);

    lastMotorVelFrontLeft = bulkData.getMotorCurrentPosition(motorFrontLeft);
    lastMotorVelFrontRight = bulkData.getMotorCurrentPosition(motorFrontRight);
    lastMotorVelBackLeft = bulkData.getMotorCurrentPosition(motorBackLeft);
    lastMotorVelBackRight = bulkData.getMotorCurrentPosition(motorBackRight);

    if (dirty) {
      refreshMotors();
    }

    lastTime = now;
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

  public Vector2 getPosition() {
    return kinematicsIntegrator.getCurrentPos();
  }

  public Double getHeading() {
    return kinematicsIntegrator.getCurrentHeading();
  }
}
