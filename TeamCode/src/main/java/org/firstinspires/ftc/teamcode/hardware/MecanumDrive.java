package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;
import java.util.List;

public class MecanumDrive {

  public DcMotor motorFrontLeft = null;
  public DcMotor motorFrontRight = null;
  public DcMotor motorBackLeft = null;
  public DcMotor motorBackRight = null;

  public List<DcMotor> motorList;

  public MecanumDrive(HardwareMap hwMap, String motorFrontLeftId, String motorFrontRightId,
      String motorBackLeftId, String motorBackRightId, boolean runWithEncoders) {
    motorFrontLeft = hwMap.get(DcMotor.class, motorFrontLeftId);
    motorFrontRight = hwMap.get(DcMotor.class, motorFrontRightId);
    motorBackLeft = hwMap.get(DcMotor.class, motorBackLeftId);
    motorBackRight = hwMap.get(DcMotor.class, motorBackRightId);

    motorFrontLeft.setDirection(Direction.REVERSE);
    motorBackLeft.setDirection(Direction.REVERSE);

    motorList.add(motorFrontLeft);
    motorList.add(motorFrontRight);
    motorList.add(motorBackLeft);
    motorList.add(motorBackRight);

    stopMotors();

    if (runWithEncoders) {
      for (DcMotor motor : motorList) {
        motor.setMode(RunMode.RUN_USING_ENCODER);
      }
    } else {
      for (DcMotor motor : motorList) {
        motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
      }
    }
  }

  public void stopMotors() {
    for (DcMotor motor : motorList) {
      motor.setPower(0);
    }
  }
}
