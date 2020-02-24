package org.firstinspires.ftc.teamcode.hardware.subsystem;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Servo.Direction;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;

public class SubsystemFoundationGrabber extends HardwareSubsystem {

  private final Servo leftServo, rightServo;
  private final String[] servoIds = new String[] { "servoFoundationLeft", "servoFoundationRight" };

  private final double LEFT_MIN = 0.45;
  private final double LEFT_MAX = 0.935;

  private final double RIGHT_MIN = 0.35;
  private final double RIGHT_MAX = 0.85;

  public SubsystemFoundationGrabber(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);

    leftServo = robot.hwMap.get(Servo.class, servoIds[0]);
    rightServo = robot.hwMap.get(Servo.class, servoIds[1]);

    leftServo.setDirection(Direction.REVERSE);

    leftServo.scaleRange(LEFT_MIN, LEFT_MAX);
    rightServo.scaleRange(RIGHT_MIN, RIGHT_MAX);
  }

  @Override
  public void onMount() {
    raise();
  }

  public void drop() {
    leftServo.setPosition(0);
    rightServo.setPosition(0);
  }

  public void raise() {
    leftServo.setPosition(1);
    rightServo.setPosition(1);
  }
}
