package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.auto.deliverypaths.SkyStoneDeliverFoundationMiddleBlue;
import org.firstinspires.ftc.teamcode.auto.deliverypaths.SkyStoneDeliverFoundationMiddleRed;
import org.firstinspires.ftc.teamcode.hardware.DriveBaseMecanum;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.firstinspires.ftc.teamcode.vision.Init3BlockDetection;
import org.firstinspires.ftc.teamcode.vision.NaiveRectangleSamplingSkystoneDetectionPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name="Auto - Deliver/Foundation Blue")
public class AutoSkystoneDeliverFoundationBlue extends LinearOpMode {
  @Override
  public void runOpMode() throws InterruptedException {
    SkyStoneDeliverFoundationMiddleBlue path;
//    DeliveryPath path;

    final MainHardware robot = new MainHardware(hardwareMap);
    DriveBaseMecanum drive = new DriveBaseMecanum(hardwareMap);

    Init3BlockDetection pipeline = new NaiveRectangleSamplingSkystoneDetectionPipeline();
    int cameraMonitorViewId = hardwareMap.appContext.getResources()
        .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

    OpenCvCamera camera = OpenCvCameraFactory
        .getInstance()
        .createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
    camera.openCameraDevice();
    camera.setPipeline(pipeline);
    camera.startStreaming(pipeline.getWidth(), pipeline.getHeight(), OpenCvCameraRotation.UPRIGHT);

    while(!isStarted()) {
      telemetry.addData("Skystone", pipeline.getDetectedSkystonePosition());
      telemetry.addData("Skystone Positions",
          pipeline.getSkystonePositions(3)[0] + "" + pipeline.getSkystonePositions(3)[1]);

      telemetry.addData("Frame Count", camera.getFrameCount());
      telemetry.addData("FPS", String.format("%.2f", camera.getFps()));
      telemetry.addData("Total frame time ms", camera.getTotalFrameTimeMs());
      telemetry.addData("Pipeline time ms", camera.getPipelineTimeMs());
      telemetry.addData("Overhead time ms", camera.getOverheadTimeMs());
      telemetry.addData("Theoretical max FPS", camera.getCurrentPipelineMaxFps());

      telemetry.update();
      sleep(100);
    }

    waitForStart();

    robot.init();
    robot.dropSliderServo();
    robot.intake.directionBackward();
    robot.intake.toggle(true);

//    sleep(300);

//    switch(pipeline.getSkystonePositions(0)[0]) {
//      default:
//      case 0:
//        path = new SkyStoneDeliverFoundationFirstRed(robot, drive);
//        break;
//      case 1:
//        path = new SkyStoneDeliverFoundationMiddleRed(robot, drive);
//        break;
//      case 2:
//        path = new SkyStoneDeliverFoundationLastRed(robot, drive);
//        break;
//    }

    path = new SkyStoneDeliverFoundationMiddleBlue(robot, drive);

    path.telemetry = telemetry;
    path.init();

    while(opModeIsActive()) {
      robot.lift.update();
      path.update();
    }
  }
}
