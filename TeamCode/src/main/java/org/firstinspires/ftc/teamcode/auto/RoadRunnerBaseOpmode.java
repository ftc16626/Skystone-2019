package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.teamcode.hardware.RadicalRoadRunnerDriveBase;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.hardware.SubsystemRoadRunner;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.tuning.DriveBaseMecanumOld;

public class RoadRunnerBaseOpmode extends RadicalOpMode {

  private SubsystemRoadRunner subsystemRoadRunner;
  public RadicalRoadRunnerDriveBase drive;
//  public DriveBaseMecanumOld drive;

  protected Robot robot;

  @Override
  public void onInit() {
    baseInit();
  }

  public void baseInit() {
    robot = new Robot(hardwareMap, this);

//    subsystemRoadRunner = new SubsystemRoadRunner(robot);
    subsystemHandler.add(robot);
//    subsystemHandler.add(subsystemRoadRunner);

//    drive = subsystemRoadRunner.drive;

    robot.subsystemDriveTrainMecanum.setRunUsingEncoders();
    robot.subsystemDriveTrainMecanum.setBrakeMode();
  }
}
