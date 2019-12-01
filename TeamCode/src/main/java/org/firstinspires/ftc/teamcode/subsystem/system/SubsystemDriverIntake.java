package org.firstinspires.ftc.teamcode.subsystem.system;

import org.firstinspires.ftc.teamcode.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadType;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.jetbrains.annotations.NotNull;

public class SubsystemDriverIntake extends Subsystem implements GamepadListener {

  DriverInterface driverInterface;

  public SubsystemDriverIntake(
      @NotNull MainHardware robot,
      @NotNull RadicalOpMode opMode,
      @NotNull DriverInterface driverInterface) {
    super(robot, opMode);

    this.driverInterface = driverInterface;
  }

  @Override
  public void update() {
    if(driverInterface.aid.gamepad.b) {
      getRobot().intake.open();
    } else {
      getRobot().intake.close();
    }
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
        }
      }
    }
  }
}
