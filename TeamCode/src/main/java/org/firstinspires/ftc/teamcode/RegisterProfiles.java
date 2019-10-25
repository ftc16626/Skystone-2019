package org.firstinspires.ftc.teamcode;

import com.ftc16626.missioncontrol.MissionControl;
import com.ftc16626.missioncontrol.util.profiles.PilotProfile;
import com.ftc16626.missioncontrol.util.profiles.StickControl;
import com.ftc16626.missioncontrol.util.profiles.StickResponseCurve;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

@TeleOp(name = "Register Profiles", group = "Mission Control")
public class RegisterProfiles extends OpMode {

  private MissionControl missionControl;

  private PilotProfile enzoProfile = new PilotProfile("Enzo's Profile",
      StickControl.STRAFE_LEFT_TURN_RIGHT_STICK, false, false,
      true, false, StickResponseCurve.CUBED, false);
  private PilotProfile emilioProfile = new PilotProfile("Emilio's Profile",
      StickControl.STRAFE_LEFT_TURN_RIGHT_STICK, true, false,
      true, false, StickResponseCurve.CUBED, false);

  @Override
  public void init() {
    missionControl = ((FtcRobotControllerActivity) hardwareMap.appContext).missionControl;
    missionControl.getPilotProfileHandler().addProfile(enzoProfile);
    missionControl.getPilotProfileHandler().addProfile(emilioProfile);
  }

  @Override
  public void loop() {

  }
}
