package org.firstinspires.ftc.teamcode.auto;

import android.util.Log;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.vision.Init3BlockDetection;
import org.firstinspires.ftc.teamcode.vision.NaiveRectangleSamplingSkystoneDetectionPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class SubsystemVision extends HardwareSubsystem {

  public Init3BlockDetection pipeline;

  private Robot robot;
  private OpenCvCamera camera;

  public SubsystemVision(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);

    this.robot = robot;
  }

  @Override
  public void onInit() {
    pipeline = new NaiveRectangleSamplingSkystoneDetectionPipeline();
    int cameraMonitorViewId = robot.hwMap.appContext.getResources()
        .getIdentifier(
            "cameraMonitorViewId",
            "id",
            robot.hwMap.appContext.getPackageName()
        );

    camera = OpenCvCameraFactory
        .getInstance()
        .createWebcam(robot.hwMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
    camera.openCameraDevice();
    camera.setPipeline(pipeline);
    camera.startStreaming(pipeline.getWidth(), pipeline.getHeight(), OpenCvCameraRotation.UPRIGHT);
  }

  @Override
  public void initLoop() {
    getOpMode().telemetry.addData("Skystone", pipeline.getDetectedSkystonePosition());
    getOpMode().telemetry.addData("Skystone Positions",
        pipeline.getSkystonePositions(3)[0] + "" + pipeline.getSkystonePositions(3)[1]);
    getOpMode().telemetry.addData("Frame Count", camera.getFrameCount());
    getOpMode().telemetry.addData("FPS", String.format("%.2f", camera.getFps()));
    getOpMode().telemetry.addData("Total frame time ms", camera.getTotalFrameTimeMs());
    getOpMode().telemetry.addData("Pipeline time ms", camera.getPipelineTimeMs());
    getOpMode().telemetry.addData("Overhead time ms", camera.getOverheadTimeMs());
    getOpMode().telemetry.addData("Theoretical max FPS", camera.getCurrentPipelineMaxFps());

    getOpMode().telemetry.update();
    getOpMode().sleep(10);
  }

  @Override
  public void onStop() {
    camera.stopStreaming();
    camera.closeCameraDevice();
  }
}
