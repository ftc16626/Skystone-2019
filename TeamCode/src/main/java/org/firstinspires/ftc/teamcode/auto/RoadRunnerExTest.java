package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveConstants;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.SampleMecanumDriveREV;
import org.firstinspires.ftc.teamcode.hardware.roadrunnerex.RoadRunnerExMecanum;
import org.firstinspires.ftc.teamcode.hardware.roadrunnerex.TrajectoryEx;

public class RoadRunnerExTest extends LinearOpMode {

  boolean someCondition = false;

  @Override
  public void runOpMode() throws InterruptedException {
    Pose2d startPose = new Pose2d(0, 0, 0);

    RoadRunnerExMecanum driveBase = new RoadRunnerExMecanum(new SampleMecanumDriveREV(hardwareMap));

    TrajectoryEx trajectory = new TrajectoryEx(startPose, startPose.getHeading(),
        DriveConstants.BASE_CONSTRAINTS);

    trajectory
        .lineTo(new Vector2d(10.0, 5.0)) // Drive to x: 10, y: 5
        .turn(Math.toRadians(30))              // Point turn
        .setMaxVelocity(30)                    // Temporarily limit max velocity
        .splineTo(new Pose2d(15, 10, Math.toRadians(30))) // Spline to x: 15, y: 10, 30 degrees heading
        .waitFor(() -> {                       // Will wait until someCondition reaches true
          return someCondition;
        })
        .resetConstraints()                    // Resets the maxVelocity and other constraints
        .turn(Math.toRadians(90))              // Point turn
        .waitSeconds(3)
        .addDisplacementMarker(() -> {
          // Lower servo or something
        })
        .reverse()
        .splineTo(new Pose2d(10, 30, Math.toRadians(45))) // Splines backwards because of reverse
        .build();

    driveBase.followTrajectorySync(trajectory);
  }
}
