package org.firstinspires.ftc.teamcode.vision

import org.opencv.core.*
import org.opencv.imgproc.Imgproc

class NaiveRectangleSamplingSkystoneDetectionPipeline : Init3BlockDetection() {
    private val matYCrCb = Mat() // YCrCb matt
    private val matCb = Mat() // To be extracted cB mat

    // Width and height of the sampling rectangles
    private val samplingRectWidth = 15
    private val samplingRectHeight = 15
    // Color of the sampling rect
    private val samplingRectColor = Scalar(0.0, 0.0, 255.0)
    // Thickness of the sampling rect lines
    private val samplingRectThickness = 1

    // Circle to indicate which stone was the skystone
    private val samplingCircleRadius = 5
    private val samplingCircleColor = Scalar(225.0, 52.0, 235.0)
    // -1 indicates no outline
    private val samplingCircleThickness = -1

    // Location of the sampling points based on percentages
    // 1st point: 15% x width, 50% y height
    // 2nd point: 50% x width, 50% y height
    // 3rd point: 85% x width, 50% y height
    private val samplePointPercentages = arrayOf(
        arrayOf(.15, .5), arrayOf(.5, .5), arrayOf(.85, .5)
    )

    // Create sampling point coordinates based on percentages
    private val samplePoints = samplePointPercentages.map {
        arrayOf(
            Point(
                it[0] * width - samplingRectWidth / 2,
                it[1] * height - samplingRectHeight / 2
            ),
            Point(
                it[0] * width + samplingRectWidth / 2,
                it[1] * height + samplingRectHeight / 2
            )
        )
    }

    // Link sample areas from the Cb channel
    var subMats = samplePoints.map {
        matCb.submat(Rect(it[0], it[1]))
    }

    override fun processFrame(input: Mat): Mat {
        // Convert image from RGB to YCrCb
        Imgproc.cvtColor(input, matYCrCb, Imgproc.COLOR_RGB2YCrCb)

        // Extract the Cb channel from the input
        Core.extractChannel(matYCrCb, matCb, 2)

        // Average sample areas
        val avgSamples = subMats.map {
            Core.mean(it).`val`[0]
        }

        // Draw rectangles around sample areas
        samplePoints.forEach {
            Imgproc.rectangle(input, it[0], it[1], samplingRectColor, samplingRectThickness)
        }

        // Find sample w/lowest contrast from blue (lightest color)
        val max = avgSamples.max()

        // Draw circle on detected skystone
        detectedSkystonePosition = avgSamples.indexOf(max)

        // Draw a circle on the detected point
        val detectedPoint = samplePoints[detectedSkystonePosition]
        Imgproc.circle(
            input,
            Point(
                (detectedPoint[0].x + detectedPoint[1].x) / 2,
                (detectedPoint[0].y + detectedPoint[1].y) / 2
            ),
            samplingCircleRadius,
            samplingCircleColor,
            samplingCircleThickness
        )

        // Free allocated submat memory
        subMats.forEach {
            it.release()
        }

        return input
    }
}