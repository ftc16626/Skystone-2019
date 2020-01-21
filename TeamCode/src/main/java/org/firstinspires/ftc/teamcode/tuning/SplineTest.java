package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="Tuning - Spline Test", group = "tuning")
public class SplineTest extends LinearOpMode {
  @Override
  public void runOpMode() throws InterruptedException {
    DriveBaseMecanumOld drive = new DriveBaseMecanumOld(hardwareMap);
    drive.setPoseEstimate(new Pose2d(-15, -63, Math.toRadians(90)));

    waitForStart();

    if (isStopRequested()) return;

    drive.followTrajectory(
        drive.trajectoryBuilder()
//            .splineTo(new Pose2d(20, -20, Math.toRadians(0)))
            .splineTo(new Pose2d(-20, -30, Math.toRadians(120)))
            .splineTo(new Pose2d(20.0, -35.0, Math.toRadians(180.0)))
            .build()
    );

    while(opModeIsActive()) {
      drive.update();

    }

    sleep(2000);

//    drive.followTrajectorySync(
//        drive.trajectoryBuilder()
//            .reverse()
//            .splineTo(new Pose2d(0, 0, 0))
//            .build()
//    );
  }
}