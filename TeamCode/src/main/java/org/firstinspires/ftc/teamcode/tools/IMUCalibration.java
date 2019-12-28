package org.firstinspires.ftc.teamcode.tools;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMU.AccelUnit;
import com.qualcomm.hardware.bosch.BNO055IMU.AngleUnit;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ReadWriteFile;
import java.io.File;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

@TeleOp(name = "IMU Calibration", group = "Tools")
public class IMUCalibration extends OpMode {

  private final BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
  private BNO055IMU imu;

  private final String fileName = "RadicalIMUCalibration.json";

  @Override
  public void init() {
    imu = hardwareMap.get(BNO055IMU.class, "imu");
    parameters.angleUnit = AngleUnit.DEGREES;
    parameters.accelUnit = AccelUnit.METERS_PERSEC_PERSEC;
    parameters.calibrationDataFile = fileName;

    imu.initialize(parameters);

    telemetry.addData("", "Press the start button to begin calibration");
  }

  @Override
  public void loop() {
    if(!imu.isGyroCalibrated()) {
      telemetry.addData("Do this", "Leave the device still\n"
          + "n");
    } else if(!imu.isAccelerometerCalibrated()) {
      telemetry.addData("Do this", "Move the sensor in various positions. Start flat, rotating 45 degrees\n");
    } else if(!imu.isMagnetometerCalibrated()) {
      telemetry.addData("Do this", "Move device in figure 8 pattern\n");
    } else {
      telemetry.addData("Do this", "DONE\n");
      finish();
      requestOpModeStop();
    }

    telemetry.addData("Total", imu.getCalibrationStatus());
    telemetry.addData("Accel Calibrated", imu.isAccelerometerCalibrated() ? "✔" : "");
    telemetry.addData("Gyro Calibrated", imu.isGyroCalibrated() ? "✔" : "");
    telemetry.addData("Mag Calibrated", imu.isMagnetometerCalibrated() ? "✔" : "");
    telemetry.addData("Sys Calibrated", imu.isSystemCalibrated() ? "✔" : "");
  }

  private void finish() {
    BNO055IMU.CalibrationData calibrationData = imu.readCalibrationData();
    File file = AppUtil.getInstance().getSettingsFile(fileName);
    ReadWriteFile.writeFile(file, calibrationData.serialize());
    telemetry.log().clear();
    telemetry.log().add("Saved to '%s'", fileName);
    telemetry.log().add(calibrationData.serialize());
  }
}
