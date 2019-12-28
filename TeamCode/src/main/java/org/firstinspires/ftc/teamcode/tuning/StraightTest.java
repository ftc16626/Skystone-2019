package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveBaseMecanum;

/*
 * This is a simple routine to test translational drive capabilities.
 */
@Config
@Autonomous(name="Tuning - Straight Test", group = "tuning")
public class StraightTest extends LinearOpMode {
  public static double DISTANCE = 1000;

  @Override
  public void runOpMode() throws InterruptedException {
    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    Trajectory trajectory = drive.trajectoryBuilder()
        .forward(DISTANCE)
        .build();

    waitForStart();

    if (isStopRequested()) return;

    drive.followTrajectorySync(trajectory);
  }
}
