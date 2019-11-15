package org.firstinspires.ftc.teamcode.auto;

import com.ftc16626.missioncontrol.MissionControl;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.openftc.revextensions2.RevBulkData;

@Autonomous(name="Pull Foundation - Red")
public class AutoOpPullFoundationRed extends LinearOpMode {

  RevBulkData bulkData;

  private MainHardware robot;
  private ElapsedTime runtime = new ElapsedTime();

  private MissionControl missionControl;

  @Override
  public void runOpMode() throws InterruptedException {
    missionControl = ((FtcRobotControllerActivity) hardwareMap.appContext).missionControl;

    robot = new MainHardware(hardwareMap);

    telemetry.addData("Status", "Initialized");
    telemetry.update();

    waitForStart();
    runtime.reset();

    robot.init();

    bulkData = robot.expansionHubMain.getBulkInputData();

    robot.drive.setAngle(Math.toRadians(135));
    robot.drive.setTurn(0);
    robot.drive.setPower(0.75);
    while(opModeIsActive() && runtime.seconds() < 0.5) {
      robot.update();

      telemetry.addData("STEP", "1");
      telemetry.update();
    }

    runtime.reset();

    robot.drive.setAngle(Math.toRadians(225));
    while(opModeIsActive() && runtime.seconds() < 1.50) {//bulkData.getMotorCurrentPosition(robot.drive.motorFrontLeft) > -1900) {
      bulkData = robot.expansionHubMain.getBulkInputData();

      robot.update();
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.addData("STEP", "2");
      telemetry.update();

      if(runtime.seconds() > 1) robot.backServo.setPosition(0);
    }

    robot.drive.setPower(0);
//    robot.backServo.setPosition(0);
    runtime.reset();
    while(opModeIsActive() && runtime.seconds() < 0.4) {

    }

    runtime.reset();

    robot.drive.setPower(0.75);
    robot.drive.setAngle(Math.toRadians(45));
    while(opModeIsActive() && runtime.seconds() < 2.1) {//bulkData.getMotorCurrentPosition(robot.drive.motorFrontLeft) > -1900) {
      bulkData = robot.expansionHubMain.getBulkInputData();

      robot.update();
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.addData("STEP", "3");
      telemetry.update();
    }

//    robot.drive.setPower(0);
    runtime.reset();

    robot.drive.setAngle(Math.toRadians(315));
    robot.backServo.setPosition(1);
    while(opModeIsActive() && runtime.seconds() < 1.8) {
      bulkData = robot.expansionHubMain.getBulkInputData();

      robot.update();
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.addData("STEP", "4");
      telemetry.update();
    }

    runtime.reset();

    robot.drive.setAngle(Math.toRadians(225));
    while(opModeIsActive() && runtime.seconds() < 0.5) {
      bulkData = robot.expansionHubMain.getBulkInputData();

      robot.update();
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.addData("STEP", "4.5");
      telemetry.update();
    }


    runtime.reset();

    robot.drive.setAngle(Math.toRadians(180));
    while(opModeIsActive() && runtime.seconds() < 1) {
      bulkData = robot.expansionHubMain.getBulkInputData();

      robot.update();
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.addData("STEP", "5");
      telemetry.update();
    }

    runtime.reset();

    robot.drive.setAngle(Math.toRadians(315));
    while(opModeIsActive() && runtime.seconds() < 1) {
      bulkData = robot.expansionHubMain.getBulkInputData();

      robot.update();
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.addData("STEP", "6");
      telemetry.update();
    }

    robot.swingyServo.setPosition(1);

    robot.starboardServo.setPosition(0);
//    robot.backServo.setPosition(0);

//    runtime.reset();
//    robot.drive.setAngle(Math.toRadians(-45));
//    while(opModeIsActive() && (runtime.seconds() < 1.0)) {
//      robot.update();
//      telemetry.addData("Pos X", robot.drive.getPosition().getX());
//      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
//      telemetry.addData("STEP", "2");
//      telemetry.update();
//    }
//
//    runtime.reset();
//    robot.drive.setAngle(Math.toRadians(225));
//    while(opModeIsActive() && (runtime.seconds() < 1.0)) {
//      robot.update();
//      telemetry.addData("Pos X", robot.drive.getPosition().getX());
//      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
//      telemetry.addData("STEP", "2");
//      telemetry.update();
//    }

    while(opModeIsActive() && runtime.seconds() < 0.5) {

    }

    robot.drive.stopMotors();
  }

}
