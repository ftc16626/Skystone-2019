package org.firstinspires.ftc.teamcode.auto.delivery;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import org.firstinspires.ftc.teamcode.auto.RoadRunnerBaseOpmode;
import org.firstinspires.ftc.teamcode.hardware.RadicalRoadRunnerDriveBase;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;

public class DeliveryPathRedRight extends HardwareSubsystem{

  Trajectory trajectory;
  RadicalRoadRunnerDriveBase drive;

  public DeliveryPathRedRight(Robot robot, RoadRunnerBaseOpmode opmode) {
    super(robot, opmode);

    drive = opmode.drive;

    trajectory = drive.trajectoryBuilder().forward(500).build();
  }

  @Override
  public void onMount() {
    drive.followTrajectory(trajectory);
  }
}
