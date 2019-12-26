package org.firstinspires.ftc.teamcode.teleop;

import com.ftc16626.missioncontrol.util.profiles.PilotProfile;
import com.ftc16626.missioncontrol.util.profiles.StickControl;
import com.ftc16626.missioncontrol.util.profiles.StickResponseCurve;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.util.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadType;

public class MainTeleop extends RadicalOpMode implements GamepadListener {
  private final SubsystemTeleDrive subsystemTeleDrive;
  private final SubsystemTeleIntake subsystemTeleIntake;

  private DriverInterface driverInterface;

  private PilotProfile enzoProfile = new PilotProfile("Enzo's Profile",
      StickControl.STRAFE_LEFT_TURN_RIGHT_STICK, false, false,
      true, false, StickResponseCurve.CUBED, false);

  public MainTeleop() {
    driverInterface = new DriverInterface(gamepad1, gamepad2, this);
    driverInterface.driver.setProfile(enzoProfile);

    Robot robot = new Robot(hardwareMap, this);
    subsystemTeleDrive = new SubsystemTeleDrive(robot, driverInterface);
    subsystemTeleIntake = new SubsystemTeleIntake(robot, driverInterface);

    subsystemHandler.add(robot);
    subsystemHandler.add(subsystemTeleDrive);
    subsystemHandler.add(subsystemTeleIntake);
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {

  }
}
