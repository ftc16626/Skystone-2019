package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.teamcode.hardware.RadicalRoadRunnerDriveBase;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.hardware.SubsystemRoadRunner;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;

public class RoadRunnerBaseOpmode extends RadicalOpMode {
  private SubsystemRoadRunner subsystemRoadRunner;
  public RadicalRoadRunnerDriveBase drive;

  protected Robot robot;

  public RoadRunnerBaseOpmode() {
    robot = new Robot(hardwareMap, this);
    robot.subsystemIMU.turnOn();

    subsystemRoadRunner = new SubsystemRoadRunner(robot);

    subsystemHandler.add(robot);
    subsystemHandler.add(subsystemRoadRunner);

    drive = subsystemRoadRunner.drive;
  }

  @Override
  public void onInit() {
    robot.subsystemDriveTrainMecanum.setRunUsingEncoders();
  }
}
