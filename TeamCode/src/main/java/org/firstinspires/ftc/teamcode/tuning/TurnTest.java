package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveBaseMecanumOld;

@Config
@Autonomous(name="Tuning - Turn Test", group = "tuning")
public class TurnTest extends LinearOpMode {
  public static double ANGLE = 180; // deg

  @Override
  public void runOpMode() throws InterruptedException {
    DriveBaseMecanumOld drive = new DriveBaseMecanumOld(hardwareMap);

    waitForStart();

    if (isStopRequested()) return;

    drive.turnSync(Math.toRadians(ANGLE));
  }
}