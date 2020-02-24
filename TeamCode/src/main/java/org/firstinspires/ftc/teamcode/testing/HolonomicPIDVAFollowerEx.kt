package org.firstinspires.ftc.teamcode.testing

import android.util.Log
import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.control.PIDFController
import com.acmerobotics.roadrunner.drive.DriveSignal
import com.acmerobotics.roadrunner.followers.TrajectoryFollower
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.kinematics.Kinematics
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.util.NanoClock

class HolonomicPIDVAFollowerEx @JvmOverloads constructor(
    axialCoeffs: PIDCoefficients,
    lateralCoeffs: PIDCoefficients,
    headingCoeffs: PIDCoefficients,
    admissibleError: Pose2d = Pose2d(),
    timeout: Double = 0.0,
    clock: NanoClock = NanoClock.system()
) : TrajectoryFollowerEx(admissibleError, timeout, clock) {
    private val axialController = PIDFController(axialCoeffs)
    private val lateralController = PIDFController(lateralCoeffs)
    private val headingController = PIDFController(headingCoeffs)

    override var lastError: Pose2d = Pose2d()

    init {
        headingController.setInputBounds(-Math.PI, Math.PI)
    }

    override fun followTrajectory(trajectory: Trajectory) {
        axialController.reset()
        lateralController.reset()
        headingController.reset()

        super.followTrajectory(trajectory)
    }

    override fun internalUpdate(currentPose: Pose2d): DriveSignal {
        val t = elapsedTime()

        val targetPose = trajectory[t]
        val targetVel = trajectory.velocity(t)
        val targetAccel = trajectory.acceleration(t)

        val targetRobotVel = Kinematics.fieldToRobotVelocity(targetPose, targetVel)
        val targetRobotAccel = Kinematics.fieldToRobotAcceleration(targetPose, targetVel, targetAccel)

        val poseError = Kinematics.calculatePoseError(targetPose, currentPose)

        // you can pass the error directly to PIDFController by setting setpoint = error and position = 0
        axialController.targetPosition = poseError.x
        lateralController.targetPosition = poseError.y
        headingController.targetPosition = poseError.heading

        // note: feedforward is processed at the wheel level; velocity is only passed here to adjust the derivative term
        val axialCorrection = axialController.update(0.0, targetRobotVel.x)
        val lateralCorrection = lateralController.update(0.0, targetRobotVel.y)
        val headingCorrection = headingController.update(0.0, targetRobotVel.heading)

        val correctedVelocity = targetRobotVel + Pose2d(
            axialCorrection,
            lateralCorrection,
            headingCorrection
        )

        lastError = poseError

        return DriveSignal(correctedVelocity, targetRobotAccel)
    }
}