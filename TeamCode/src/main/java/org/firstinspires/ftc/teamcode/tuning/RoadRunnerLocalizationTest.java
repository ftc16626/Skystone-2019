package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;
import org.firstinspires.ftc.teamcode.hardware.SampleMecanumDriveBase;
import org.firstinspires.ftc.teamcode.hardware.SampleMecanumDriveREV;

@Config
@TeleOp(group = "drive")
public class RoadRunnerLocalizationTest extends LinearOpMode {
  public static double VX_WEIGHT = 1;
  public static double VY_WEIGHT = 1;
  public static double OMEGA_WEIGHT = 1;

  @Override
  public void runOpMode() throws InterruptedException {
    telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

    SampleMecanumDriveBase drive = new DriveBaseMecanum(hardwareMap);

    waitForStart();

    while (!isStopRequested()) {
      Pose2d baseVel = new Pose2d(
          -gamepad1.left_stick_y,
          -gamepad1.left_stick_x,
          -gamepad1.right_stick_x
      );

      Pose2d vel;
      if (Math.abs(baseVel.getX()) + Math.abs(baseVel.getY()) + Math.abs(baseVel.getHeading()) > 1) {
        // re-normalize the powers according to the weights
        double denom = VX_WEIGHT * Math.abs(baseVel.getX())
            + VY_WEIGHT * Math.abs(baseVel.getY())
            + OMEGA_WEIGHT * Math.abs(baseVel.getHeading());
        vel = new Pose2d(
            VX_WEIGHT * baseVel.getX(),
            VY_WEIGHT * baseVel.getY(),
            OMEGA_WEIGHT * baseVel.getHeading()
        ).div(denom);
      } else {
        vel = baseVel;
      }

      drive.setDrivePower(vel);

      drive.update();

      Pose2d poseEstimate = drive.getPoseEstimate();
      telemetry.addData("x", poseEstimate.getX());
      telemetry.addData("y", poseEstimate.getY());
      telemetry.addData("heading", Math.toDegrees(poseEstimate.getHeading()));
//      telemetry.addData("heading", Math.toDegrees(drive.getExternalHeading()));
      telemetry.update();
    }
  }
}
