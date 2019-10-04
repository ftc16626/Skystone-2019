package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

// Left joystick is used to translate the bot
// Right joystick controls the rotation of the bot
// But this one doesn't use trig.
@TeleOp(name = "Mecanum Test 2", group = "Mecanum")
public class MecanumTest2 extends OpMode {

  private MainHardware robot;

  @Override
  public void init() {
    robot = new MainHardware(hardwareMap);

    telemetry.addData("Status", "Initialized");
  }

  @Override
  public void loop() {
    int threshold = 20;

    double motorPowerFrontLeft = (-gamepad1.left_stick_y - gamepad1.left_stick_x) / 2;
    double motorPowerFrontRight = (gamepad1.left_stick_y - gamepad1.left_stick_x) / 2;
    double motorPowerBackLeft = (gamepad1.left_stick_y - gamepad1.left_stick_x) / 2;
    double motorPowerBackRight = (-gamepad1.left_stick_y - gamepad1.left_stick_x) / 2;

    if (Math.abs(gamepad1.right_stick_x) > threshold) {
      motorPowerFrontLeft = (-gamepad1.right_stick_x) / 2;
      motorPowerFrontRight = (-gamepad1.right_stick_x) / 2;
      motorPowerBackLeft = (gamepad1.right_stick_x) / 2;
      motorPowerBackRight = (gamepad1.right_stick_x) / 2;
    }

    robot.drive.motorFrontLeft.setPower(motorPowerFrontLeft);
    robot.drive.motorFrontRight.setPower(motorPowerFrontRight);
    robot.drive.motorBackLeft.setPower(motorPowerBackLeft);
    robot.drive.motorBackRight.setPower(motorPowerBackRight);
  }
}
