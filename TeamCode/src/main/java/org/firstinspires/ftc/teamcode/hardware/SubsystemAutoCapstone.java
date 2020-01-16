package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;

public class SubsystemAutoCapstone extends HardwareSubsystem {

  private final Servo capstoneServo;
  private final String servoId = "servoCapstone";

  private final double MIN = 0.0;
  private final double MAX = 1.0;

  public SubsystemAutoCapstone(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);

    capstoneServo = robot.hwMap.get(Servo.class, servoId);
    capstoneServo.scaleRange(MIN, MAX);
  }

  @Override
  public void onMount() {
    raise();
  }

  public void lower() {
    capstoneServo.setPosition(1);
  }

  public void raise() {
    capstoneServo.setPosition(0);
  }
}
