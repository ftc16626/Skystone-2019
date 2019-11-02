package org.firstinspires.ftc.teamcode.tools;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ReadWriteFile;
import java.io.File;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import org.firstinspires.ftc.teamcode.hardware.RadicalIMU;

@TeleOp(name = "IMU Calibration", group = "Tools")
public class IMUCalibration extends OpMode {

  private RadicalIMU radicalIMU;
  private final String fileName = "RadicalIMUCalibration.json";

  @Override
  public void init() {
    radicalIMU = new RadicalIMU(hardwareMap.get(BNO055IMU.class, "imu"), false);
    telemetry.addData("", "Press the start button to begin calibration");
  }

  @Override
  public void loop() {
    if(!radicalIMU.isGyroCalibrated()) {
      telemetry.addData("Do this", "Leave the device still\n"
          + "n");
    } else if(!radicalIMU.isAccelCalibrated()) {
      telemetry.addData("Do this", "Move the sensor in various positions. Start flat, rotating 45 degrees\n");
    } else if(!radicalIMU.isMagCalibrated()) {
      telemetry.addData("Do this", "Move device in figure 8 pattern\n");
    } else {
      telemetry.addData("Do this", "DONE\n");
      finish();
      requestOpModeStop();
    }

    telemetry.addData("Total", radicalIMU.getCalibrationStatus());
    telemetry.addData("Accel Calibrated", radicalIMU.isAccelCalibrated() ? "✔" : "");
    telemetry.addData("Gyro Calibrated", radicalIMU.isGyroCalibrated() ? "✔" : "");
    telemetry.addData("Mag Calibrated", radicalIMU.isMagCalibrated() ? "✔" : "");
    telemetry.addData("Sys Calibrated", radicalIMU.isSysCalibrated() ? "✔" : "");

    radicalIMU.update();
  }

  private void finish() {
    BNO055IMU.CalibrationData calibrationData = radicalIMU.getImu().readCalibrationData();
    File file = AppUtil.getInstance().getSettingsFile(fileName);
    ReadWriteFile.writeFile(file, calibrationData.serialize());
    telemetry.log().clear();
    telemetry.log().add("Saved to '%s'", fileName);
    telemetry.log().add(calibrationData.serialize());
  }
}
