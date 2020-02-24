package org.firstinspires.ftc.teamcode.hardware.subsystem;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.hardware.util.GhostServo35kg;
import org.firstinspires.ftc.teamcode.hardware.util.GhostServoGoBildaTorque;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;

public class SubsystemVirtual4Bar extends HardwareSubsystem {

  private final GhostServo35kg leftServo, rightServo;
  private final GhostServoGoBildaTorque grabberServo;
  private final String[] servoIds = new String[]{"servoV4BLeft", "servoV4BRight", "servoGrabber"};

  private final double LEFT_MIN = 0;
  private final double LEFT_MAX = 1;
  private final double LEFT_MIDDLE_OFFSET = 0;

  private final double RIGHT_MIN = 0;
  private final double RIGHT_MAX = 1;
  private final double RIGHT_MIDDLE_OFFSET = 0;

  private final double V4B_MIN = 0.0326;
  private final double V4B_MAX = 0.917;

  private final double GRABBER_MIN = 0.45;
  private final double GRABBER_MAX = 0.6;

  private double currentPosition = 0;

  private final double clampPoint = 0.45;
  private final double clampPointThreshold = 0.08;

  public boolean isClamped = false;

  public SubsystemVirtual4Bar(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);

    leftServo = new GhostServo35kg(robot.hwMap.get(Servo.class, servoIds[0]), LEFT_MIN, LEFT_MAX, 2, true);
    rightServo = new GhostServo35kg(robot.hwMap.get(Servo.class, servoIds[1]), RIGHT_MIN, RIGHT_MAX, 2);

    leftServo.setEstimate(V4B_MIN);
    rightServo.setEstimate(V4B_MIN);

//    leftServo.setDEBUG(true);

    grabberServo = new GhostServoGoBildaTorque(robot.hwMap.get(Servo.class, servoIds[2]), GRABBER_MIN, GRABBER_MAX, 1);

    getRobot().subsystemGhostServo.addServo(leftServo);
    getRobot().subsystemGhostServo.addServo(rightServo);
    getRobot().subsystemGhostServo.addServo(grabberServo);
  }

  @Override
  public void onInit() {
    setPosition(currentPosition);
  }

  @Override
  public void onMount() {
    release();
  }

  @Override
  public void update() {
    if(Math.abs(leftServo.getEstimate() - clampPoint) < clampPointThreshold) {
      clamp();
    }

    if(getRobot().subsystemAutoIntakeGrabber.isIdle() && leftServo.getEstimate() < 0.1 && isClamped) {
      release();
    }
  }

  public void setPosition(double pos) {
    pos = Range.clip(pos, V4B_MIN, V4B_MAX);
    currentPosition = pos;

    leftServo.setPosition(pos + LEFT_MIDDLE_OFFSET);
    rightServo.setPosition(pos + RIGHT_MIDDLE_OFFSET);

//    if(Math.abs(currentPosition - clampPoint) < clampPointThreshold) {
//      clamp();
//    }
  }

  public void move(double speed) {
    currentPosition += speed;
    currentPosition = Range.clip(currentPosition, V4B_MIN, V4B_MAX);

    setPosition(currentPosition);
  }

  public void clamp() {
    isClamped = true;
    setClamp();
  }

  public void release() {
    isClamped = false;
    setClamp();
  }

  public void toggleClamp() {
    isClamped = !isClamped;
    setClamp();
  }

  private void setClamp() {
    if(isClamped) {
      grabberServo.setPosition(0);
    } else {
      grabberServo.setPosition(1);
    }
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
