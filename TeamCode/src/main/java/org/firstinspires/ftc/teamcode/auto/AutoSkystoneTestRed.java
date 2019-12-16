package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

@Autonomous
public class AutoSkystoneTestRed extends LinearOpMode {

  @Override
  public void runOpMode() throws InterruptedException {
    final MainHardware robot = new MainHardware(hardwareMap);
    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    waitForStart();
    robot.init();
    robot.dropSliderServo();

    robot.intake.directionBackward();
    robot.intake.toggle(true);

    robot.intake.open();
    Trajectory trajectory1 = new TrajectoryBuilder(drive.getPoseEstimate(),
        new DriveConstraints(400, 450, 0.0, Math.toRadians(180.0), Math.toRadians(180.0), 0.0))
        .strafeLeft(-50)
        .forward(850)
        .addMarker(new Function0<Unit>() {
          @Override
          public Unit invoke() {
            robot.intake.close();
            return Unit.INSTANCE;
          }
        })
        .back(400)
        .addMarker(new Function0<Unit>() {
          @Override
          public Unit invoke() {
            return Unit.INSTANCE;
          }
        })
        .build();

    Trajectory trajectory2 = drive.trajectoryBuilder()
        .forward(1200)
        .build();

    drive.followTrajectorySync(trajectory1);
//    drive.turnSync(Math.toRadians(-85));
//    drive.followTrajectorySync(trajectory2);

  }
}
