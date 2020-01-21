package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumConstraints;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveConstants;
import org.firstinspires.ftc.teamcode.tuning.DriveBaseMecanumOld;

@Autonomous(name="FUCK", group="FUCK")
public class RecreateCrap extends LinearOpMode {

  private double startX = -32.5;
  private double startY = -62.5;
  private double startHeading = Math.toRadians(90);

  @Override
  public void runOpMode() throws InterruptedException {
    DriveBaseMecanumOld drive = new DriveBaseMecanumOld(hardwareMap);
    drive.setPoseEstimate(new Pose2d(startX, startY, startHeading));

    waitForStart();

    if (isStopRequested()) return;

    drive.followTrajectory(
//        drive.trajectoryBuilder()
        new TrajectoryBuilder(new Pose2d(startX, startY, Math.toRadians(startHeading + 180)), Math.toRadians(90), new MecanumConstraints(DriveConstants.BASE_CONSTRAINTS,
            DriveConstants.TRACK_WIDTH))
//            .splineTo(new Pose2d(20, -20, Math.toRadians(0)))
            .splineTo(new Pose2d(-20, -30, Math.toRadians(120 + 180)))
            .splineTo(new Pose2d(20.0, -35.0, Math.toRadians(180.0 + 180)))
            .build()
    );

    while(opModeIsActive()) {
      drive.update();

    }
  }
}
