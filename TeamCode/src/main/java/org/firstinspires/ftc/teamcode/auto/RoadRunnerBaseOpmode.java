package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.teamcode.hardware.RadicalRoadRunnerDriveBase;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.hardware.subsystem.SubsystemRoadRunner;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;

public class RoadRunnerBaseOpmode extends RadicalOpMode {

  private SubsystemRoadRunner subsystemRoadRunner;
  public RadicalRoadRunnerDriveBase drive;
//  public DriveBaseMecanumOld drive;

  @Override
  public void onInit() {
    baseInit();
  }

  public void baseInit() {
    subsystemRoadRunner = new SubsystemRoadRunner(robot);

    subsystemHandler.add(subsystemRoadRunner);

    drive = subsystemRoadRunner.drive;

    robot.subsystemDriveTrainMecanum.setRunUsingEncoders();
    robot.subsystemDriveTrainMecanum.setBrakeMode();
  }
}
