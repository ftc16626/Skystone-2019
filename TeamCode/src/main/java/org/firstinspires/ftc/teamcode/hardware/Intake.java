package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Intake {
  private DcMotor motorLeft;
  private DcMotor motorRight;

  private float power = -0.75f;
  private boolean isMotorOn = false;

  private Servo intakeServo;
  private boolean isServoOpen = false;

  public Intake(HardwareMap hwMap, String motorLeftId, String motorRightId, String servoId) {
    motorLeft = hwMap.get(DcMotor.class, motorLeftId);
    motorRight = hwMap.get(DcMotor.class, motorRightId);

    motorRight.setDirection(Direction.REVERSE);

    intakeServo = hwMap.get(Servo.class, servoId);
    intakeServo.scaleRange(0.5, 1);
  }

  public void toggle() {
    isMotorOn = !isMotorOn;

    toggle(isMotorOn);
  }

  public void toggle(boolean on) {
    this.isMotorOn = on;

    if(this.isMotorOn) {
      motorLeft.setPower(power);
      motorRight.setPower(power);
    } else {
      motorLeft.setPower(0);
      motorRight.setPower(0);
    }
  }

  public void reverse() {
    power = power * -1;

    if(this.isMotorOn) {
      motorLeft.setPower(power);
      motorRight.setPower(power);
    }
  }

  public void toggleIntakeOpen() {
    isServoOpen = !isServoOpen;

    if(isServoOpen) open();
    else if(!isServoOpen) close();
  }

  public void open() {
    intakeServo.setPosition(0);
  }

  public void close() {
    intakeServo.setPosition(1);
  }
}
