package org.firstinspires.ftc.teamcode.testing;

import com.ftc16626.missioncontrol.MissionControl;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

@TeleOp(name ="Square Auto Op", group = "Testing")
public class SquareAutoOp extends LinearOpMode {

  private MainHardware robot;

  private MissionControl missionControl;

  @Override
  public void runOpMode() {
    missionControl = ((FtcRobotControllerActivity) hardwareMap.appContext).missionControl;

    robot = new MainHardware(hardwareMap);
    robot.init();

    telemetry.addData("Status", "Initialized");
    telemetry.update();

    waitForStart();

    robot.drive.setAngle(Math.toRadians(45));
    robot.drive.setPower(0.3);
    while(robot.drive.getPosition().getX() < 30) {
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.addData("Heading", robot.drive.getHeading());
      telemetry.addData("Status", "Step 1");
      telemetry.update();
      robot.update();
    }

    robot.drive.setAngle(Math.toRadians(45 + 270));
    while(robot.drive.getPosition().getY() < 30) {
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.addData("Heading", robot.drive.getHeading());
      telemetry.addData("Status", "Step 2");
      telemetry.update();
      robot.update();
    }

    robot.drive.setAngle(Math.toRadians(45 + 180));
    while(robot.drive.getPosition().getX() > 0) {
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.addData("Heading", robot.drive.getHeading());
      telemetry.addData("Status", "Step 3");
      telemetry.update();
      robot.update();
    }

    robot.drive.setAngle(Math.toRadians(45 + 90));
    while(robot.drive.getPosition().getY() > 0) {
      telemetry.addData("Pos X", robot.drive.getPosition().getX());
      telemetry.addData("Pos Y", robot.drive.getPosition().getY());
      telemetry.addData("Heading", robot.drive.getHeading());
      telemetry.addData("Status", "Step 4");
      telemetry.update();
      robot.update();
    }

    robot.drive.stopMotors();
  }
}
