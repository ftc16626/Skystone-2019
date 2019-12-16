package org.firstinspires.ftc.teamcode.subsystem.system;

import com.ftc16626.missioncontrol.math.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.util.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadType;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.jetbrains.annotations.NotNull;

public class SubsystemDriverLift extends Subsystem implements GamepadListener {

  DriverInterface driverInterface;
  private PIDController controller;

  private final double kP = 0.3;
  private final double kI = 0;
  private final double kD = 2.5;

  private final double kG = 0; // power against gravity

  private double currentHeight = 0;
  private double targetHeight = 0;

  private final double MAX_HEIGHT = getRobot().LIFT_MAX_HEIGHT / 10;
  private final double MIN_HEIGHT = getRobot().LIFT_MIN_HEIGHT / 10;

  private final double LIFT_SPEED = 0.7; // gamepad y stick * LIFT_SPEED
  private final double DPAD_INCREMENT = 14;

  public SubsystemDriverLift(
      @NotNull MainHardware robot,
      @NotNull RadicalOpMode opMode,
      @NotNull DriverInterface driverInterface) {
    super(robot, opMode);

    controller = new PIDController(kP, kI, kD, kG);
    controller.setBounds(-1, 1);

    this.driverInterface = driverInterface;
  }

  @Override
  public void onInit() {
    getRobot().motorLift.setMode(RunMode.RUN_USING_ENCODER);
    getRobot().motorLift.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
  }

  @Override
  public void update() {
    targetHeight = Range.clip(targetHeight - driverInterface.aid.gamepad.left_stick_y * LIFT_SPEED, MIN_HEIGHT, MAX_HEIGHT);

//    controller.setConstants(kP, kI, kD, kG);

    currentHeight = getRobot().liftRange.getDistance(DistanceUnit.CM);
    double power = -controller.update(targetHeight - currentHeight);

    getRobot().motorLift.setPower(currentHeight < MIN_HEIGHT && targetHeight < MIN_HEIGHT ? 0 : power);

    getOpMode().telemetry.addData("Target Height", targetHeight);
    getOpMode().telemetry.addData("Current Height", currentHeight);
    getOpMode().telemetry.addData("Motor Power", power);
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if(eventType == GamepadEventType.BUTTON_PRESSED) {
      if(eventName == GamepadEventName.DPAD_UP) {
        targetHeight = Range.clip(targetHeight + DPAD_INCREMENT, MIN_HEIGHT, MAX_HEIGHT);
      } else if(eventName == GamepadEventName.DPAD_DOWN) {
        targetHeight = Range.clip(targetHeight - DPAD_INCREMENT, MIN_HEIGHT, MAX_HEIGHT);
      }
    }
  }
}
