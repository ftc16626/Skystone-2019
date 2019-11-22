package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;

@Autonomous(name="Tuning - Spline Test", group = "tuning")
public class SplineTest extends LinearOpMode {
  @Override
  public void runOpMode() throws InterruptedException {
    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    waitForStart();

    if (isStopRequested()) return;

    drive.followTrajectorySync(
        drive.trajectoryBuilder()
            .splineTo(new Pose2d(7620, 7620, 0))
            .build()
    );

    sleep(2000);

    drive.followTrajectorySync(
        drive.trajectoryBuilder()
            .reverse()
            .splineTo(new Pose2d(0, 0, 0))
            .build()
    );
  }
}