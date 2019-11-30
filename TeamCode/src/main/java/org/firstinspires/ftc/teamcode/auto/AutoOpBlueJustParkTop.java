package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

@Autonomous(name="Auto - Just Park Blue,Top", group="justpark")
public class AutoOpBlueJustParkTop extends LinearOpMode {
  MainHardware robot;

  ElapsedTime runtime = new ElapsedTime();

  // CHANGE THIS NUMBER
  final double DISTANCE = 760;

  @Override
  public void runOpMode() throws InterruptedException {
    robot = new MainHardware(hardwareMap);
    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    Trajectory trajectory1 = drive.trajectoryBuilder().strafeLeft(DISTANCE).build();

    waitForStart();

    if(isStopRequested()) return;

    robot.init();

    drive.followTrajectorySync(trajectory1);

    robot.lowerBackLeftServo();

    runtime.reset();
    while(runtime.seconds() < 1)  {

    }
  }
}