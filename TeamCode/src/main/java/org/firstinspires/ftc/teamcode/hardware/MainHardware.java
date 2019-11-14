package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
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
  public Servo backServo;
  public Servo swingyServo;

  public DcMotor motorSlider;

  private final String[] motorIds = new String[]{
      "motorFrontLeft", "motorFrontRight",
      "motorBackLeft", "motorBackRight"
  };

  private final String[] intakeMotorIds = new String[]{"intakeLeft", "intakeRight"};
  private final String intakeServoId = "intakeServo";

  private final String sliderMotorId = "motorSlider";

  private final String starboardServoId = "starboardServo";
  private final String backSerovId = "backServo";
  private final String swingyServoId = "swingyServo";

  private double starboardServoPos = 0;

  // Millimeters
  public final Dimensions dimensionsDriveTrain = new Dimensions(198.125, 336, 50);
  public final double wheelRadius = 100 / 2;

  public final double gearRatio = 2;
  public final double encoderCountsPerRev = 386.3;

  public MainHardware(HardwareMap hwMap) {
    expansionHubMain = hwMap.get(ExpansionHubEx.class, "Expansion Hub 9");
    expansionHubDaughter = hwMap.get(ExpansionHubEx.class, "Expansion Hub 2");

    drive = new MecanumDrive(
        hwMap,
        expansionHubMain,
        motorIds[0], motorIds[1], motorIds[2], motorIds[3],
        true,
        dimensionsDriveTrain.getWidth(), dimensionsDriveTrain.getHeight(), wheelRadius,
        gearRatio, encoderCountsPerRev
    );
    imu = new RadicalIMU(hwMap.get(BNO055IMU.class, "imu"), false);

    intake = new Intake(hwMap, intakeMotorIds[0], intakeMotorIds[1], intakeServoId);

    starboardServo = hwMap.get(Servo.class, starboardServoId);
    starboardServo.scaleRange(0.35, 0.78);

    backServo = hwMap.get(Servo.class, backSerovId);
    backServo.scaleRange(0, 0.82);

    swingyServo = hwMap.get(Servo.class, swingyServoId);

    motorSlider = hwMap.get(DcMotor.class, sliderMotorId);
  }

  public void init() {
    drive.resetEncoders();

    starboardServo.setPosition(1);
    backServo.setPosition(1);
    swingyServo.setPosition(0.38);

    intake.close();
  }

  public void update() {
    drive.update();
    imu.update();
  }

  public void lowerStarboardServo() {
    starboardServoPos = Math.max(starboardServoPos - 0.05, 0);
    starboardServo.setPosition(starboardServoPos);
  }

  public void raiseStarboardServo() {
    starboardServoPos = Math.min(starboardServoPos + 0.05, 1);
    starboardServo.setPosition(starboardServoPos);
  }

  public double getStarboardServoPos() {
    return starboardServoPos;
  }
}
