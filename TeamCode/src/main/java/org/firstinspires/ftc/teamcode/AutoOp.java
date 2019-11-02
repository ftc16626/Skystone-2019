package org.firstinspires.ftc.teamcode;

import com.ftc16626.missioncontrol.MissionControl;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

public class AutoOp extends LinearOpMode {

  private MainHardware robot;
  private ElapsedTime runtime = new ElapsedTime();

  private MissionControl missionControl;

  @Override
  public void runOpMode() throws InterruptedException {
    missionControl = ((FtcRobotControllerActivity) hardwareMap.appContext).missionControl;

    robot = new MainHardware(hardwareMap);
    robot.init();

    telemetry.addData("Status", "Initialized");
    telemetry.update();

    waitForStart();
    runtime.reset();

    robot.drive.setAngle(Math.toRadians(90));
    robot.drive.setPower(0.75);
    while(opModeIsActive() && (runtime.seconds() < 2.0)) {
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.update();
    }

    runtime.reset();
    robot.drive.setAngle(Math.toRadians(0));
    while(opModeIsActive() && (runtime.seconds() < 2.0)) {
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.update();
    }

    runtime.reset();
    robot.drive.setAngle(Math.toRadians(270));
    while(opModeIsActive() && (runtime.seconds() < 2.0)) {
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.update();
    }


    robot.drive.stopMotors();
  }

}
