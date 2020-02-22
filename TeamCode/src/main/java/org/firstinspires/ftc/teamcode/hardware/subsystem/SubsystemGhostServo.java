package org.firstinspires.ftc.teamcode.hardware.subsystem;

import android.util.Log;
import java.util.ArrayList;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.hardware.util.GhostServo;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;

public class SubsystemGhostServo extends HardwareSubsystem {

  private ArrayList<GhostServo> servos = new ArrayList<>();

  public SubsystemGhostServo(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);
  }

  public void addServo(GhostServo servo) {
    servos.add(servo);
  }

  @Override
  public void update() {
    for (GhostServo servo : servos) {
      servo.update();
    }
  }
}
