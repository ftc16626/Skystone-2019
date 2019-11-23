package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanumOld;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

@Config
@Autonomous(name = "Auto - Block Auto Red", group = "testing")
public class BlockAutoRed extends LinearOpMode {

  public static double step1 = 650;
  public static double step2 = 150;
  public static double step3 = 1600;
  public static double step4 = step3 + 190 - 150;
  public static double step5 = step2 + 90;
  public static double step6 = step3 + 220;
  //  public static double step7 = 250;
  public static double step7 = 1120 + 80;
  public static double step8 = 280 - 50;
  public static double step9 = 1500;

  //  public static double turn1 = Math.toRadians(-90);
  MainHardware robot;

  ElapsedTime runtime = new ElapsedTime();

  @Override
  public void runOpMode() {
    robot = new MainHardware(hardwareMap);
    DriveBaseMecanumOld drive = new DriveBaseMecanumOld(hardwareMap);

    Trajectory trajectory1 = drive.trajectoryBuilder()
        .back(step1).build();
    Trajectory trajectory2 = drive.trajectoryBuilder().forward(step2).strafeLeft(step3).build();
    Trajectory trajectory3 = drive.trajectoryBuilder().strafeRight(step4).back(step5).build();
    Trajectory trajectory4 = drive.trajectoryBuilder().forward(step2).strafeLeft(step6).build();
//    Trajectory trajectory5 = drive.trajectoryBuilder().strafeRight(step7).build();
    Trajectory trajectory5 = drive.trajectoryBuilder().strafeRight(step7).back(step8).build();
    Trajectory trajectory6 = drive.trajectoryBuilder().strafeRight(step2).build();
    Trajectory trajectory7 = drive.trajectoryBuilder().back(step9).build();
    Trajectory trajectory8 = drive.trajectoryBuilder().forward(step9 - 780).build();

//    robot.backRightServo.setPosition(1);
//    robot.raiseBackRightServo();
    robot.backRightServo.setPosition(0.55);

    robot.swingyServo.setPosition(1);

    double startingAngle = drive.getRawExternalHeading();

    waitForStart();

    if (isStopRequested()) {
      return;
    }

    robot.init();

    drive.followTrajectorySync(trajectory1);

//    robot.backRightServo.setPosition(0);
    robot.perpendicularBackRightServo();
    robot.swingyServo.setPosition(1);

    runtime.reset();
    while (runtime.seconds() < 0.8) {
      ;
    }

    drive.followTrajectorySync(trajectory2);

//    robot.raiseBackRightServo();
    robot.backRightServo.setPosition(0.55);


    drive.turnSync(-(drive.getRawExternalHeading() - startingAngle));
    drive.followTrajectorySync(trajectory3);

    robot.perpendicularBackRightServo();

    runtime.reset();
    while (runtime.seconds() < 0.8) {

    }

    drive.followTrajectorySync(trajectory4);

//    robot.raiseBackRightServo();
    robot.backRightServo.setPosition(0.55);
    drive.turnSync(-(drive.getRawExternalHeading() - startingAngle));

    runtime.reset();
    while (runtime.seconds() < 0.8) {
    }

    drive.followTrajectorySync(trajectory5);

    robot.perpendicularBackRightServo();

    runtime.reset();
    while (runtime.seconds() < 0.8) {
    }

    drive.followTrajectorySync(trajectory6);

    drive.turnSync(Math.toRadians(-90));

    drive.followTrajectorySync(trajectory7);

    robot.backRightServo.setPosition(0.55);

    runtime.reset();
    while (runtime.seconds() < 0.2) {
    }

    drive.followTrajectorySync(trajectory8);

//    drive.turnSync(turn1);
//    drive.followTrajectorySync(trajectory2);
  }
}
