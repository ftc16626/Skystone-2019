package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

public class MainHardware {

  public MecanumDrive drive;

  private String[] motorIds = new String[]{
      "motorFrontLeft", "motorFrontRight",
      "motorBackLeft", "motorBackRight"
  };

  public MainHardware(HardwareMap hwMap) {
    drive = new MecanumDrive(hwMap, motorIds[0], motorIds[1], motorIds[2], motorIds[3], false);
  }

  public void init() {
//    hwMap =
  }
}
