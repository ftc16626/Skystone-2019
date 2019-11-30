package org.firstinspires.ftc.teamcode.subsystem.system;

import android.util.Log;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.ftc16626.missioncontrol.math.PIDController;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadType;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.jetbrains.annotations.NotNull;

@Config
public class SubsystemPIDLift extends Subsystem implements GamepadListener {

  DriverInterface driverInterface;
  PIDController controller;

  public static double kP = 0.25;
  public static double kI = 0;
  public static double kD = 0.08;

  public static double kG = 0; // power against gravity

  private double currentHeight = 0;
  public static double targetHeight = 8;

  private FtcDashboard dashboard = FtcDashboard.getInstance();
  Telemetry telemetry = dashboard.getTelemetry();

  public SubsystemPIDLift(
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
    targetHeight = Range.clip(targetHeight - getOpMode().gamepad1.left_stick_y / 2, 1.5, 52);

    controller.setConstants(kP, kI, kD, currentHeight < 3 ? 0 : kG);

    currentHeight = getRobot().liftRange.getDistance(DistanceUnit.CM);
    double power = -controller.update(targetHeight - currentHeight);
//    double power = (targetHeight - currentHeight) / 10 * 0.8 * -1;

    getOpMode().telemetry.addData("Height", currentHeight);
    getOpMode().telemetry.addData("Power", power);

    getRobot().motorLift.setPower(currentHeight < 1.7 && targetHeight < 1.7 ? 0 : power);

    telemetry.addData("currentHeight", currentHeight);
    telemetry.addData("targetHeight", targetHeight);
    telemetry.addData("power", power);
    telemetry.addData("error", targetHeight - currentHeight);
    telemetry.update();
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if(eventType == GamepadEventType.BUTTON_PRESSED) {
      if(eventName == GamepadEventName.DPAD_UP) {
        targetHeight = Range.clip(targetHeight + 10, 1.5, 52);
      Log.i("TEST", "BUTTON PRESSED 1");
      } else if(eventName == GamepadEventName.DPAD_DOWN) {
        targetHeight = Range.clip(targetHeight - 10, 1.5, 52);
      Log.i("TEST", "BUTTON PRESSED 2");
      }

      Log.i("TEST", "BUTTON PRESSED");
    }
  }
}
