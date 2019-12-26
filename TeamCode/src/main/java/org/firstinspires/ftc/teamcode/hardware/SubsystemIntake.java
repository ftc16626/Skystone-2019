package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;

public class SubsystemIntake extends HardwareSubsystem {

  private final DcMotor motorLeft, motorRight;
  private final String[] motorIds = new String[] {"motorIntakeLeft", "motorIntakeRight"};

  private final double BASE_SPEED = 0.75;
  private final double SLOW_SPEED = 0.3;

  private double currentSpeed = BASE_SPEED;

  private boolean isReversed = false;
  private boolean isMotorOn = false;

  public SubsystemIntake(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);

    motorLeft = robot.hwMap.dcMotor.get(motorIds[0]);
    motorRight = robot.hwMap.dcMotor.get(motorIds[1]);
  }

  public void setMotorOn(boolean on) {
    this.isMotorOn = on;
    setPower();
  }

  public void setReversed(boolean reversed) {
    isReversed = reversed;
    setPower();
  }

  public void setSpeed(double speed) {
    currentSpeed = speed;
    setPower();
  }

  public void setPower() {
    if(this.isMotorOn) {
      motorLeft.setPower(currentSpeed * (isReversed ? -1 : 1));
      motorRight.setPower(currentSpeed * (isReversed ? -1 : 1));
    } else {
      motorLeft.setPower(0);
      motorLeft.setPower(0);
    }
  }
}
