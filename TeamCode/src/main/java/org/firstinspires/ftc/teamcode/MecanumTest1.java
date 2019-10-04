package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

// Left joystick is used to translate the bot
// Right joystick controls the rotation of the bot
@TeleOp(name="Mecanum Test 1", group="Mecanum")
public class MecanumTest1 extends OpMode {

  private MainHardware robot;

  @Override
  public void init() {
    robot = new MainHardware(hardwareMap);

    telemetry.addData("Status", "Initialized");
  }

  @Override
  public void loop() {
    double radius = Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y);
    double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
    double rightX = gamepad1.right_stick_x;

    final double motorPowerFrontLeft = radius * Math.cos(robotAngle) + rightX;
    final double motorPowerFrontRight = radius * Math.sin(robotAngle) - rightX;
    final double motorPowerBackLeft = radius * Math.sin(robotAngle) + rightX;
    final double motorPowerBackRight = radius * Math.cos(robotAngle) - rightX;

    robot.drive.motorFrontLeft.setPower(motorPowerFrontLeft);
    robot.drive.motorFrontRight.setPower(motorPowerFrontRight);
    robot.drive.motorBackLeft.setPower(motorPowerBackLeft);
    robot.drive.motorBackRight.setPower(motorPowerBackRight);
  }
}
