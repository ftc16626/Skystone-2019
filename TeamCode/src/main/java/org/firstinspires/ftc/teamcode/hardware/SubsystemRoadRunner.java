package org.firstinspires.ftc.teamcode.hardware;

import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.firstinspires.ftc.teamcode.tuning.DriveBaseMecanumOld;

public class SubsystemRoadRunner extends Subsystem {
  public RadicalRoadRunnerDriveBase drive;
//  public DriveBaseMecanumOld drive;

  public SubsystemRoadRunner(Robot robot) {
    drive = new RadicalRoadRunnerDriveBase(robot);
//    drive = new DriveBaseMecanum  Old(robot.hwMap);
  }

  @Override
  public void update() {
//    drive.update();
  }
}
