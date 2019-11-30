package com.ftc16626.missioncontrol.math

import kotlin.math.max
import kotlin.math.min

class PIDController(var kP: Double, var kI: Double, var kD: Double, var kG: Double) {
    private var lastTimeStamp: Long = 0

    private var lastError = 0.0
    private var errorSum = 0.0

    private var lowerBound = 0.0
    private var upperBound = 0.0
    private var isBounded = false

    private var updated = false

    fun setBounds(lowerBound: Double, upperBound: Double) {
        this.lowerBound = lowerBound
        this.upperBound = upperBound
        this.isBounded = true
    }

    fun update(error: Double): Double {
        if (!updated) {
            lastTimeStamp = System.currentTimeMillis()
            lastError = error

            updated = true
            return 0.0
        }

        val now = System.currentTimeMillis()
        val dt = (now - lastTimeStamp)

        errorSum += 0.5 * (error + lastError) * dt
        val derivative = (error - lastError) / dt

        lastError = error
        lastTimeStamp = now

        val p = error * kP

        val d = derivative * kD

        val i = errorSum * kI

        val output = p + i + d + kG

        return if (isBounded) max(lowerBound, min(output, upperBound)) else output
    }

    fun setConstants(kP: Double, kI: Double, kD: Double, kG: Double) {
        this.kP = kP
        this.kI = kI
        this.kD = kD
        this.kG = kG
    }
}