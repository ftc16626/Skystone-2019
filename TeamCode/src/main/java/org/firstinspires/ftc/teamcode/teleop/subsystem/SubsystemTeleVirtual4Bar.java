package org.firstinspires.ftc.teamcode.teleop.subsystem;

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

  private final double SPEED = 60;
  private final double SPEED_SLOW = 120;

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

    if (joystick != 0) {
      if (driverInterface.aid.gamepad.right_trigger > 0.1) {
        robot.subsystemVirtual4Bar.move(joystick / SPEED_SLOW);
      } else {
        robot.subsystemVirtual4Bar.move(joystick / SPEED);
      }
    }
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if (gamepadType == GamepadType.AID && eventType == GamepadEventType.BUTTON_PRESSED) {
      if (eventName == GamepadEventName.RIGHT_BUMPER) {
        robot.subsystemVirtual4Bar.clamp();
      } else if (eventName == GamepadEventName.LEFT_BUMPER) {
        robot.subsystemVirtual4Bar.release();
        robot.subsystemAutoIntakeGrabber.reset();
      }

      if (eventName == GamepadEventName.A) {
        robot.subsystemVirtual4Bar.flipSide();
      }
    }
  }
}
