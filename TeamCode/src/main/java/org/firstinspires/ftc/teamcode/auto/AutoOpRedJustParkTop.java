package org.firstinspires.ftc.teamcode.auto;

import android.os.Trace;
import com.acmerobotics.roadrunner.drive.Drive;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

@Autonomous(name="Auto - Just Park Red,Top", group="justpark")
public class AutoOpRedJustParkTop extends LinearOpMode {
  MainHardware robot;

  ElapsedTime runtime = new ElapsedTime();

  final double DISTANCE = 760;

  @Override
  public void runOpMode() throws InterruptedException {
    robot = new MainHardware(hardwareMap);
    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    Trajectory trajectory1 = drive.trajectoryBuilder().strafeRight(DISTANCE).build();

    waitForStart();

    if(isStopRequested()) return;

    robot.init();

    drive.followTrajectorySync(trajectory1);

    robot.lowerBackLeftServo();

    runtime.reset();
    while(runtime.seconds() < 1) {

    }
  }
}