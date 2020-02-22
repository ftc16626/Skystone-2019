package org.firstinspires.ftc.teamcode.hardware.roadrunnerex.trajectorystep

import org.firstinspires.ftc.teamcode.hardware.roadrunnerex.WaitCallback

class WaitConditionalStep(val callback: WaitCallback): TrajectoryStep {
    override val TYPE = TrajectoryStepType.WAIT_CONDITIONAL
}