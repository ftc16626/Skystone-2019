package org.firstinspires.ftc.teamcode.hardware;

import com.ftc16626.missioncontrol.math.PIDController;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Lift {
  private PIDController controller;

  private final double kP = 0.25;
  private final double kI = 0;
  private final double kD = 2.5;
  private final double kG = 0;

  private double currentHeight = 0;
  private double targetHeight = 0;

  private double MAX_HEIGHT;
  private double MIN_HEIGHT;

  private DcMotor liftMotor;
  private Rev2mDistanceSensor liftSensor;

  public Lift(HardwareMap hwMap, String liftMotorId, String liftSensorId, double maxHeight, double minHeight) {
    liftMotor = hwMap.get(DcMotor.class, liftMotorId);
    liftSensor = (Rev2mDistanceSensor) hwMap.get(DistanceSensor.class, liftSensorId);

    liftMotor.setMode(RunMode.RUN_USING_ENCODER);
    liftMotor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

    controller = new PIDController(kP, kI, kD, kG);
    controller.setBounds(-1, 1);
  }

  public void update() {
    targetHeight = Range.clip(targetHeight, MIN_HEIGHT, MAX_HEIGHT);

    currentHeight = liftSensor.getDistance(DistanceUnit.CM);
    double power = -controller.update(targetHeight - currentHeight);

    liftMotor.setPower(currentHeight < MIN_HEIGHT && targetHeight < MIN_HEIGHT ? 0 : power);
  }

  public void setTargetHeight(double targetHeight) {
    this.targetHeight = targetHeight;
  }
}
