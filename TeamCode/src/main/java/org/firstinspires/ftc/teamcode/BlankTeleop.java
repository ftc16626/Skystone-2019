package org.firstinspires.ftc.teamcode;

import com.ftc16626.missioncontrol.MissionControl;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

@TeleOp(name = "Blank TeleOp", group = "Testing")
public class BlankTeleop extends OpMode {

  private MissionControl missionControl;

  @Override
  public void init() {
    missionControl = ((FtcRobotControllerActivity) hardwareMap.appContext).missionControl;
//    missionControl.getPilotProfileHandler().addProfile(
//        new PilotProfile("Enzo's Config", StickControl.STRAFE_LEFT_TURN_RIGHT_STICK, false, false,
//            true, false, StickResponseCurve.CUBED, false));

//    context.missionControl.getPilotProfileHandler().getCurrentProfile();
  }

  @Override
  public void loop() {

  }
}
