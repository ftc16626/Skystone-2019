package org.firstinspires.ftc.teamcode.auto.tempauto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Servo.Direction;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class RedFoundationAutoRed extends LinearOpMode {
  private MecanumDrive drive;
  private ElapsedTime runtime = new ElapsedTime();

  private final String[] motorIds = new String[]{
      "motorFrontLeft", "motorFrontRight",
      "motorBackLeft", "motorBackRight"
  };


  private Servo leftServo, rightServo;
  private final String[] servoIds = new String[] { "servoFoundationLeft", "servoFoundationRight" };

  private final double LEFT_MIN = 0.4;
  private final double LEFT_MAX = 0.985;

  private final double RIGHT_MIN = 0.1;
  private final double RIGHT_MAX = 0.9;


  @Override
  public void runOpMode() throws InterruptedException {

    drive = new MecanumDrive(hardwareMap, motorIds[0], motorIds[1], motorIds[2], motorIds[3]);

    leftServo = hardwareMap.get(Servo.class, servoIds[0]);
    rightServo = hardwareMap.get(Servo.class, servoIds[1]);

    leftServo.setDirection(Direction.REVERSE);

    leftServo.scaleRange(LEFT_MIN, LEFT_MAX);
    rightServo.scaleRange(RIGHT_MIN, RIGHT_MAX);

    telemetry.addData("Status", "Initialized");
    telemetry.update();

    waitForStart();
    runtime.reset();

    drive.setAngle(Math.toRadians(135));
    drive.setTurn(0);
    drive.setPower(0.75);
    while(opModeIsActive() && runtime.seconds() < 0.5) {
      drive.update();

      telemetry.addData("STEP", "1");
      telemetry.update();
    }

    runtime.reset();

    drive.setAngle(Math.toRadians(225));

    while(opModeIsActive() && runtime.seconds() < 1.50) {
      drive.update();
      telemetry.addData("STEP", "2");
      telemetry.update();

      if(runtime.seconds() > 1) {
        drop();
      }
    }

    drive.setPower(0);

    runtime.reset();
    while(opModeIsActive() && runtime.seconds() < 0.4) {

    }

    runtime.reset();

    drive.setPower(0.75);
    drive.setAngle(Math.toRadians(45));
    while(opModeIsActive() && runtime.seconds() < 2.1) {
      drive.update();
      telemetry.addData("STEP", "3");
      telemetry.update();
    }

//    robot.drive.setPower(0);
    runtime.reset();

    drive.setAngle(Math.toRadians(315));

    raise();

    while(opModeIsActive() && runtime.seconds() < 1.8) {
      drive.update();
      telemetry.addData("STEP", "4");
      telemetry.update();
    }

    runtime.reset();

    drive.setAngle(Math.toRadians(225));
    while(opModeIsActive() && runtime.seconds() < 0.5) {
      drive.update();
      telemetry.addData("STEP", "4.5");
      telemetry.update();
    }


    runtime.reset();

    drive.setAngle(Math.toRadians(180));
    while(opModeIsActive() && runtime.seconds() < 1) {
      drive.update();
      telemetry.addData("STEP", "5");
      telemetry.update();
    }



    runtime.reset();
    drive.setAngle(Math.toRadians(315));
    while(opModeIsActive() && runtime.seconds() < 1.4) {
      telemetry.addData("STEP", "6");
      telemetry.update();
    }


    drop();
    while(opModeIsActive() && runtime.seconds() < 0.5) {

    }

    drive.stopMotors();
  }

  void drop() {
    leftServo.setPosition(0);
    rightServo.setPosition(0);
  }

  void raise() {
    leftServo.setPosition(1);
    rightServo.setPosition(1);
  }
}
