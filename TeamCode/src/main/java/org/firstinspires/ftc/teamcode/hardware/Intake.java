package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
  private DcMotor motorLeft;
  private DcMotor motorRight;

  private float power = -0.75f;
  private boolean on = false;

  public Intake(HardwareMap hwMap, String motorLeftId, String motorRightId) {
    motorLeft = hwMap.get(DcMotor.class, motorLeftId);
    motorRight = hwMap.get(DcMotor.class, motorRightId);

    motorRight.setDirection(Direction.REVERSE);
  }

  public void setPower(float power) {
//    this.power = power;
    motorLeft.setPower(power);
    motorRight.setPower(power);
  }

  public void toggle() {
    on = !on;

    toggle(on);
  }

  public void toggle(boolean on) {
    this.on = on;

    if(this.on) {
      motorLeft.setPower(power);
      motorRight.setPower(power);
    } else {
      motorLeft.setPower(0);
      motorRight.setPower(0);
    }
  }

  public void reverse() {
    power = power * -1;

    if(this.on) {
      motorLeft.setPower(power);
      motorRight.setPower(power);
    }
  }
}
