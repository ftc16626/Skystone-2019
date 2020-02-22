package org.firstinspires.ftc.teamcode.hardware.util

import com.qualcomm.robotcore.hardware.Servo

class GhostServoGoBildaTorque
    @JvmOverloads constructor(
        servo: Servo,
        min: Double = 0.0,
        max: Double = 1.0,
        scaleSpeed: Double = 1.0,
        reversed: Boolean = false
        ): GhostServo(servo, min, max, 0.25, 300.0, scaleSpeed, reversed)