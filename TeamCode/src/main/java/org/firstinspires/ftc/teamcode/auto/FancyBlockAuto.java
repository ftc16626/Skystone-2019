package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.vision.Init3BlockDetection;
import org.firstinspires.ftc.teamcode.vision.NaiveRectangleSamplingSkystoneDetectionPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(group="testing")
public class FancyBlockAuto extends LinearOpMode {

  OpenCvCamera webCam;
  Init3BlockDetection pipeline;

  @Override
  public void runOpMode() throws InterruptedException {
//    pipeline = new NaivePointSampleSkystoneDetectionPipeline();
    pipeline = new NaiveRectangleSamplingSkystoneDetectionPipeline();

    int cameraMonitorViewId = hardwareMap.appContext.getResources()
        .getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

    webCam = OpenCvCameraFactory
        .getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
    webCam.openCameraDevice();
    webCam.setPipeline(pipeline);
    webCam.startStreaming(pipeline.getWidth(), pipeline.getHeight(), OpenCvCameraRotation.UPRIGHT);

    waitForStart();

    while(opModeIsActive()) {
//      telemetry.addData("Values", pipeline.getValLeft() + "   " + pipeline.getValMid() + "   " + pipeline.getValRight());
      telemetry.addData("Skystone", pipeline.getDetectedSkystonePosition());
      telemetry.addData("Skystone Positions", pipeline.getSkystonePositions(3)[0] + "" + pipeline.getSkystonePositions(3)[1]);
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
  }
}
