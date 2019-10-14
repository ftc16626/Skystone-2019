package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

@TeleOp(name = "Main TeleOp", group = "Mecanum")
public class MainTeleop extends OpMode {

  private MainHardware robot;

  @Override
  public void init() {
    robot = new MainHardware(hardwareMap);
    telemetry.addData("Status", "Initialized");
  }

  @Override
  public void loop() {
    double magnitude = Math.min(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1);
    double angle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
    double turn = gamepad1.right_stick_x;

    robot.drive.setAngle(angle);
    robot.drive.setPower(magnitude);
    robot.drive.setTurn(turn);

    robot.update();
  }
}
