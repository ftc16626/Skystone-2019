package org.firstinspires.ftc.teamcode.hardware;

import org.firstinspires.ftc.teamcode.subsystem.Subsystem;

public class SubsystemRoadRunner extends Subsystem {
  public RadicalRoadRunnerDriveBase drive;

  public SubsystemRoadRunner(Robot robot) {
    drive = new RadicalRoadRunnerDriveBase(robot);
  }

  @Override
  public void update() {
    drive.update();
  }
}
