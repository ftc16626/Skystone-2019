package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumConstraints;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveConstants;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.tuning.DriveBaseMecanumOld;

@Autonomous(name="FUCK2", group="FUCK")
public class RecreateCrap2 extends RadicalOpMode {
  private double startX = -32.5;
  private double startY = -62.5;
  private double startHeading = Math.toRadians(90);

  private Pose2d startPose = new Pose2d(startX, startY, startHeading);

  DriveBaseMecanumOld drive;

  Trajectory trajectory1;

  @Override
  public void onInit() {
    robot.subsystemDriveTrainMecanum.turnOff();

    drive = new DriveBaseMecanumOld(hardwareMap);
    drive.setPoseEstimate(startPose);

    trajectory1 = new TrajectoryBuilder(startPose, startHeading, new MecanumConstraints(
        DriveConstants.BASE_CONSTRAINTS,
        DriveConstants.TRACK_WIDTH))
//            .splineTo(new Pose2d(20, -20, Math.toRadians(0)))
        .splineTo(new Pose2d(-20, -30, Math.toRadians(120)))
//            .splineTo(new Pose2d(20.0, -35.0, Math.toRadians(180.0 + 180)))
        .build();
  }

  @Override
  public void onMount() {
    robot.subsystemStoneGuide.drop();
    drive.followTrajectorySync(trajectory1);
  }

  @Override
  public void update() {
//    drive.update();
  }
}
