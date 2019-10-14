package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

// Left joystick is used to translate the bot
// Right joystick controls the rotation of the bot
@TeleOp(name="Mecanum Test 4", group="Mecanum")
public class MecanumTest4 extends OpMode {

  private MainHardware robot;

  @Override
  public void init() {
    robot = new MainHardware(hardwareMap);

    telemetry.addData("Status", "Initialized");
  }

  @Override
  public void loop() {
    double drive = gamepad1.left_stick_y;
    double strafe = gamepad1.left_stick_x;
    double twist = gamepad1.right_stick_x;

    double[] speeds = {
        (drive + strafe + twist),
        (drive - strafe - twist),
        (drive - strafe + twist),
        (drive + strafe - twist)
    };

    // Because we are adding vectors and motors only take values between
    // [-1,1] we may need to normalize them.

    // Loop through all values in the speeds[] array and find the greatest
    // magnitude.  Not the greatest velocity.
    double max = Math.abs(speeds[0]);
    for(int i = 0; i < speeds.length; i++) {
      if ( max < Math.abs(speeds[i]) ) max = Math.abs(speeds[i]);
    }

    // If and only if the maximum is outside of the range we want it to be,
    // normalize all the other speeds based on the given speed value.
    if (max > 1) {
      for (int i = 0; i < speeds.length; i++) speeds[i] /= max;
    }

    telemetry.addData("drive", drive);
    telemetry.addData("strafe", strafe);
    telemetry.addData("twist", twist);
    telemetry.addData("front left", speeds[0]);
    telemetry.addData("front right", speeds[1]);
    telemetry.addData("back left", speeds[2]);
    telemetry.addData("back right", speeds[3]);

    robot.drive.motorFrontLeft.setPower(speeds[0]);
    robot.drive.motorFrontRight.setPower(speeds[1]);
    robot.drive.motorBackLeft.setPower(speeds[2]);
    robot.drive.motorBackRight.setPower(speeds[3]);
  }
}
