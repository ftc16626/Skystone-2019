package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
  private DcMotor motorLeft;
  private DcMotor motorRight;

  public float power = -0.75f;
  public boolean isMotorOn = false;

  private Servo intakeServo;
  private boolean isServoOpen = false;

  public Intake(HardwareMap hwMap, String motorLeftId, String motorRightId, String servoId) {
    motorLeft = hwMap.get(DcMotor.class, motorLeftId);
    motorRight = hwMap.get(DcMotor.class, motorRightId);

    motorRight.setDirection(Direction.REVERSE);

    intakeServo = hwMap.get(Servo.class, servoId);
    intakeServo.scaleRange(0.58, 1);
  }

  public void toggle() {
    isMotorOn = !isMotorOn;

    toggle(isMotorOn);
  }

  public void toggle(boolean on) {
    this.isMotorOn = on;

    setPower();
  }

  public void reverse() {
    power = power * -1;

    if(this.isMotorOn) {
      motorLeft.setPower(power);
      motorRight.setPower(power);
    }
  }

  public void directionBackward() {
    power = Math.abs(power) * -1;
    setPower();
  }

  public void directionForward() {
    power = Math.abs(power);
    setPower();
  }

  public void setPower() {
    if(this.isMotorOn) {
      motorLeft.setPower(power);
      motorRight.setPower(power);
    } else {
      motorLeft.setPower(0);
      motorRight.setPower(0);
    }
  }

  public void setPower(double test) {
    motorLeft.setPower(test);
    motorRight.setPower(test);
  }

  public void toggleIntakeOpen() {
    isServoOpen = !isServoOpen;

    if(isServoOpen) open();
    else if(!isServoOpen) close();
  }

  public void open() {
    isServoOpen = true;
    intakeServo.setPosition(0.1);
  }

  public void close() {
    isServoOpen = false;
    intakeServo.setPosition(1);
  }
}
