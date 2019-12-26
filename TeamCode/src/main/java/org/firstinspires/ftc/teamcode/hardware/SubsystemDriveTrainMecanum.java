package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import java.util.ArrayList;
import java.util.List;
import org.firstinspires.ftc.teamcode.hardware.util.DcMotorCached;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;

public class SubsystemDriveTrainMecanum extends HardwareSubsystem {

  private final DcMotorCached motorFrontLeft, motorFrontRight, motorBackLeft, motorBackRight;
  private final String[] motorIds = new String[]{
      "motorFrontLeft", "motorFrontRight",
      "motorBackLeft", "motorBackRight"
  };
  private final List<DcMotorCached> motorList = new ArrayList<>();

  private double angle = 0;
  private double power = 0;
  private double turn = 0;

  private boolean dirty = false;

  public SubsystemDriveTrainMecanum(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);

    motorFrontLeft = new DcMotorCached(robot.hwMap.dcMotor.get(motorIds[0]));
    motorFrontRight = new DcMotorCached(robot.hwMap.dcMotor.get(motorIds[1]));
    motorBackLeft = new DcMotorCached(robot.hwMap.dcMotor.get(motorIds[2]));
    motorBackRight = new DcMotorCached(robot.hwMap.dcMotor.get(motorIds[3]));

    motorFrontLeft.getMotor().setDirection(Direction.REVERSE);
    motorBackLeft.getMotor().setDirection(Direction.REVERSE);

    motorList.add(motorFrontLeft);
    motorList.add(motorFrontRight);
    motorList.add(motorBackLeft);
    motorList.add(motorBackRight);

    stopMotors();

    for (DcMotorCached motor : motorList) {
      motor.getMotor().setMode(RunMode.RUN_USING_ENCODER);
    }

    for (DcMotorCached motor : motorList) {
      motor.getMotor().setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
    }
  }

  public void stopMotors() {
    for (DcMotorCached motor : motorList) {
      motor.setPower(0);
    }
  }

  @Override
  public void update() {
    if(dirty) {
      refreshMotors();
    }
  }

  @Override
  public void onStop() {
    stopMotors();
  }

  public void setAngle(double angle) {
    this.angle = angle;
    this.dirty = true;
  }

  public void setPower(double power) {
    this.power = power;
    this.dirty = true;
  }

  public void setTurn(double turn) {
    this.turn = turn;
    this.dirty = true;
  }

  public void refreshMotors() {
    double motorPowerFrontLeft = Math.cos(angle);
    double motorPowerFrontRight = Math.sin(angle);
    double motorPowerBackLeft = Math.sin(angle);
    double motorPowerBackRight = Math.cos(angle);

    // Normalize values
    double maxValue = Math.abs(Math.max(motorPowerFrontLeft,
        Math.max(motorPowerFrontRight, Math.max(motorPowerBackLeft, motorPowerBackRight))));

    motorPowerFrontLeft /= maxValue;
    motorPowerFrontRight /= maxValue;
    motorPowerBackLeft /= maxValue;
    motorPowerBackRight /= maxValue;

    motorPowerFrontLeft = motorPowerFrontLeft * power + turn;
    motorPowerFrontRight = motorPowerFrontRight * power - turn;
    motorPowerBackLeft = motorPowerBackLeft * power + turn;
    motorPowerBackRight = motorPowerBackRight * power - turn;

    motorFrontLeft.setPower(motorPowerFrontLeft);
    motorFrontRight.setPower(motorPowerFrontRight);
    motorBackLeft.setPower(motorPowerBackLeft);
    motorBackRight.setPower(motorPowerBackRight);

    this.dirty = false;
  }
}
