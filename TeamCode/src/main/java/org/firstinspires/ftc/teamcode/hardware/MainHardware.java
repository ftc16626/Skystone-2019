package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MainHardware {

  public MecanumDrive drive;
  public RadicalIMU imu;
  public Intake intake;

  private String[] motorIds = new String[]{
      "motorFrontLeft", "motorFrontRight",
      "motorBackLeft", "motorBackRight"
  };

  private String[] intakeMotorIds = new String[] { "intakeLeft", "intakeRight" };

  public MainHardware(HardwareMap hwMap) {
    drive = new MecanumDrive(hwMap, motorIds[0], motorIds[1], motorIds[2], motorIds[3], true);
    imu = new RadicalIMU(hwMap.get(BNO055IMU.class, "imu"));
    intake = new Intake(hwMap, intakeMotorIds[0], intakeMotorIds[1]);
  }

  public void init() {
    drive.resetEncoders();
  }

  public void update() {
    drive.update();
    imu.update();
  }
}
