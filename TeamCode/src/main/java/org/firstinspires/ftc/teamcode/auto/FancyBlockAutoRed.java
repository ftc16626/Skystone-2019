package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.ftc16626.missioncontrol.math.PIDController;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.util.Range;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.firstinspires.ftc.teamcode.vision.Init3BlockDetection;
import org.firstinspires.ftc.teamcode.vision.NaiveRectangleSamplingSkystoneDetectionPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(group = "testing")
public class FancyBlockAutoRed extends LinearOpMode {
  OpenCvCamera webCam;
  Init3BlockDetection pipeline;

  private double targetHeight = 0;

  @Override
  public void runOpMode() throws InterruptedException {
    final MainHardware robot = new MainHardware(hardwareMap);

    final double kP = 0.25;
    final double kI = 0;
    final double kD = 2.5;

    final double kG = 0;

    double currentHeight = 0;
//    double targetHeight = 0;

    final double MAX_HEIGHT = robot.LIFT_MAX_HEIGHT / 10;
    final double MIN_HEIGHT = robot.LIFT_MIN_HEIGHT / 10;

    PIDController controller = new PIDController(kP, kI, kD, kG);

    controller.setBounds(-1, 1);
    robot.motorLift.setMode(RunMode.RUN_USING_ENCODER);
    robot.motorLift.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);
    Trajectory middleSkystoneTrajectory1 = drive.trajectoryBuilder()
        .splineTo(new Pose2d(800, 0, Math.toRadians(330)))
        .addMarker(new Function0<Unit>() {
          @Override
          public Unit invoke() {
            class TestThread extends Thread {
              public void run() {
                try {
                  sleep(1000);
                } catch (InterruptedException e) {
                  e.printStackTrace();
                }
                robot.intake.setPower(0.3);
              }
            }
//            new TestThread().start();

//            robot.intake.setPower(0.3);

            return Unit.INSTANCE;
          }
        })
        .back(400)
        .strafeRight(100)
        .splineTo(new Pose2d(700, -1450, Math.toRadians(270)))
        .addMarker(new Function0<Unit>() {
          @Override
          public Unit invoke() {
            targetHeight = 10;

            robot.intake.setPower(-0.4);

            return Unit.INSTANCE;
          }
        })
        .splineTo(new Pose2d(1400, -2300, Math.toRadians(0))).build();
//        .addMarker(new Function0<Unit>() {
//          @Override
//          public Unit invoke() {
//            robot.intake.toggleIntakeOpen();
//            return Unit.INSTANCE;
//          }
//        })
//        .back(500).build();

//    pipeline = new NaivePointSampleSkystoneDetectionPipeline();
    pipeline = new NaiveRectangleSamplingSkystoneDetectionPipeline();

    int cameraMonitorViewId = hardwareMap.appContext.getResources()
        .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

    webCam = OpenCvCameraFactory
        .getInstance()
        .createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
    webCam.openCameraDevice();
    webCam.setPipeline(pipeline);
    webCam.startStreaming(pipeline.getWidth(), pipeline.getHeight(), OpenCvCameraRotation.UPRIGHT);

    while (opModeIsActive() && !isStarted()) {
      //      telemetry.addData("Values", pipeline.getValLeft() + "   " + pipeline.getValMid() + "   " + pipeline.getValRight());
      telemetry.addData("Skystone", pipeline.getDetectedSkystonePosition());
      telemetry.addData("Skystone Positions",
          pipeline.getSkystonePositions(3)[0] + "" + pipeline.getSkystonePositions(3)[1]);
//      telemetry.addData("Stage", pipeline.getStageToRenderToViewPoint());
      telemetry.addData("Width", pipeline.getHeight());
      telemetry.addData("Height", pipeline.getWidth());

      telemetry.addData("Frame Count", webCam.getFrameCount());
      telemetry.addData("FPS", String.format("%.2f", webCam.getFps()));
      telemetry.addData("Total frame time ms", webCam.getTotalFrameTimeMs());
      telemetry.addData("Pipeline time ms", webCam.getPipelineTimeMs());
      telemetry.addData("Overhead time ms", webCam.getOverheadTimeMs());
      telemetry.addData("Theoretical max FPS", webCam.getCurrentPipelineMaxFps());

      telemetry.update();
      sleep(100);
    }
    waitForStart();

    robot.init();
    robot.dropSliderServo();
    robot.intake.directionBackward();
    robot.intake.toggle(true);

    sleep(500);

//    if (pipeline.getSkystonePositions(0)[0] == 1) {
      drive.followTrajectory(middleSkystoneTrajectory1);
//      robot.intake.toggleIntakeOpen();
//      drive.followTrajectorySync(drive.traj);
//      drive.followTrajectorySync(middleSkystoneTrajectory2);
//    }

    while (opModeIsActive()) {
      targetHeight = Range.clip(targetHeight, MIN_HEIGHT, MAX_HEIGHT);

      currentHeight = robot.liftRange.getDistance(DistanceUnit.CM);
      double power = -controller.update(targetHeight - currentHeight);
      robot.motorLift.setPower(currentHeight < MIN_HEIGHT && targetHeight < MIN_HEIGHT ? 0 : power);

      drive.update();
    }
  }
}
