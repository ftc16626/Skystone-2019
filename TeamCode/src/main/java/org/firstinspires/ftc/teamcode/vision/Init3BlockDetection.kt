package org.firstinspires.ftc.teamcode.vision

import org.openftc.easyopencv.OpenCvPipeline

abstract class Init3BlockDetection : OpenCvPipeline() {
    private val stoneRowMaxWidth = 6

    var detectedSkystonePosition = -1

    val cols = 480
    val rows = 640

    fun getSkystonePositions(leftMostPosition: Int): Array<Int> {
        val firstSkystonePosition = detectedSkystonePosition + leftMostPosition
        var secondSkystonePosition: Int

        if(firstSkystonePosition >= stoneRowMaxWidth / 2) {
            secondSkystonePosition = firstSkystonePosition - 3
        } else {
            secondSkystonePosition = firstSkystonePosition + 3
        }

        val skystonePositions = arrayOf(firstSkystonePosition, secondSkystonePosition)
        skystonePositions.sort()

        return skystonePositions
    }
}