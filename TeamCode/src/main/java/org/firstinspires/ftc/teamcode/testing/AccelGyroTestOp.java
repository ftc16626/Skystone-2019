package org.firstinspires.ftc.teamcode.testing;

import com.ftc16626.missioncontrol.MissionControl;
import com.ftc16626.missioncontrol.util.LogModel;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.Date;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.teamcode.hardware.RadicalIMU;

@TeleOp(name="AccelGyro Test Op", group = "Testing")
@Disabled
public class AccelGyroTestOp extends OpMode {

  private RadicalIMU radicalIMU;

  private MissionControl missionControl;

  @Override
  public void init() {
    missionControl = ((FtcRobotControllerActivity) hardwareMap.appContext).missionControl;

    radicalIMU = new RadicalIMU(hardwareMap.get(BNO055IMU.class, "imu"), false);

    telemetry.addData("IMU Status", !radicalIMU.isCalibrated() ? "Calibrating..." : "Calibrated");

    missionControl.sendInitPacket(new String[] {"status"});

    radicalIMU.calibrateSensorBias(10000);
  }

  @Override
  public void init_loop() {
    telemetry.addData("IMU Sensor Bias Calibrating", radicalIMU.isCalibrating());
    telemetry.addData("IMU Calibration", radicalIMU.getCalibrationStatus());
    telemetry.addData("IMU Accel Calibrated", !radicalIMU.getImu().isAccelerometerCalibrated() ? "Calibrating..." : "Calibrated");
    telemetry.addData("IMU Gyro Calibrated", !radicalIMU.getImu().isGyroCalibrated() ? "Calibrating..." : "Calibrated");
    telemetry.addData("IMU Magn Calibrated", !radicalIMU.getImu().isMagnetometerCalibrated() ? "Calibrating..." : "Calibrated");
    telemetry.addData("IMU System Calibrated", !radicalIMU.getImu().isSystemCalibrated() ? "Calibrating..." : "Calibrated");

    missionControl.broadcast(new LogModel("Calibrating"
        + "", "status", new Date()));

    radicalIMU.update();
  }

  @Override
  public void loop() {
    radicalIMU.update();
    sendFullTelemetry();
  }

  private void sendFullTelemetry() {
//    telemetry.addData("Global Heading", radicalIMU.getGlobalHeading());
//    telemetry.addData("Status", radicalIMU.getSystemStatus());
//    telemetry.addData("Calib", radicalIMU.getCalibrationStatus());
//    telemetry.addData("Heading", radicalIMU.getHeadingString());
//    telemetry.addData("Roll", radicalIMU.getRollString());
//    telemetry.addData("Pitch", radicalIMU.getPitchString());
//    telemetry.addData("Gravity", radicalIMU.getGravity().toString());

//    telemetry.addData("IMU Accel", radicalIMU.getAcceleration().toString());
//    telemetry.addData("IMU Accel 2", radicalIMU.getImu().getOverallAcceleration().toString());

      telemetry.addData("Accel X", radicalIMU.getAcceleration().xAccel);
      telemetry.addData("Accel Y", radicalIMU.getAcceleration().yAccel);
      telemetry.addData("Accel Z", radicalIMU.getAcceleration().zAccel);

      telemetry.addLine("");
      telemetry.addData("Accel X Max", radicalIMU.getBiasAccelXMax());
      telemetry.addData("Accel Y Max", radicalIMU.getBiasAccelYMax());
      telemetry.addData("Accel Z Max", radicalIMU.getBiasAccelZMax());

      telemetry.addLine("");
      telemetry.addData("Accel X Avg", radicalIMU.getBiasAccelXAvg());
      telemetry.addData("Accel Y Avg", radicalIMU.getBiasAccelYAvg());
      telemetry.addData("Accel Z Avg", radicalIMU.getBiasAccelZAvg());

      telemetry.addLine("");
      telemetry.addData("Vel X", radicalIMU.getVelocity().getX());
      telemetry.addData("Vel Y", radicalIMU.getVelocity().getY());
      telemetry.addData("Vel Z", radicalIMU.getVelocity().getZ());

      telemetry.addLine("");
      telemetry.addData("Pos X", radicalIMU.getPosition().getX());
      telemetry.addData("Pos Y", radicalIMU.getPosition().getY());
      telemetry.addData("Pos Z", radicalIMU.getPosition().getZ());

    telemetry.update();
  }
}
