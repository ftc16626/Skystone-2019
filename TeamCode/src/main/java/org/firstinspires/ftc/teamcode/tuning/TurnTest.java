package org.firstinspires.ftc.teamcode.tuning;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;

@Config
@Autonomous(name="Tuning - Turn Test", group = "tuning")
public class TurnTest extends LinearOpMode {
  public static double ANGLE = 90; // deg

  @Override
  public void runOpMode() throws InterruptedException {
    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    waitForStart();

    if (isStopRequested()) return;

    drive.turnSync(Math.toRadians(ANGLE));
  }
}