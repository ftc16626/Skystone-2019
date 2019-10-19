package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MainHardware {

  public MecanumDrive drive;
  public RadicalIMU imu;

  private String[] motorIds = new String[]{
      "motorFrontLeft", "motorFrontRight",
      "motorBackLeft", "motorBackRight"
  };

  public MainHardware(HardwareMap hwMap) {
    drive = new MecanumDrive(hwMap, motorIds[0], motorIds[1], motorIds[2], motorIds[3], false);
    imu = new RadicalIMU(hwMap.get(BNO055IMU.class, "imu"));
  }

  public void init() {
//    hwMap =
  }

  public void update() {
    drive.update();
    imu.update();
  }
}
