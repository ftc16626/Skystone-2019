package org.firstinspires.ftc.teamcode.testing;

import com.ftc16626.missioncontrol.MissionControl;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

@TeleOp(name = "Kinematics Test OP", group = "Testing")
@Disabled
public class KinematicsTestOp extends LinearOpMode {

  private MainHardware robot;
  private ElapsedTime runtime = new ElapsedTime();

  private MissionControl missionControl;

  @Override
  public void runOpMode() {
    missionControl = ((FtcRobotControllerActivity) hardwareMap.appContext).missionControl;

    robot = new MainHardware(hardwareMap);
    robot.init();

    telemetry.addData("Status", "Initialized");
    telemetry.update();

    waitForStart();
    runtime.reset();

    robot.drive.setAngle(Math.toRadians(45));
    robot.drive.setPower(0.75);
    while(opModeIsActive() && (runtime.seconds() < 2.0)) {
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.addData("Heading", robot.drive.getHeading());
      telemetry.addData("Status", "Step 1");
      telemetry.update();
      robot.update();
    }

    runtime.reset();

    robot.drive.setAngle(Math.toRadians(135));
    while(opModeIsActive() && (runtime.seconds() < 2.0)) {
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.addData("Heading", robot.drive.getHeading());
      telemetry.addData("Status", "Step 2");
      telemetry.update();
      robot.update();
    }

    robot.drive.stopMotors();
  }
}
