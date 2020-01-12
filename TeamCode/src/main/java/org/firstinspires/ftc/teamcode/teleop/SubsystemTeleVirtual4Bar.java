package org.firstinspires.ftc.teamcode.teleop;

import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.firstinspires.ftc.teamcode.util.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadType;

public class SubsystemTeleVirtual4Bar extends Subsystem implements GamepadListener {
  private final Robot robot;
  private final DriverInterface driverInterface;

  public SubsystemTeleVirtual4Bar(Robot robot, DriverInterface driverInterface) {
    this.robot = robot;
    this.driverInterface = driverInterface;

    driverInterface.addListener(this);
  }

  @Override
  public void onMount() {
    robot.subsystemVirtual4Bar.release();
  }

  @Override
  public void update() {
    double joystick = -driverInterface.aid.gamepad.right_stick_y;

    if(joystick != 0) {
      robot.subsystemVirtual4Bar.move(joystick / 30);
    }
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if(gamepadType == GamepadType.AID && eventType == GamepadEventType.BUTTON_PRESSED) {
      if(eventName == GamepadEventName.RIGHT_BUMPER) {
        robot.subsystemVirtual4Bar.clamp();
      } else if(eventName == GamepadEventName.LEFT_BUMPER) {
        robot.subsystemVirtual4Bar.release();
      }
    }
  }
}
