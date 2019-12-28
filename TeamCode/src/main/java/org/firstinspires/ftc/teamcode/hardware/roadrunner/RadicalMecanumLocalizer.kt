package org.firstinspires.ftc.teamcode.hardware.roadrunner

import com.acmerobotics.roadrunner.drive.MecanumDrive
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.kinematics.Kinematics
import com.acmerobotics.roadrunner.kinematics.MecanumKinematics
import com.acmerobotics.roadrunner.localization.Localizer
import com.acmerobotics.roadrunner.util.Angle

class RadicalMecanumLocalizer @JvmOverloads constructor(
    private val drive: MecanumDrive,
    private val useExternalHeading: Boolean = true
) : Localizer {
    private val xScaleVel = 100.0 / 139.0
    private val yScaleVel = 100.0 / 150.0

    private var _poseEstimate = Pose2d()
    override var poseEstimate: Pose2d
        get() = _poseEstimate
        set(value) {
            lastWheelPositions = emptyList()
            lastExtHeading = Double.NaN
            if (useExternalHeading) drive.externalHeading = value.heading
            _poseEstimate = value
        }
    private var lastWheelPositions = emptyList<Double>()
    private var lastExtHeading = Double.NaN

    override fun update() {
        val wheelPositions = drive.getWheelPositions()
        val extHeading = if (useExternalHeading) drive.externalHeading else Double.NaN
        if (lastWheelPositions.isNotEmpty()) {
            val wheelDeltas = wheelPositions
                .zip(lastWheelPositions)
                .map { it.first - it.second }
            val robotPoseDelta = MecanumKinematics.wheelToRobotVelocities(
                wheelDeltas, DriveConstants.TRACK_WIDTH, DriveConstants.TRACK_WIDTH
            )

            val modifiedRobotPoseDelta = Pose2d(robotPoseDelta.x * xScaleVel, robotPoseDelta.y * yScaleVel,robotPoseDelta.heading)

            val finalHeadingDelta = if (useExternalHeading)
                Angle.normDelta(extHeading - lastExtHeading)
            else
                robotPoseDelta.heading
//            _poseEstimate = Kinematics.relativeOdometryUpdate(
//                _poseEstimate,
//                Pose2d(robotPoseDelta.vec(), finalHeadingDelta)
//            )

            _poseEstimate = Kinematics.relativeOdometryUpdate(
                _poseEstimate,
                Pose2d(modifiedRobotPoseDelta.vec(), finalHeadingDelta)
            )
        }
        lastWheelPositions = wheelPositions
        lastExtHeading = extHeading
    }
}