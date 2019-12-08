package org.firstinspires.ftc.teamcode.tuning;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class MaxVelocityTest extends LinearOpMode {
  DcMotorEx motor;
  double currentVelocity;
  double maxVelocity = 0.0;

  @Override
  public void runOpMode() {
    motor = hardwareMap.get(DcMotorEx.class, "motorFrontLeft");
    waitForStart();

    motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
    motor.setPower(1);

    while (opModeIsActive()) {
      currentVelocity = motor.getVelocity();

      if (currentVelocity > maxVelocity) {
        maxVelocity = currentVelocity;
      }

      telemetry.addData("current velocity", currentVelocity);
      telemetry.addData("maximum velocity", maxVelocity);
      telemetry.update();
    }
  }
}
