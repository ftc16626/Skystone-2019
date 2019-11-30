package org.firstinspires.ftc.teamcode.subsystem.system;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadType;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.jetbrains.annotations.NotNull;
import org.openftc.revextensions2.RevBulkData;

public class SubsystemDriverLiftIntake extends Subsystem implements GamepadListener {

  DriverInterface driverInterface;

  public SubsystemDriverLiftIntake(
      @NotNull MainHardware robot,
      @NotNull RadicalOpMode opMode,
      @NotNull DriverInterface driverInterface) {
    super(robot, opMode);

    this.driverInterface = driverInterface;
  }

  @Override
  public void update() {
    RevBulkData bulkData = getRobot().expansionHubDaughter.getBulkInputData();

    double distance = getRobot().liftRange.getDistance(DistanceUnit.MM);

    if (driverInterface.aid.gamepad.left_stick_y != 0) {
      float stick = driverInterface.aid.gamepad.left_stick_y;

      if (stick < 0 && distance < 580) {
        getRobot().motorLift.setPower(stick);
      } else if (stick > 0 && distance > 20) {
        getRobot().motorLift.setPower(stick);
      } else {
        getRobot().motorLift.setPower(0);
      }
    } else {
      getRobot().motorLift.setPower(0);
    }

    /**
     * 15,0.1489
     * 30,.107
     * 40,.0988
     * 50,0.1239
     * 70,.1155
     * 90,.1322
     * 120,.057
     * 150,.09
     * 170,.098
     * 200,.098
     * 220,0.157
     *
     */

    if(driverInterface.aid.gamepad.b) {
      getRobot().intake.open();
    } else {
      getRobot().intake.close();
    }

    getOpMode().telemetry.addData("Slider Height", distance);
    getOpMode().telemetry.addData("Y", driverInterface.aid.gamepad.left_stick_y);
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if (gamepadType == GamepadType.AID) {
      if (eventType == GamepadEventType.BUTTON_PRESSED) {
        switch (eventName) {
          case LEFT_BUMPER:
            if (!getRobot().intake.isMotorOn) {
              getRobot().intake.directionBackward();
              getRobot().intake.toggle(true);
            } else if (getRobot().intake.power > 1) {
              getRobot().intake.directionBackward();
            } else {
              getRobot().intake.toggle(false);
            }

            break;
          case RIGHT_BUMPER:
            if (!getRobot().intake.isMotorOn) {
              getRobot().intake.directionForward();
              getRobot().intake.toggle(true);
            } else if (getRobot().intake.power < 1) {
              getRobot().intake.directionForward();
            } else {
              getRobot().intake.toggle(false);
            }

            getRobot().intake.directionForward();
            break;
//          case B:
//            getRobot().intake.toggleIntakeOpen();
//            break;
        }

      }
    }
  }
}
