package org.firstinspires.ftc.teamcode.hardware.roadrunnerex.trajectorystep

import com.acmerobotics.roadrunner.trajectory.Trajectory

class PathStep(val trajectory: Trajectory): TrajectoryStep {
    override val TYPE = TrajectoryStepType.PATH

}