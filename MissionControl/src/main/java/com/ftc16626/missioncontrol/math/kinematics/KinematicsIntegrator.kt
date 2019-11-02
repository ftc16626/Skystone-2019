package com.ftc16626.missioncontrol.math.kinematics

import com.ftc16626.missioncontrol.math.Vector2

class KinematicsIntegrator(private val initialPos: Vector2) {
    var currentPos = Vector2()

    private var updatedFirstTime = false
    private var lastTime: Double = 0.0

    fun update(vel: Vector2, angle: Double, now: Double) {
        if(!updatedFirstTime) {
            lastTime = now
            updatedFirstTime = true
        }

        val timeDelta = lastTime - now

        currentPos += vel.rotate(angle) * timeDelta

        lastTime = now
    }
}