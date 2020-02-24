package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveConstants;

@Autonomous(name="Tuning - Back and Forth", group="tuning")
public class BackAndForth extends LinearOpMode {

  public static double DISTANCE = 50;

  @Override
  public void runOpMode() throws InterruptedException {
    DriveBaseMecanumOld driveBaseMecanumOld = new DriveBaseMecanumOld(hardwareMap);

    Trajectory trajectoryForward = driveBaseMecanumOld.trajectoryBuilder()
        .forward(DISTANCE)
        .build();

    Trajectory trajectoryBackward = new TrajectoryBuilder(trajectoryForward.end(),trajectoryForward.end().getHeading(),
        DriveConstants.BASE_CONSTRAINTS)
        .back(DISTANCE)
        .build();

    waitForStart();

    while(opModeIsActive() && !isStopRequested()) {
      driveBaseMecanumOld.followTrajectorySync(trajectoryForward);
      driveBaseMecanumOld.followTrajectorySync(trajectoryBackward);
    }
  }
}
