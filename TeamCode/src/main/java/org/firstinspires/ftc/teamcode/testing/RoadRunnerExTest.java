package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveConstants;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.hardware.roadrunnerex.RoadRunnerExMecanum;
import org.firstinspires.ftc.teamcode.hardware.roadrunnerex.TrajectoryEx;
import org.firstinspires.ftc.teamcode.tuning.DriveBaseMecanumOld;

@Autonomous
public class RoadRunnerExTest extends LinearOpMode {

  boolean someCondition = false;

  @Override
  public void runOpMode() throws InterruptedException {
    Pose2d startPose = new Pose2d(0, 0, 0);

    RoadRunnerExMecanum driveBase = new RoadRunnerExMecanum(new DriveBaseMecanumOld(hardwareMap));

    TrajectoryEx trajectory = new TrajectoryEx(startPose, startPose.getHeading(),
        DriveConstants.BASE_CONSTRAINTS);

    trajectory
        .lineTo(new Vector2d(24.0, 0)) // Drive to x: 10, y: 5
        .turn(Math.toRadians(90))              // Point turn
        .setMaxVelocity(30)                    // Temporarily limit max velocity
//        .splineTo(new Pose2d(15, 10, Math.toRadians(30))) // Spline to x: 15, y: 10, 30 degrees heading
//        .waitFor(() -> {                       // Will wait until someCondition reaches true
//          return someCondition;
//        })
        .resetConstraints()                    // Resets the maxVelocity and other constraints
        .turn(Math.toRadians(90))              // Point turn
        .waitSeconds(3)
//        .addDisplacementMarker(() -> {
          // Lower servo or something
//        })
//        .reverse()
//        .splineTo(new Pose2d(10, 30, Math.toRadians(45))) // Splines backwards because of reverse
        .turn(Math.toRadians(90))// Splines backwards because of reverse
        .lineTo(new Vector2d(0, 0)) // Splines backwards because of reverse
        .build();

    waitForStart();

    driveBase.followTrajectorySync(trajectory);
  }
}
