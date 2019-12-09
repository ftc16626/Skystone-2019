package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.openftc.revextensions2.ExpansionHubEx;

public class MainHardware {

  public ExpansionHubEx expansionHubMain;
  public ExpansionHubEx expansionHubDaughter;

  public MecanumDrive drive;
//  public RadicalIMU imu;
  public SimpleIMU imu;
  public Intake intake;
  public Lift lift;

  public Servo backRightServo;
  public Servo backLeftServo;
  public Servo swingyServo;

  public DcMotor motorLift;

  public Rev2mDistanceSensor liftRange;

  private final String[] motorIds = new String[]{
      "motorFrontLeft", "motorFrontRight",
      "motorBackLeft", "motorBackRight"
  };

  private final String[] intakeMotorIds = new String[]{"intakeLeft", "intakeRight"};
  private final String INTAKE_SERVO_ID = "intakeServo";

  private final String SLIDER_MOTOR_ID = "motorLift";

  private final String BACK_LEFT_SERVO_ID = "backLeftServo";
  private final String BACK_RIGHT_SERVO_ID = "backRightServo";

  private final String SWINGY_SERVO_ID = "swingyServo";

  private final String SLIDER_RANGE_ID = "liftRange";

  // Hardware Constraints
  private final double BACK_RIGHT_SERVO_MIN = 0.40;
  private final double BACK_RIGHT_SERVO_MAX = 0.92;
  private final double BACK_RIGHT_SERVO_PERPENDICULAR = 0.21;

  private final double BACK_LEFT_SERVO_MIN = 0.45;
  private final double BACK_LEFT_SERVO_MAX = 0.9;
  private final double BACK_LEFT_SERVO_PERPENDICULAR = 0.9;

  private final double SWINGY_SERVO_MIN = 0.38;
  private final double SWINGY_SERVO_MAX = 1;

  public final double LIFT_MAX_HEIGHT = 620;
  public final double LIFT_MIN_HEIGHT = 10;

  // Units in millimeters
  public final Dimensions dimensionsDriveTrain = new Dimensions(198.125, 336, 50);
  public final double WHEEL_RADIUS = 100 / 2;

  public final double GEAR_RATIO = 1;
  public final double ENCODER_COUNTS_PER_REV = 386.3;

  public MainHardware(HardwareMap hwMap) {
    expansionHubMain = hwMap.get(ExpansionHubEx.class, "Expansion Hub 9");
    expansionHubDaughter = hwMap.get(ExpansionHubEx.class, "Expansion Hub 2");

    drive = new MecanumDrive(
        hwMap,
        expansionHubMain,
        motorIds[0], motorIds[1], motorIds[2], motorIds[3],
        true,
        dimensionsDriveTrain.getWidth(), dimensionsDriveTrain.getHeight(), WHEEL_RADIUS,
        GEAR_RATIO, ENCODER_COUNTS_PER_REV
    );
//    imu = new RadicalIMU(hwMap.get(BNO055IMU.class, "imu"), false);
    imu = new SimpleIMU(hwMap.get(BNO055IMU.class, "imu"));

    intake = new Intake(hwMap, intakeMotorIds[0], intakeMotorIds[1], INTAKE_SERVO_ID);

    lift = new Lift(hwMap, SLIDER_MOTOR_ID, SLIDER_RANGE_ID, LIFT_MAX_HEIGHT / 10, LIFT_MIN_HEIGHT / 10);

    backLeftServo = hwMap.get(Servo.class, BACK_LEFT_SERVO_ID);
    backLeftServo.scaleRange(BACK_LEFT_SERVO_MIN, BACK_LEFT_SERVO_MAX);

    backRightServo = hwMap.get(Servo.class, BACK_RIGHT_SERVO_ID);
    backRightServo.scaleRange(BACK_RIGHT_SERVO_MIN, BACK_RIGHT_SERVO_MAX);

    swingyServo = hwMap.get(Servo.class, SWINGY_SERVO_ID);

    motorLift = hwMap.get(DcMotor.class, SLIDER_MOTOR_ID);

    liftRange = (Rev2mDistanceSensor) hwMap.get(DistanceSensor.class, SLIDER_RANGE_ID);
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
    swingyServo.setPosition(SWINGY_SERVO_MAX);
  }

  public void lockSliderServo() {
    swingyServo.setPosition(SWINGY_SERVO_MIN);
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

  public void perpendicularBackLeftServo() {
    backLeftServo.setPosition(BACK_LEFT_SERVO_PERPENDICULAR);
  }

  public void perpendicularBackRightServo() {
    backRightServo.setPosition(BACK_RIGHT_SERVO_PERPENDICULAR);
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
