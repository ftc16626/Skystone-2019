package org.firstinspires.ftc.teamcode.subsystem.system;

import com.ftc16626.missioncontrol.MissionControl;
import com.ftc16626.missioncontrol.util.profiles.PilotProfile;
import com.ftc16626.missioncontrol.util.profiles.StickControl;
import com.ftc16626.missioncontrol.util.profiles.StickResponseCurve;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadType;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.jetbrains.annotations.NotNull;

public class SubsystemDriverControl extends Subsystem implements GamepadListener {

  private DriverInterface driverInterface;
  private MissionControl missionControl;

  private SubsytemDriveMecanum mecanumDrive;
  private SubsystemDriverBackServos backServos;
  private SubsystemDriverLiftIntake liftIntake;

  private PilotProfile enzoProfile = new PilotProfile("Enzo's Profile",
      StickControl.STRAFE_LEFT_TURN_RIGHT_STICK, false, false,
      true, false, StickResponseCurve.CUBED, false);

  public SubsystemDriverControl(
      @NotNull MainHardware robot, @NotNull RadicalOpMode opMode) {
    super(robot, opMode);

    mecanumDrive = new SubsytemDriveMecanum(robot, opMode, driverInterface);
    backServos = new SubsystemDriverBackServos(robot, opMode, driverInterface);
    liftIntake = new SubsystemDriverLiftIntake(robot, opMode, driverInterface);

    getSubsystemHandler().add(mecanumDrive);
    getSubsystemHandler().add(backServos);
    getSubsystemHandler().add(liftIntake);
  }

  @Override
  public void onInit() {
    missionControl = ((FtcRobotControllerActivity) getOpMode().hardwareMap.appContext).missionControl;

    driverInterface = new DriverInterface(getOpMode().gamepad1, getOpMode().gamepad2, this);
//    driverInterface.driver.setProfile(missionControl.getPilotProfileHandler().getCurrentProfile());
    driverInterface.driver.setProfile(enzoProfile);

    mecanumDrive.driverInterface = driverInterface;
    backServos.driverInterface = driverInterface;
    liftIntake.driverInterface = driverInterface;
  }

  @Override
  public void onMount() {
    getRobot().init();
    getRobot().dropSliderServo();
  }

  @Override
  public void update() {
    driverInterface.update();

    getRobot().update();
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    backServos.actionPerformed(eventName, eventType, gamepadType);
    liftIntake.actionPerformed(eventName, eventType, gamepadType);
  }
}
