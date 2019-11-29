package org.firstinspires.ftc.teamcode.vision;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvPipeline;

@TeleOp
@Disabled
public class WebcamExample extends LinearOpMode
{
  OpenCvCamera phoneCam;
  StageSwitchingPipeline stageSwitchingPipeline;

  @Override
  public void runOpMode()
  {
    /**
     * NOTE: Many comments have been omitted from this sample for the
     * sake of conciseness. If you're just starting out with EasyOpenCv,
     * you should take a look at {@link InternalCameraExample} or its
     * webcam counterpart, {@link WebcamExample} first.
     */

    int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    phoneCam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK, cameraMonitorViewId);
    phoneCam.openCameraDevice();
    stageSwitchingPipeline = new StageSwitchingPipeline();
    phoneCam.setPipeline(stageSwitchingPipeline);
    phoneCam.startStreaming(640, 480, OpenCvCameraRotation.UPRIGHT);

    waitForStart();

    while (opModeIsActive())
    {
      telemetry.addData("Num contours found", stageSwitchingPipeline.getNumContoursFound());
      telemetry.update();
      sleep(100);
    }
  }

  /*
   * With this pipeline, we demonstrate how to change which stage of
   * is rendered to the viewport when the viewport is tapped. This is
   * particularly useful during pipeline development. We also show how
   * to get data from the pipeline to your OpMode.
   */
  static class StageSwitchingPipeline extends OpenCvPipeline
  {
    Mat yCbCrChan2Mat = new Mat();
    Mat thresholdMat = new Mat();
    Mat contoursOnFrameMat = new Mat();
    List<MatOfPoint> contoursList = new ArrayList<>();
    int numContoursFound;

    enum Stage
    {
      YCbCr_CHAN2,
      THRESHOLD,
      CONTOURS_OVERLAYED_ON_FRAME,
      RAW_IMAGE,
    }

    private Stage stageToRenderToViewport = Stage.YCbCr_CHAN2;
    private Stage[] stages = Stage.values();

    @Override
    public void onViewportTapped()
    {
      /*
       * Note that this method is invoked from the UI thread
       * so whatever we do here, we must do quickly.
       */

      int currentStageNum = stageToRenderToViewport.ordinal();

      int nextStageNum = currentStageNum + 1;

      if(nextStageNum >= stages.length)
      {
        nextStageNum = 0;
      }

      stageToRenderToViewport = stages[nextStageNum];
    }

    @Override
    public Mat processFrame(Mat input)
    {
      contoursList.clear();

      /*
       * This pipeline finds the contours of yellow blobs such as the Gold Mineral
       * from the Rover Ruckus game.
       */
      Imgproc.cvtColor(input, yCbCrChan2Mat, Imgproc.COLOR_RGB2YCrCb);
      Core.extractChannel(yCbCrChan2Mat, yCbCrChan2Mat, 2);
      Imgproc.threshold(yCbCrChan2Mat, thresholdMat, 102, 255, Imgproc.THRESH_BINARY_INV);
      Imgproc.findContours(thresholdMat, contoursList, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
      numContoursFound = contoursList.size();
      input.copyTo(contoursOnFrameMat);
      Imgproc.drawContours(contoursOnFrameMat, contoursList, -1, new Scalar(0, 0, 255), 3, 8);

      switch (stageToRenderToViewport)
      {
        case YCbCr_CHAN2:
        {
          return yCbCrChan2Mat;
        }

        case THRESHOLD:
        {
          return thresholdMat;
        }

        case CONTOURS_OVERLAYED_ON_FRAME:
        {
          return contoursOnFrameMat;
        }

        case RAW_IMAGE:
        {
          return input;
        }

        default:
        {
          return input;
        }
      }
    }

    public int getNumContoursFound()
    {
      return numContoursFound;
    }
  }
}