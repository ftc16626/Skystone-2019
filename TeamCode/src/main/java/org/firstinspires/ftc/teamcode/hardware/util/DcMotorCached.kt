package org.firstinspires.ftc.teamcode.hardware.util

import com.qualcomm.robotcore.hardware.DcMotor

class DcMotorCached @JvmOverloads constructor(val motor: DcMotor, @JvmField val CACHE_THRESHOLD: Double = 0.02) {
    private var lastRead = 0.0

    fun setPower(power: Double) {
        if (Math.abs(lastRead - power) > CACHE_THRESHOLD || power == 0.0) {
            motor.power = power
            lastRead = power
        }
    }
}