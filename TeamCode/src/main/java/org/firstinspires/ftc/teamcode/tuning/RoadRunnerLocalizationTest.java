package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;

@TeleOp(group = "tuning")
public class RoadRunnerLocalizationTest extends LinearOpMode {
  @Override
  public void runOpMode() throws InterruptedException {
    telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    waitForStart();

    while (!isStopRequested()) {
      drive.setDrivePower(new Pose2d(
          -gamepad1.left_stick_y,
          -gamepad1.left_stick_x,
          -gamepad1.right_stick_x
      ));

      drive.update();

      Pose2d poseEstimate = drive.getPoseEstimate();
      telemetry.addData("x", poseEstimate.getX());
      telemetry.addData("y", poseEstimate.getY());
      telemetry.addData("heading", poseEstimate.getHeading());
      telemetry.update();
    }
  }
}