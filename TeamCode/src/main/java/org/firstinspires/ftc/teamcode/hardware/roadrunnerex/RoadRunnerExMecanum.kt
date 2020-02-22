package org.firstinspires.ftc.teamcode.hardware.roadrunnerex

import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.hardware.roadrunner.SampleMecanumDriveBase
import org.firstinspires.ftc.teamcode.hardware.roadrunnerex.trajectorystep.*

class RoadRunnerExMecanum(val driveBase: SampleMecanumDriveBase) {
    private var currentTrajectoryEx: TrajectoryEx? = null

    private var currentStep = -1

    private var currentlyFollowingAPath = false
    private var startedWaiting = ElapsedTime()

    fun followTrajectorySync(trajectory: TrajectoryEx) {
        followTrajectory(trajectory)
        waitForIdle()
    }

    fun followTrajectory(trajectory: TrajectoryEx) {
        if (trajectory.trajectoryList.size == 0) return

        currentTrajectoryEx = trajectory
        currentStep = 0
    }

    fun waitForIdle() {
        while (!Thread.currentThread().isInterrupted && isBusy()) update()
    }

    fun isBusy(): Boolean {
        return currentStep != -1
    }

    fun update() {
        if (currentTrajectoryEx != null) {
            val currentPath = currentTrajectoryEx!!.trajectoryList[currentStep]

            if (!currentlyFollowingAPath) {
                when (currentPath.TYPE) {
                    TrajectoryStepType.PATH -> driveBase.followTrajectory((currentPath as PathStep).trajectory)
                    TrajectoryStepType.TURN -> driveBase.turn((currentPath as TurnStep).angle)
                    TrajectoryStepType.WAIT -> startedWaiting.reset()
                    TrajectoryStepType.WAIT_CONDITIONAL -> return
                }

                currentlyFollowingAPath = true
                currentStep++
            } else {
                if (currentPath.TYPE == TrajectoryStepType.WAIT) {
                    if (startedWaiting.seconds() > (currentPath as WaitStep).seconds)
                        currentlyFollowingAPath = false
                } else if (currentPath.TYPE == TrajectoryStepType.WAIT_CONDITIONAL) {
                    if ((currentPath as WaitConditionalStep).callback.onWait())
                        currentlyFollowingAPath = false
                } else if (!driveBase.isBusy) {
                    currentlyFollowingAPath = false
                }
            }

            if (currentStep >= currentTrajectoryEx!!.trajectoryList.size) {
                currentTrajectoryEx = null
                currentStep = -1
            }
        }

        driveBase.update()
    }
}