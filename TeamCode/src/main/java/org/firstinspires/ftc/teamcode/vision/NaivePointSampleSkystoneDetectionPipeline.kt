package org.firstinspires.ftc.teamcode.vision

import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class NaivePointSampleSkystoneDetectionPipeline : Init3BlockDetection() {

    var valMid = -1
    var valRight = -1
    var valLeft = -1

    private val threshold = 102.0

    private val rectWidth: Double = 0.1875
    private val rectHeight: Double = 0.1875


    private val midX = .5
    private val midY = .5

    private val leftX = .25
    private val leftY = .5

    private val rightX = .75
    private val rightY = .5

    private val yCbCrChan2Mat = Mat()
    private val thresholdMat = Mat()
    private val combinedMat = Mat()

    private val contours = mutableListOf<MatOfPoint>()

    enum class PipelineStage {
        DETECTION,
        THRESHOLD,
        RAW_IMAGE
    }

    var stageToRenderToViewPoint = PipelineStage.DETECTION
    private val stages = PipelineStage.values()

    override fun onViewportTapped() {
        val currentStageNum = stageToRenderToViewPoint.ordinal
        var nextStageNum = currentStageNum + 1

        if (nextStageNum >= stages.size) nextStageNum = 0

        stageToRenderToViewPoint = stages[nextStageNum]
    }

    override fun processFrame(input: Mat?): Mat {
        contours.clear()

        Imgproc.cvtColor(input, yCbCrChan2Mat, Imgproc.COLOR_RGB2YCrCb)
        Core.extractChannel(yCbCrChan2Mat, yCbCrChan2Mat, 2)

        Imgproc.threshold(yCbCrChan2Mat, thresholdMat, threshold, 255.0, Imgproc.THRESH_BINARY_INV)

        Imgproc.findContours(
            thresholdMat,
            contours,
            Mat(),
            Imgproc.RETR_LIST,
            Imgproc.CHAIN_APPROX_SIMPLE
        )
        yCbCrChan2Mat.copyTo(combinedMat)

        Imgproc.drawContours(input, contours, -1, Scalar(255.0, 0.0, 0.0, 0.0), 3, 8)

        val pixelsMid =
            thresholdMat.get((input!!.rows() * midY).toInt(), (input.cols() * midX).toInt())
        val pixelsLeft =
            thresholdMat.get((input.rows() * leftY).toInt(), (input.cols() * leftX).toInt())
        val pixelsRight =
            thresholdMat.get((input.rows() * rightY).toInt(), (input.cols() * rightX).toInt())

        valMid = pixelsMid[0].toInt()
        valLeft = pixelsLeft[0].toInt()
        valRight = pixelsRight[0].toInt()

        detectedSkystonePosition =
            if (valLeft == 0) 0 else if (valMid == 0) 1 else if (valRight == 0) 2 else -1

        Imgproc.rectangle(
            combinedMat,
            Point(
                (input.cols() * (leftX - rectWidth / 2)),
                (input.rows() * (leftY - rectHeight / 2))
            ),
            Point(
                (input.cols() * (leftX + rectWidth / 2)),
                (input.rows() * (leftY + rectHeight / 2))
            ),
            Scalar(0.0, 255.0, 0.0), 3
        )
        Imgproc.rectangle(
            combinedMat,
            Point(
                (input.cols() * (midX - rectWidth / 2)),
                (input.rows() * (midY - rectHeight / 2))
            ),
            Point(
                (input.cols() * (midX + rectWidth / 2)),
                (input.rows() * (midY + rectHeight / 2))
            ),
            Scalar(0.0, 255.0, 0.0), 3
        )
        Imgproc.rectangle(
            combinedMat,
            Point(
                (input.cols() * (rightX - rectWidth / 2
                        )
                        ),
                (input.rows() * (rightY - rectHeight / 2))
            ),
            Point(
                (input.cols() * (rightX + rectWidth / 2)),
                (input.rows() * (rightY + rectHeight / 2))
            ),
            Scalar(0.0, 255.0, 0.0), 3
        )

        return when (stageToRenderToViewPoint) {
            PipelineStage.THRESHOLD -> thresholdMat
            PipelineStage.DETECTION -> combinedMat
            PipelineStage.RAW_IMAGE -> input
        }
    }

}