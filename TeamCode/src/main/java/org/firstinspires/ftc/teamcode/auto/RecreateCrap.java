package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumConstraints;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Servo.Direction;
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveConstants;
import org.firstinspires.ftc.teamcode.tuning.DriveBaseMecanumOld;

@Autonomous(name="FUCK", group="FUCK")
public class RecreateCrap extends LinearOpMode {

  private double startX = -32.5;
  private double startY = -62.5;
  private double startHeading = Math.toRadians(90);

  private Pose2d startPose = new Pose2d(startX, startY, startHeading);

  private Servo stoneGuideLeftServo, stoneGuideRightServo;
  private final String[] stoneGuideServoIds = new String[]{"servoStoneGuideLeft", "servoStoneGuideRight"};

  private final double STONE_GUIDE_LEFT_MIN = 0;
  private final double STONE_GUIDE_LEFT_MAX = 0.5;

  private final double STONE_GUIDE_RIGHT_MIN = 0.5;
  private final double STONE_GUIDE_RIGHT_MAX = 1;

  @Override
  public void runOpMode() throws InterruptedException {
    stoneGuideLeftServo = hardwareMap.get(Servo.class, stoneGuideServoIds[0]);
    stoneGuideRightServo = hardwareMap.get(Servo.class, stoneGuideServoIds[1]);

    stoneGuideRightServo.setDirection(Direction.REVERSE);

    stoneGuideLeftServo.scaleRange(STONE_GUIDE_LEFT_MIN, STONE_GUIDE_LEFT_MAX);
    stoneGuideRightServo.scaleRange(STONE_GUIDE_RIGHT_MIN, STONE_GUIDE_RIGHT_MAX);

    DriveBaseMecanumOld drive = new DriveBaseMecanumOld(hardwareMap);
    drive.setPoseEstimate(startPose);

    waitForStart();

    if (isStopRequested()) return;

    dropStoneGuides();

    drive.followTrajectory(
//        drive.trajectoryBuilder()
        new TrajectoryBuilder(startPose, startHeading, new MecanumConstraints(DriveConstants.BASE_CONSTRAINTS,
            DriveConstants.TRACK_WIDTH))
//            .splineTo(new Pose2d(20, -20, Math.toRadians(0)))
//            .splineTo(new Pose2d(-20, -30, Math.toRadians(120)))
//            .splineTo(new Pose2d(20.0, -35.0, Math.toRadians(180.0 + 180)))
              .strafeRight(20)
              .forward(20)
            .build()
    );

    while(opModeIsActive()) {
      drive.update();

    }
  }

  public void dropStoneGuides() {
    stoneGuideLeftServo.setPosition(0);
    stoneGuideRightServo.setPosition(0);
  }

  public void raiseStoneGuides() {
    stoneGuideLeftServo.setPosition(1);
    stoneGuideRightServo.setPosition(1);
  }
}
