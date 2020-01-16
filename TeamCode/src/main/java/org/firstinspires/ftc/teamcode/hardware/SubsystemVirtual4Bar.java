package org.firstinspires.ftc.teamcode.hardware;

import android.util.Log;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Servo.Direction;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;

public class SubsystemVirtual4Bar extends HardwareSubsystem {

  private final Servo leftServo, rightServo, grabberServo;
  private final String[] servoIds = new String[]{"servoV4BLeft", "servoV4BRight", "servoGrabber"};

  private final double LEFT_MIN = 0.08;
  private final double LEFT_MAX = 0.95;
  private final double LEFT_MIDDLE_OFFSET = 0;

  private final double RIGHT_MIN = 0.07;
  private final double RIGHT_MAX = 0.97;
  private final double RIGHT_MIDDLE_OFFSET = 0;

  private final double GRABBER_MIN = 0.45;
  private final double GRABBER_MAX = 0.6;

  public static final double DOWN_IN = 0;
  public static final double DOWN_OUT = 1;

  private double currentPosition = 0;

  private final double clampPoint = 0.5;
  private final double clampPointThreshold = 0.08;

  public SubsystemVirtual4Bar(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);

    leftServo = robot.hwMap.get(Servo.class, servoIds[0]);
    rightServo = robot.hwMap.get(Servo.class, servoIds[1]);

    grabberServo = robot.hwMap.get(Servo.class, servoIds[2]);

    leftServo.setDirection(Direction.REVERSE);

    leftServo.scaleRange(LEFT_MIN, LEFT_MAX);
    rightServo.scaleRange(RIGHT_MIN, RIGHT_MAX);
    grabberServo.scaleRange(GRABBER_MIN, GRABBER_MAX);
  }

  @Override
  public void onInit() {
    setPosition(currentPosition);
  }

  @Override
  public void onMount() {
    release();
  }

  public void setPosition(double pos) {
    currentPosition = pos;

    leftServo.setPosition(pos + LEFT_MIDDLE_OFFSET);
    rightServo.setPosition(pos + RIGHT_MIDDLE_OFFSET);

    if(Math.abs(currentPosition - clampPoint) < clampPointThreshold) {
      clamp();
    }
  }

  public void move(double speed) {
    currentPosition += speed;
    currentPosition = Range.clip(currentPosition, 0, 1);

    setPosition(currentPosition);
  }

  public void clamp() {
    grabberServo.setPosition(0);
  }

  public void release() {
    grabberServo.setPosition(1);
  }

  public void flipSide() {
    if(currentPosition < 0.5) {
      currentPosition = 1;
    } else {
      currentPosition = 0;
    }

    setPosition(currentPosition);
  }
}
