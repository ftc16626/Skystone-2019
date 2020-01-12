package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.hardware.SubsystemLift;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.firstinspires.ftc.teamcode.util.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadType;

public class SubsystemTeleLift extends Subsystem implements GamepadListener {
  private final Robot robot;
  private final DriverInterface driverInterface;

  private double speed = 80;
  private double downSpeed = 20;
  private int position = 0;

  public SubsystemTeleLift(Robot robot, DriverInterface driverInterface) {
    this.driverInterface = driverInterface;
    driverInterface.addListener(this);

    this.robot = robot;
  }

  @Override
  public void update() {
    double leftStick = -driverInterface.aid.gamepad.left_stick_y;
    if(leftStick> 0) {
      position += leftStick * speed;
    } else if(leftStick < 0) {
      position += leftStick * downSpeed;
    }

    position = Range.clip(position, 0, SubsystemLift.MAX_HEIGHT);
    robot.subsystemLift.setPosition(position);
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {

  }
}
