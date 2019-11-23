package org.firstinspires.ftc.teamcode.subsystem.system;

import java.sql.Driver;
import org.firstinspires.ftc.teamcode.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadType;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.jetbrains.annotations.NotNull;

public class SubsystemDriverBackServos extends Subsystem implements GamepadListener {

  public DriverInterface driverInterface;

  private boolean isBackRightServoDown = false;
  private boolean isBackLeftServoDown = false;

  public SubsystemDriverBackServos(
      @NotNull MainHardware robot,
      @NotNull RadicalOpMode opMode,
      @NotNull DriverInterface driverInterface) {
    super(robot, opMode);

    this.driverInterface = driverInterface;
  }

  @Override
  public void update() {
    getOpMode().telemetry.addData("SERVO", getRobot().backLeftServo.getPosition());
  }

  private void toggleBackRightServo() {
    isBackRightServoDown = !isBackRightServoDown;

    if(isBackRightServoDown) {
      getRobot().perpendicularBackRightServo();
    } else {
      getRobot().raiseBackRightServo();
    }
  }

  private void toggleBackLeftServo() {
    isBackLeftServoDown = !isBackLeftServoDown;

    if(isBackLeftServoDown) {
      getRobot().perpendicularBackLeftServo();
    } else {
      getRobot().raiseBackLeftServo();
    }
  }


  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if(gamepadType == GamepadType.AID) {
      if(eventType == GamepadEventType.BUTTON_PRESSED) {
        switch (eventName) {
          case Y:
            toggleBackRightServo();
            toggleBackLeftServo();
            break;
        }
      }
    }
  }
}
