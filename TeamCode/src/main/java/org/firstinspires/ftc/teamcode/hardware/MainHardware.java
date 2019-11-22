package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.openftc.revextensions2.ExpansionHubEx;

public class MainHardware {

  public ExpansionHubEx expansionHubMain;
  public ExpansionHubEx expansionHubDaughter;

  public MecanumDrive drive;
  public RadicalIMU imu;
  public Intake intake;

  public Servo backRightServo;
  public Servo backLeftServo;
  public Servo swingyServo;

  public DcMotor motorSlider;

  public Rev2mDistanceSensor sliderRange;

  private final String[] motorIds = new String[]{
      "motorFrontLeft", "motorFrontRight",
      "motorBackLeft", "motorBackRight"
  };

  private final String[] intakeMotorIds = new String[]{"intakeLeft", "intakeRight"};
  private final String intakeServoId = "intakeServo";

  private final String sliderMotorId = "motorSlider";

  private final String backLeftServoId = "backLeftServo";
  private final String backRightServoId = "backRightServo";

  private final String swingyServoId = "swingyServo";

  private final String sliderRangeId = "sliderRange";

  // Hardware Constraints
  private final double backRightServoMin = 0.50;
  private final double backRightServoMax = 0.92;

  private final double backLeftServoMin = 0.45;
  private final double backLeftServoMax = 0.8;

  private final double swingyServoMin = 0.38;
  private final double swingyServoMax = 1;

  // Positions
  private double backLeftServoPos = 0;
  private double backRightServoPos = 0;

  // Units in millimeters
  public final Dimensions dimensionsDriveTrain = new Dimensions(198.125, 336, 50);
  public final double wheelRadius = 100 / 2;

  public final double gearRatio = 1;
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

    backLeftServo = hwMap.get(Servo.class, backLeftServoId);
    backLeftServo.scaleRange(backLeftServoMin, backLeftServoMax);

    backRightServo = hwMap.get(Servo.class, backRightServoId);
    backRightServo.scaleRange(backRightServoMin, backRightServoMax);

    swingyServo = hwMap.get(Servo.class, swingyServoId);

    motorSlider = hwMap.get(DcMotor.class, sliderMotorId);
//    motorSlider.setTargetPosition(0);
//    motorSlider.setMode(RunMode.RUN_TO_POSITION);

    sliderRange = (Rev2mDistanceSensor) hwMap.get(DistanceSensor.class, sliderRangeId);
  }

  public void init() {
    drive.resetEncoders();

    raiseBackServos();
    lockSliderServo();

    intake.close();
  }

  public void update() {
    drive.update();
    imu.update();
  }

  public void dropSliderServo() {
    swingyServo.setPosition(swingyServoMax);
  }

  public void lockSliderServo() {
    swingyServo.setPosition(swingyServoMin);
  }

  public void lowerBackLeftServo() {
    backLeftServo.setPosition(1);
  }

  public void lowerBackRightServo() {
    backRightServo.setPosition(0);
  }

  public void raiseBackLeftServo() {
    backLeftServo.setPosition(0);
  }

  public void raiseBackRightServo() {
    backRightServo.setPosition(1);
  }

  public void lowerBackServos() {
    lowerBackLeftServo();
    lowerBackRightServo();
  }

  public void raiseBackServos() {
    raiseBackLeftServo();
    raiseBackRightServo();
  }
}
