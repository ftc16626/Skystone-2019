package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

@Autonomous(name="Tuning - Spline Test", group = "tuning")
public class SplineTest extends LinearOpMode {
  @Override
  public void runOpMode() throws InterruptedException {
    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    waitForStart();

    if (isStopRequested()) return;

    drive.followTrajectory(
        drive.trajectoryBuilder()
            .splineTo(new Pose2d(1000, -300, Math.toRadians(90)))
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