package org.firstinspires.ftc.teamcode.testing;

import com.ftc16626.missioncontrol.MissionControl;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;

@TeleOp(name = "MissionControl Test Op", group="Testing")
public class MissionControlTestOp extends OpMode {

  private MissionControl missionControl;

  private int ticks = 0;

  @Override
  public void init() {
    missionControl = ((FtcRobotControllerActivity) hardwareMap.appContext).missionControl;

    missionControl.sendInitPacket(new String[] { "time-elapsed", "sin" });
    missionControl.startLogging();
    missionControl.broadcast("Starting", "status");
  }

  @Override
  public void loop() {
    missionControl.broadcast(Double.toString(getRuntime()), "time-elapsed");
    missionControl.broadcast(Double.toString(Math.sin(getRuntime())), "sin");

    if(++ticks % 50 == 0) {
      missionControl.broadcast("Tick " + ticks, "ticks");
    }
  }

  @Override
  public void stop() {
    missionControl.stopLogging();
  }
}
