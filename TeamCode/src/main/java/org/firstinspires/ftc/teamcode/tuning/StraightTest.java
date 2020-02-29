package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
@Autonomous(name="Tuning - Straight Test", group = "tuning")
public class StraightTest extends LinearOpMode {
  public static double DISTANCE = 100;

  @Override
  public void runOpMode() throws InterruptedException {
    DriveBaseMecanumOld drive = new DriveBaseMecanumOld(hardwareMap);

    Trajectory trajectory = drive.trajectoryBuilder()
        .forward(DISTANCE)
        .build();

    waitForStart();

    if (isStopRequested()) return;

    drive.followTrajectory(trajectory);

    while(opModeIsActive()) {
      drive.update();

      TelemetryPacket packet = new TelemetryPacket();

      Pose2d estimate = drive.getPoseEstimate();
      packet.put("x", estimate.getX());
      packet.put("y", estimate.getY());
      packet.put("heading", Math.toDegrees(estimate.getHeading()));

      FtcDashboard.getInstance().sendTelemetryPacket(packet);
    }
  }
}
