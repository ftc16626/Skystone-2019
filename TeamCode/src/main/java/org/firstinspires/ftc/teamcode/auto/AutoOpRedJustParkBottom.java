package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;

@Autonomous(name="Auto - Just Park Red,Bot", group="justpark")
public class AutoOpRedJustParkBottom extends LinearOpMode {
  MainHardware robot;

  ElapsedTime runtime = new ElapsedTime();

  @Override
  public void runOpMode() throws InterruptedException {
    robot = new MainHardware(hardwareMap);

    waitForStart();

    if(isStopRequested()) return;

    robot.init();

    robot.lowerBackLeftServo();

    runtime.reset();
    while(runtime.seconds() < 1) {

    }
  }
}