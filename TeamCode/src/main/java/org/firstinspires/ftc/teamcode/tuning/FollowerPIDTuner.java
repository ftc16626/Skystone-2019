package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveBaseMecanum;

@Config
@Autonomous(name="Tuning - Follower PID", group = "tuning")
public class FollowerPIDTuner extends LinearOpMode {
  public static double DISTANCE = 700;

  @Override
  public void runOpMode() throws InterruptedException {
    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    drive.setPoseEstimate(new Pose2d(-DISTANCE / 2, -DISTANCE / 2, 0));

    waitForStart();

    if (isStopRequested()) return;

    while (!isStopRequested()) {
      drive.followTrajectorySync(
          drive.trajectoryBuilder()
              .forward(DISTANCE)
              .build()
      );
      drive.turnSync(Math.toRadians(90));
    }
  }
}
