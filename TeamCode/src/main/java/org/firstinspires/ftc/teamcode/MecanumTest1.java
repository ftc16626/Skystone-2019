package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

// Left joystick is used to translate the bot
// Right joystick controls the rotation of the bot
@TeleOp(name = "Mecanum Test 1", group = "Mecanum")
public class MecanumTest1 extends OpMode {

  private MainHardware robot;

  @Override
  public void init() {
    robot = new MainHardware(hardwareMap);

    telemetry.addData("Status", "Initialized");
  }

  @Override
  public void loop() {
    double magnitude = Math.min(Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y), 1);
    double robotAngle = Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x) - Math.PI / 4;
    double rightX = gamepad1.right_stick_x;

//    final double motorPowerFrontLeft = magnitude * Math.cos(robotAngle) + rightX;
//    final double motorPowerFrontRight = magnitude * Math.sin(robotAngle) - rightX;
//    final double motorPowerBackLeft = magnitude * Math.sin(robotAngle) + rightX;
//    final double motorPowerBackRight = magnitude * Math.cos(robotAngle) - rightX;

    // normalize motor powers so max is 1
    double motorPowerFrontLeft = Math.cos(robotAngle);
    double motorPowerFrontRight = Math.sin(robotAngle);
    double motorPowerBackLeft = Math.sin(robotAngle);
    double motorPowerBackRight = Math.cos(robotAngle);

    double maxValue = Math.abs(Math.max(motorPowerFrontLeft,
        Math.max(motorPowerFrontRight, Math.max(motorPowerBackLeft, motorPowerBackRight))));

    motorPowerFrontLeft /= maxValue;
    motorPowerFrontRight /= maxValue;
    motorPowerBackLeft /= maxValue;
    motorPowerBackRight /= maxValue;

    motorPowerFrontLeft = motorPowerFrontLeft * magnitude + rightX;
    motorPowerFrontRight = motorPowerFrontRight * magnitude - rightX;
    motorPowerBackLeft = motorPowerBackLeft * magnitude + rightX;
    motorPowerBackRight = motorPowerBackRight * magnitude - rightX;

    telemetry.addData("mag", magnitude);
    telemetry.addData("angle", robotAngle);
    telemetry.addData("rightX", rightX);
    telemetry.addData("front left", motorPowerFrontLeft);
    telemetry.addData("front right", motorPowerFrontRight);
    telemetry.addData("back left", motorPowerBackLeft);
    telemetry.addData("back right", motorPowerBackRight);

    robot.drive.motorFrontLeft.setPower(motorPowerFrontLeft);
    robot.drive.motorFrontRight.setPower(motorPowerFrontRight);
    robot.drive.motorBackLeft.setPower(motorPowerBackLeft);
    robot.drive.motorBackRight.setPower(motorPowerBackRight);
  }
}
