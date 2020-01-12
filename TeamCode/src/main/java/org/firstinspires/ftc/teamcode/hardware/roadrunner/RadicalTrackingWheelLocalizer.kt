package org.firstinspires.ftc.teamcode.hardware.roadrunner

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.hardware.Robot
import java.util.*

@Config
class RadicalTrackingWheelLocalizer(val robot: Robot) :
    ThreeTrackingWheelLocalizer(
        Arrays.asList(
            Pose2d(0.0, LATERAL_DISTANCE / 2, 0.0),  // left
            Pose2d(0.0, -LATERAL_DISTANCE / 2, 0.0),  // right
            Pose2d(
                FORWARD_OFFSET,
                0.0,
                Math.toRadians(90.0)
            ) // front
        )
    ) {
    companion object {
        private var TICKS_PER_REV = 720
        private var WHEEL_RADIUS = 1.5 // in
        private var GEAR_RATIO = 1.0 // output (wheel) speed / input (encoder) speed
        var LATERAL_DISTANCE = 10.0 // in; distance between the left and right wheels
        var FORWARD_OFFSET = 4.0 // in; offset of the lateral wheel
        fun encoderTicksToInches(ticks: Int): Double {
            return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV
        }
    }

    private val leftEncoder: DcMotor = robot.hwMap.dcMotor["motorIntakeLeftAndEncoderLeft"]
    private val rightEncoder: DcMotor = robot.hwMap.dcMotor["motorIntakeRightAndEncoderRight"]
    private val frontEncoder: DcMotor = robot.hwMap.dcMotor["motorLiftTopAndEncoderMiddle"]

    override fun getWheelPositions(): List<Double> {
        val bulkData = robot.bulkDataLeft ?: return listOf(0.0, 0.0, 0.0)

        return listOf(
            encoderTicksToInches(bulkData.getMotorCurrentPosition(leftEncoder)),
            encoderTicksToInches(bulkData.getMotorCurrentPosition(rightEncoder)),
            encoderTicksToInches(bulkData.getMotorCurrentPosition(frontEncoder))
        )
    }
}