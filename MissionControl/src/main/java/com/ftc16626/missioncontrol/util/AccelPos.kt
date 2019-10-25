package com.ftc16626.missioncontrol.util

import java.util.*

class AccelPos(
    private val initialAccel: Vector3,
    private val initialVel: Vector3,
    private val initialPos: Vector3
) {
    private var lastAccel: Vector3 = Vector3(0.0, 0.0, 0.0)
    private var lastVel: Vector3 = Vector3(0.0, 0.0, 0.0)
    private var lastPos: Vector3 = Vector3(0.0, 0.0, 0.0)

    var currentAccel: Vector3 = initialAccel
    var currentVel: Vector3 = initialVel
    var currentPos: Vector3 = initialPos

    private var updatedFirstTime = false
    private var lastTime: Double = 0.0

    fun update(accel: Vector3, now: Double) {
        if (!updatedFirstTime) {
            lastTime = now
            updatedFirstTime = true
        }

        val timeDelta = lastTime - now

        integrateAccel(accel, timeDelta)
        integratePos(currentVel, timeDelta)

        lastTime = now
    }

    private fun integrateAccel(accel: Vector3, timeDelta: Double) {
        currentAccel = accel

        currentVel = Vector3(
            lastVel.x + (lastAccel.x + ((currentAccel.x - lastAccel.x) / 2)) * timeDelta,
            lastVel.y + (lastAccel.y + ((currentAccel.y - lastAccel.y) / 2)) * timeDelta,
            lastVel.z + (lastAccel.z + ((currentAccel.z - lastAccel.z) / 2)) * timeDelta
        )
    }

    private fun integratePos(vel: Vector3, timeDelta: Double) {
        currentVel = vel

        currentPos = Vector3(
            lastPos.x + (lastVel.x + ((currentVel.x - lastVel.x) / 2)) * timeDelta,
            lastPos.y + (lastVel.y + ((currentVel.y - lastVel.y) / 2)) * timeDelta,
            lastPos.z + (lastVel.z + ((currentVel.z - lastVel.z) / 2)) * timeDelta
        )
    }
}