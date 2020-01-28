package org.firstinspires.ftc.teamcode.teleop;

import android.util.Log;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.firstinspires.ftc.teamcode.util.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadType;

public class SubsystemTeleCapstone extends Subsystem implements GamepadListener {

  private final Robot robot;
  private final DriverInterface driverInterface;

  private boolean capstoneActivated = false;

  public SubsystemTeleCapstone(Robot robot, DriverInterface driverInterface) {
    this.driverInterface = driverInterface;
    driverInterface.addListener(this);

    this.robot = robot;
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if(gamepadType == GamepadType.AID && eventType == GamepadEventType.BUTTON_PRESSED) {
      if(eventName == GamepadEventName.X) {
        capstoneActivated = !capstoneActivated;

        if(capstoneActivated) {
          robot.subsystemAutoIntakeGrabber.setEndgame(true);

          robot.subsystemVirtual4Bar.setPosition(0);
          robot.subsystemLighting.setCapstone(true);
        } else {
          robot.subsystemLighting.setCapstone(false);
        }
      }
    }
  }
}
