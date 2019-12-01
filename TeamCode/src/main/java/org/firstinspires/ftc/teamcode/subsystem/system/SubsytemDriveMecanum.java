package org.firstinspires.ftc.teamcode.subsystem.system;

import org.firstinspires.ftc.teamcode.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.jetbrains.annotations.NotNull;

public class SubsytemDriveMecanum extends Subsystem {

  public DriverInterface driverInterface;

  private final boolean USE_FIELD_CENTRIC = true;

  public SubsytemDriveMecanum(
      @NotNull MainHardware robot,
      @NotNull RadicalOpMode opMode,
      @NotNull DriverInterface driverInterface) {
    super(robot, opMode);

    this.driverInterface = driverInterface;
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
    } else if (driverInterface.driver.gamepad.right_bumper) {
//      if (magnitude != 0) {
//        magnitude /= 4;
//      }
    } else {
//      magnitude *= 1 - driverInterface.driver.gamepad.left_trigger;
    }

//    if (driverInterface.driver.getProfile().enableFieldCentric) {
    if(USE_FIELD_CENTRIC) {
      angle += Math.toRadians(getRobot().imu.getGlobalHeading() % 360);
      getOpMode().telemetry.addData("field centric", "true " + angle);
    } else {
      getOpMode().telemetry.addData("field centric", "false " + angle);
    }

    getRobot().drive.setAngle(angle);
    getRobot().drive.setPower(magnitude);
    getRobot().drive.setTurn(turn);
  }
}
