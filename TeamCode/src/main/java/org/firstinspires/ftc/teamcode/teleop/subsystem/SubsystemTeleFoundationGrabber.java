package org.firstinspires.ftc.teamcode.teleop.subsystem;

import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.firstinspires.ftc.teamcode.util.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadType;

public class SubsystemTeleFoundationGrabber extends Subsystem implements GamepadListener {
  private final Robot robot;

  private boolean isGrabberUp = true;

  public SubsystemTeleFoundationGrabber(Robot robot, DriverInterface driverInterface) {
    this.robot = robot;
    driverInterface.addListener(this);
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if(gamepadType == GamepadType.DRIVER && eventType == GamepadEventType.BUTTON_PRESSED) {
      if(eventName == GamepadEventName.Y) {
        isGrabberUp = !isGrabberUp;

        if(isGrabberUp) {
          robot.subsystemFoundationGrabber.raise();
        } else {
          robot.subsystemFoundationGrabber.drop();
        }
      }
    }
  }
}
