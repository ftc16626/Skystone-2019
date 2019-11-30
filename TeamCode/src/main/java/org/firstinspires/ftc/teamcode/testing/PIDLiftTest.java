package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadType;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.subsystem.system.SubsystemPIDLift;

@TeleOp(group="testing")
public class PIDLiftTest extends RadicalOpMode implements GamepadListener {
  private DriverInterface driverInterface;
  private SubsystemPIDLift pidLift;

  @Override
  public void extendedInit() {
    driverInterface = new DriverInterface(gamepad1, gamepad2, this);

    pidLift = new SubsystemPIDLift(robot, this, driverInterface);

    subsystemHandler.add(pidLift);
  }

  @Override
  public void extendedLoop() {
    driverInterface.update();
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    pidLift.actionPerformed(eventName, eventType, gamepadType);
  }
}
