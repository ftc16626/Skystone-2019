package org.firstinspires.ftc.teamcode.auto.delivery;

import android.util.Log;
import com.acmerobotics.roadrunner.drive.Drive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import org.firstinspires.ftc.teamcode.auto.RoadRunnerBaseOpmode;
import org.firstinspires.ftc.teamcode.hardware.RadicalRoadRunnerDriveBase;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.hardware.util.DcMotorCached;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.tuning.DriveBaseMecanumOld;
import org.openftc.revextensions2.ExpansionHubMotor;

public class DeliveryPathRedRight extends HardwareSubsystem{

  Trajectory trajectory;
  RadicalRoadRunnerDriveBase drive;
//  DriveBaseMecanumOld drive;

  private double startX = -32.5;
  private double startY = -62.5;
  private double startHeading = Math.toRadians(90);

  DriveBaseMecanumOld test;

  Robot robot;

  public DeliveryPathRedRight(Robot robot, RoadRunnerBaseOpmode opmode) {
    super(robot, opmode);

    this.robot = robot;

//    drive = opmode.drive;
//    drive.setPoseEstimate(new Pose2d(startX, startY, startHeading));

    test = new DriveBaseMecanumOld(getRobot().hwMap);
  }

  @Override
  public void onInit() {
    trajectory = test.trajectoryBuilder()
        .forward(15)
        .build();
//        .splineTo(new Pose2d(-20.0, -30.0, Math.toRadians(120))).build();
  }

  @Override
  public void onMount() {
    test.followTrajectory(trajectory);
  }

  @Override
  public void update() {
    test.update();
//    drive.update();
  }
}
