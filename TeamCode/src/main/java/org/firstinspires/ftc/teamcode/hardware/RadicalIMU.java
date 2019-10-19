package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.BNO055IMU.Parameters;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import java.util.Locale;

public class RadicalIMU {

  public BNO055IMU imu;
  private BNO055IMU.Parameters parameters;

  private Orientation lastOrientation;
  private double lastHeading;
  private double globalHeading;

  public RadicalIMU(BNO055IMU imu) {
    parameters = new Parameters();
    parameters.mode = BNO055IMU.SensorMode.IMU;
    parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
    parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
//    parameters.loggingEnabled = true;
//    parameters.loggingTag = "IMU";

    lastOrientation = new Orientation();

    this.imu = imu;
    imu.initialize(parameters);
  }

  public void update() {
    double deltaAngle = getHeading() - lastHeading;

    if(deltaAngle < -180)
      deltaAngle += 360;
    else if (deltaAngle > 180)
      deltaAngle -= 360;

    globalHeading += deltaAngle;

    lastHeading = getHeading();
  }

  public void resetAngle() {
    lastOrientation = getAngles();

    globalHeading = 0;
  }

  public double getGlobalHeading() {
    return globalHeading;
  }

  public Orientation getAngles() {
    return imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
  }

  public boolean isCalibrated() {
    return imu.isAccelerometerCalibrated() && imu.isGyroCalibrated() && imu
        .isMagnetometerCalibrated() && imu.isSystemCalibrated();
  }

  public String getSystemStatus() {
    return imu.getSystemStatus().toString();
  }

  public String getCalibrationStatus() {
    return imu.getCalibrationStatus().toString();
  }

  public float getHeading() {
    return getAngles().firstAngle;
  }

  public String getHeadingString() {
    return formatAngleString(getAngles().angleUnit, getAngles().firstAngle);
  }

  public float getRoll() {
    return getAngles().secondAngle;
  }

  public String getRollString() {
    return formatAngleString(getAngles().angleUnit, getAngles().firstAngle);
  }

  public float getPitch() {
    return getAngles().thirdAngle;
  }

  public String getPitchString() {
    return formatAngleString(getAngles().angleUnit, getAngles().thirdAngle);
  }

  public Acceleration getGravity() {
    return imu.getGravity();
  }

  public String formatAngleString(AngleUnit angleUnit, double angle) {
    return formatDegreesString(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
  }

  public String formatDegreesString(double degrees) {
    return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
  }
}
