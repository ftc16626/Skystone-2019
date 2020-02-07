package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Config
@Autonomous(name="Tuning - Strafe Test", group = "tuning")
public class StrafeTest extends LinearOpMode {
  public static double DISTANCE = 50;

  @Override
  public void runOpMode() throws InterruptedException {
    DriveBaseMecanumOld drive = new DriveBaseMecanumOld(hardwareMap);

    Trajectory trajectory = drive.trajectoryBuilder()
        .strafeRight(DISTANCE)
        .build();

    waitForStart();

    if (isStopRequested()) return;

    drive.followTrajectorySync(trajectory);
  }
}
