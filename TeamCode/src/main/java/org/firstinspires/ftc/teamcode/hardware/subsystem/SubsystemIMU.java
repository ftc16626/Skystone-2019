package org.firstinspires.ftc.teamcode.hardware.subsystem;

import com.qualcomm.hardware.bosch.BNO055IMU;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;

public class SubsystemIMU extends HardwareSubsystem {

  private final String ID = "imu";

  private final BNO055IMU imu;

  private final BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

  private Orientation angles = new Orientation();
  private double lastHeading = 0.0;
  private double globalHeading = 0.0;

  public SubsystemIMU(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);

    parameters.mode = BNO055IMU.SensorMode.IMU;
    parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
    parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
    parameters.calibrationDataFile = "RadicalIMUCalibration.json";

    imu = robot.hwMap.get(BNO055IMU.class, ID);
  }

  @Override
  public void onInit() {
    imu.initialize(parameters);
  }

  @Override
  public void update() {
    angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

    double headingNow = getHeading();
    double deltaAngle = headingNow - lastHeading;

    if(deltaAngle < Math.toRadians(-180))
      deltaAngle += Math.toRadians(360.0);
    else if(deltaAngle > Math.toRadians(180))
      deltaAngle -= Math.toRadians(360.0);

    globalHeading += deltaAngle;
    lastHeading = headingNow;
  }

  @Override
  public void onStop() {
    imu.close();
  }

  public double getHeading() {
    return (double) angles.firstAngle;
  }

  public double getRoll() {
    return (double) angles.secondAngle;
  }

  public double getPitch() {
    return (double) angles.thirdAngle;
  }
}
