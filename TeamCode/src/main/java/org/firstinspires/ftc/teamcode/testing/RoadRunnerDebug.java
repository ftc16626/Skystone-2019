package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;

@Autonomous(group="testing")
public class RoadRunnerDebug extends LinearOpMode {
  @Override
  public void runOpMode() {
    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    Trajectory trajectory = drive.trajectoryBuilder().forward(5000).build();

    waitForStart();

    if(isStopRequested()) {
      return;
    }

    drive.followTrajectorySync(trajectory);
  }
}

/**
 * kV = 0.00727
 * kStatic = 0.01306 (R^2 = 1.00)
 * kA = 0.0008 (R^2 = 0.09)
 */