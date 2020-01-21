package org.firstinspires.ftc.teamcode.auto.delivery;

import android.util.Log;
import com.acmerobotics.roadrunner.drive.Drive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import org.firstinspires.ftc.teamcode.auto.RoadRunnerBaseOpmode;
import org.firstinspires.ftc.teamcode.hardware.RadicalRoadRunnerDriveBase;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.tuning.DriveBaseMecanumOld;

public class DeliveryPathRedRight extends HardwareSubsystem{

  Trajectory trajectory;
//  RadicalRoadRunnerDriveBase drive;
  DriveBaseMecanumOld drive;

  private double startX = -32.5;
  private double startY = -62.5;
  private double startHeading = Math.toRadians(90);

  public DeliveryPathRedRight(Robot robot, RoadRunnerBaseOpmode opmode) {
    super(robot, opmode);

//    drive = opmode.drive
    drive = new DriveBaseMecanumOld(robot.hwMap);
    drive.setPoseEstimate(new Pose2d(startX, startY, startHeading));
  }

  @Override
  public void onInit() {
    trajectory = drive.trajectoryBuilder()
        .splineTo(new Pose2d(-20.0, -30.0, Math.toRadians(120))).build();
  }

  @Override
  public void onMount() {
    drive.followTrajectory(trajectory);
  }

  @Override
  public void update() {
    drive.update();
  }
}
