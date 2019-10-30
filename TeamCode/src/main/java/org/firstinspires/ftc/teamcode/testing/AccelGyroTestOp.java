package org.firstinspires.ftc.teamcode.testing;

import com.ftc16626.missioncontrol.MissionControl;
import com.ftc16626.missioncontrol.util.LogModel;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.Date;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.hardware.RadicalIMU;

@TeleOp(name="AccelGyro Test Op", group = "Testing")
public class AccelGyroTestOp extends OpMode {

  private RadicalIMU radicalIMU;

  private MissionControl missionControl;

  @Override
  public void init() {
    missionControl = ((FtcRobotControllerActivity) hardwareMap.appContext).missionControl;

    radicalIMU = new RadicalIMU(hardwareMap.get(BNO055IMU.class, "imu"));

    telemetry.addData("IMU Status", !radicalIMU.isCalibrated() ? "Calibrating..." : "Calibrated");

    missionControl.sendInitPacket(new String[] {"status"});
  }

  @Override
  public void init_loop() {
    telemetry.addData("IMU Calibration", radicalIMU.getCalibrationStatus());
    telemetry.addData("IMU Accel Calibrated", !radicalIMU.imu.isAccelerometerCalibrated() ? "Calibrating..." : "Calibrated");
    telemetry.addData("IMU Gyro Calibrated", !radicalIMU.imu.isGyroCalibrated() ? "Calibrating..." : "Calibrated");
    telemetry.addData("IMU Magn Calibrated", !radicalIMU.imu.isMagnetometerCalibrated() ? "Calibrating..." : "Calibrated");
    telemetry.addData("IMU System Calibrated", !radicalIMU.imu.isSystemCalibrated() ? "Calibrating..." : "Calibrated");

    missionControl.broadcast(new LogModel("Calibrating"
        + "", "status", new Date()));  }

  @Override
  public void loop() {
    radicalIMU.update();
    sendFullTelemetry();
  }

  private void sendFullTelemetry() {
    telemetry.addData("Global Heading", radicalIMU.getGlobalHeading());
    telemetry.addData("Status", radicalIMU.getSystemStatus());
    telemetry.addData("Calib", radicalIMU.getCalibrationStatus());
    telemetry.addData("Heading", radicalIMU.getHeadingString());
    telemetry.addData("Roll", radicalIMU.getRollString());
    telemetry.addData("Pitch", radicalIMU.getPitchString());
    telemetry.addData("Gravity", radicalIMU.getGravity().toString());
    telemetry.update();
  }
}
