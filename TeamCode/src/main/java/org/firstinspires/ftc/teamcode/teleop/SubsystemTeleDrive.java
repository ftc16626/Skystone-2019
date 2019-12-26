package org.firstinspires.ftc.teamcode.teleop;

import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.firstinspires.ftc.teamcode.util.gamepadextended.DriverInterface;

public class SubsystemTeleDrive extends Subsystem {
  private final DriverInterface driverInterface;
  private final Robot robot;

  public SubsystemTeleDrive(Robot robot, DriverInterface driverInterface) {
    this.driverInterface = driverInterface;
    this.robot = robot;
  }

  @Override
  public void update() {
    double magnitude = 0;
    double angle = 0;
    double turn = 0;

    double realMag = driverInterface.driver.getStrafeStickMagnitude() / 2;
    double realAngle = driverInterface.driver.getStrafeStickAngle();
    double realTurn = driverInterface.driver.getTurnStickX() / 2;

    if (driverInterface.driver.gamepad.right_trigger > 0.7) {
      turn = realTurn;
      magnitude = realMag;
      angle = realAngle;
    } else if (driverInterface.driver.getTurnStickX() != 0) {
      turn = realTurn;
    } else {
      magnitude = realMag;
      angle = realAngle;
    }

    if (driverInterface.driver.gamepad.left_trigger > 0.7) {
      double deg = Math.toDegrees(realAngle);
      double rounded = Math.round(deg / 45) * 45;
      angle = Math.toRadians(rounded);
    }

    if (driverInterface.driver.gamepad.left_bumper) {
      if (magnitude != 0) {
        magnitude *= 2;
      }
      if (turn != 0) {
        turn *= 2;
      }
    }

//    if (driverInterface.driver.getProfile().enableFieldCentric) {
//    if(USE_FIELD_CENTRIC) {
//      angle -= Math.toRadians(getRobot().imu.getGlobalHeading() % 360);
//      getOpMode().telemetry.addData("field centric", "true " + Math.toDegrees(angle));
//    } else {
//      getOpMode().telemetry.addData("field centric", "false " + angle);
//    }

    robot.subsystemDriveTrainMecanum.setAngle(angle);
    robot.subsystemDriveTrainMecanum.setPower(magnitude);
    robot.subsystemDriveTrainMecanum.setTurn(turn);
  }
}
