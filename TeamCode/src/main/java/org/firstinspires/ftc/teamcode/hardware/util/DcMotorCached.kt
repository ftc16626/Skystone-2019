package org.firstinspires.ftc.teamcode.hardware.util

import org.openftc.revextensions2.ExpansionHubMotor

class DcMotorCached @JvmOverloads constructor(val motor: ExpansionHubMotor, @JvmField val CACHE_THRESHOLD: Double = 0.0) {
    private var lastRead = 0.0
    
    fun setPower(power: Double) {
        if (Math.abs(lastRead - power) > CACHE_THRESHOLD || power == 0.0) {
            motor.power = power
            lastRead = power
        }
    }
}