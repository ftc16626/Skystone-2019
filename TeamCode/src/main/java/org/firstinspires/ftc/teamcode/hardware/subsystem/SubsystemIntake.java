package org.firstinspires.ftc.teamcode.hardware.subsystem;

import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.hardware.util.DcMotorCached;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.openftc.revextensions2.ExpansionHubMotor;

public class SubsystemIntake extends HardwareSubsystem {

  private final DcMotorCached motorLeft, motorRight;
  private final String[] motorIds = new String[]{"motorIntakeLeftAndEncoderLeft",
      "motorIntakeRightAndEncoderRight"};

  private final double BASE_SPEED = 0.75;
  private final double SLOW_SPEED = 0.3;

  private double currentSpeed = BASE_SPEED;

  private boolean isReversed = false;
  private boolean isMotorOn = false;

  public SubsystemIntake(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);

    motorLeft = new DcMotorCached(robot.hwMap.get(ExpansionHubMotor.class, motorIds[0]), 0.05);
    motorRight = new DcMotorCached(robot.hwMap.get(ExpansionHubMotor.class, motorIds[1]), 0.05);

//    motorRight.getMotor().setDirection(Direction.REVERSE);
  }

  public void setMotorOn(boolean on) {
    this.isMotorOn = on;
    setPower();
  }

  public void setReversed(boolean reversed) {
    isReversed = reversed;
  }

  public void setSpeed(double speed) {
    currentSpeed = speed;
    setPower();
  }

  public void setPower() {
    if (this.isMotorOn) {
      motorLeft.setPower(currentSpeed * (isReversed ? -1 : 1));
      motorRight.setPower(currentSpeed * (isReversed ? -1 : 1));
    } else {
      motorLeft.setPower(0);
      motorRight.setPower(0);
    }
  }

  public void setDirect(float leftValue, float rightValue) {
    motorLeft.setPower(leftValue);
    motorRight.setPower(rightValue);
  }
}
