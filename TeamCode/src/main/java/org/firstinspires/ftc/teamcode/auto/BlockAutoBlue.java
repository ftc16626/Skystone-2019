package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

@Config
@Autonomous(name="Auto - Block Auto Blue", group = "testing")
public class BlockAutoBlue extends LinearOpMode {

  public static double step1 = 650;
  public static double step2 = 150;
  public static double step3 = 1570;
  public static double step4 = step3 + 190;
  public static double step5 = step1 - 160;


  //  public static double turn1 = Math.toRadians(-90);
  MainHardware robot;

  ElapsedTime runtime = new ElapsedTime();

  @Override
  public void runOpMode() {
    robot = new MainHardware(hardwareMap);
    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    Trajectory trajectory1 = drive.trajectoryBuilder()
        .back(step1).build();
    Trajectory trajectory2 = drive.trajectoryBuilder().forward(step2).strafeRight(step3).build();
    Trajectory trajectory3 = drive.trajectoryBuilder().strafeLeft(step4).back(step5).build();

    robot.backRightServo.setPosition(1);
    robot.swingyServo.setPosition(1);

    double startingAngle = drive.getRawExternalHeading();

    waitForStart();

    if (isStopRequested()) {
      return;
    }

    robot.init();
    robot.swingyServo.setPosition(1);

    drive.followTrajectorySync(trajectory1);

    robot.backRightServo.setPosition(0);

    runtime.reset();
    while (runtime.seconds() < 0.8) {
      ;
    }

    drive.followTrajectorySync(trajectory2);

    robot.backRightServo.setPosition(1);
    telemetry.addData("Heading", drive.getRawExternalHeading());

    drive.turnSync(drive.getRawExternalHeading() - startingAngle);
    drive.followTrajectorySync(trajectory3);

    robot.backRightServo.setPosition(0);

    runtime.reset();
    while (runtime.seconds() < 0.8) {
      ;
    }

//    drive.turnSync(turn1);
//    drive.followTrajectorySync(trajectory2);
  }
}
