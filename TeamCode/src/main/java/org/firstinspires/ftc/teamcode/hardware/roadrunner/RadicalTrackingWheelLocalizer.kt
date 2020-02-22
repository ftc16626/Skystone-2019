package org.firstinspires.ftc.teamcode.hardware.roadrunner

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
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
        private var WHEEL_RADIUS = 0.75 // in
        private var GEAR_RATIO =
            0.252476537 * 1.011824101657394 * 0.9782104794947832 // output (wheel) speed / input (encoder) speed
        var LATERAL_DISTANCE =
            16 * 0.989908320012592 // in; distance between the left and right wheels
        var FORWARD_OFFSET = -3.25 * 0.98 // in; offset of the lateral wheel

        var MULTIPLIER_X = 1.003821352575286 * 1.00206844852084
        var MULTIPLIER_Y = 0.99447513812154

        fun encoderTicksToInches(ticks: Int): Double {
            return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV
        }
    }

    private val leftEncoder: DcMotor = robot.hwMap.dcMotor["motorIntakeLeftAndEncoderLeft"]
    private val rightEncoder: DcMotor = robot.hwMap.dcMotor["motorIntakeRightAndEncoderRight"]
    private val frontEncoder: DcMotor = robot.hwMap.dcMotor["motorLiftTopAndEncoderMiddle"]

    init {
        leftEncoder.direction = DcMotorSimple.Direction.REVERSE
    }

    override fun getWheelPositions(): List<Double> {
        val bulkData = robot.bulkDataOne ?: return listOf(0.0, 0.0, 0.0)

        return listOf(
            encoderTicksToInches(bulkData.getMotorCurrentPosition(leftEncoder)) * MULTIPLIER_X,
            encoderTicksToInches(bulkData.getMotorCurrentPosition(rightEncoder)) * MULTIPLIER_X,
            encoderTicksToInches(bulkData.getMotorCurrentPosition(frontEncoder)) * MULTIPLIER_Y
        )
    }
}