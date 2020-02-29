package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.util.DashboardUtil;

@Autonomous(name = "Tuning - Hand Tuning", group = "tuning")
public class HandTuning extends LinearOpMode {

  @Override
  public void runOpMode() throws InterruptedException {
    DriveBaseMecanumOld drive = new DriveBaseMecanumOld(hardwareMap);

    waitForStart();

    while (opModeIsActive()) {
      TelemetryPacket packet = new TelemetryPacket();

      Pose2d estimate = drive.getPoseEstimate();
      packet.put("x", estimate.getX());
      packet.put("y", estimate.getY());
      packet.put("heading", Math.toDegrees(estimate.getHeading()));
      packet.put("imu", Math.toDegrees(drive.getRawExternalHeading()));

      Canvas fieldOverlay = packet.fieldOverlay();
      fieldOverlay.setStroke("#F44336");
      DashboardUtil.drawRobot(fieldOverlay,
          new Pose2d(estimate.getX(), estimate.getY(), estimate.getHeading()));

//      FtcDashboard.getInstance().sendTelemetryPacket(packet);

      drive.update();
    }
  }
}
