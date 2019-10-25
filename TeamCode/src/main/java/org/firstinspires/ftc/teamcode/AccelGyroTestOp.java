package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.hardware.RadicalIMU;

@TeleOp(name="AccelGyro Test Op", group = "Testing")
public class AccelGyroTestOp extends OpMode {

  private RadicalIMU radicalIMU;

  @Override
  public void init() {
    radicalIMU = new RadicalIMU(hardwareMap.get(BNO055IMU.class, "imu"));
    telemetry.addData("IMU Status", !radicalIMU.isCalibrated() ? "Calibrating..." : "Calibrated");
  }

  @Override
  public void init_loop() {
    telemetry.addData("IMU Status", !radicalIMU.isCalibrated() ? "Calibrating..." : "Calibrated");
  }

  @Override
  public void loop() {
    radicalIMU.update();
    sendFullTelemetry();
  }

  private void sendFullTelemetry() {
    telemetry.addData("Status", radicalIMU.getSystemStatus());
    telemetry.addData("Calib", radicalIMU.getCalibrationStatus());
    telemetry.addData("Heading", radicalIMU.getHeadingString());
    telemetry.addData("Roll", radicalIMU.getRollString());
    telemetry.addData("Pitch", radicalIMU.getPitchString());
    telemetry.addData("Gravity", radicalIMU.getGravity().toString());
    telemetry.update();
  }
}
