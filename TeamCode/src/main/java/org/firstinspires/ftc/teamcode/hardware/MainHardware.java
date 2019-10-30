package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.openftc.revextensions2.ExpansionHubEx;

public class MainHardware {

  public ExpansionHubEx expansionHubMain;
  public ExpansionHubEx expansionHubDaughter;

  public MecanumDrive drive;
  public RadicalIMU imu;
  public Intake intake;
  public Servo starboardServo;

  private String[] motorIds = new String[]{
      "motorFrontLeft", "motorFrontRight",
      "motorBackLeft", "motorBackRight"
  };

  private String[] intakeMotorIds = new String[] { "intakeLeft", "intakeRight" };

  private String starboardServoId = "starboardServo";

  public MainHardware(HardwareMap hwMap) {
    expansionHubMain = hwMap.get(ExpansionHubEx.class, "Expansion Hub 9");
    expansionHubDaughter = hwMap.get(ExpansionHubEx.class, "Expansion Hub 2");

    drive = new MecanumDrive(hwMap, expansionHubMain, motorIds[0], motorIds[1], motorIds[2], motorIds[3], true);
    imu = new RadicalIMU(hwMap.get(BNO055IMU.class, "imu"));
    intake = new Intake(hwMap, intakeMotorIds[0], intakeMotorIds[1]);

    starboardServo = hwMap.get(Servo.class, starboardServoId);
    starboardServo.scaleRange(0.35, 0.83);
  }

  public void init() {
    drive.resetEncoders();
  }

  public void update() {
    drive.update();
    imu.update();
  }
}
