package com.ftc16626.missioncontrol.math.kinematics

import com.ftc16626.missioncontrol.math.Vector2

class KinematicsIntegrator(var currentPos: Vector2, var currentHeading: Double) {
    private var updatedFirstTime = false
    private var lastTime: Double = 0.0

    fun update(vel: Vector2, angle: Double, now: Double) {
        if(!updatedFirstTime) {
            lastTime = now
            updatedFirstTime = true
        }

        val timeDelta = (now - lastTime) / 1000

        currentHeading += angle * timeDelta * 0.94

//        currentPos += vel * timeDelta
        currentPos += vel.rotate(currentHeading) * timeDelta

        lastTime = now
    }
}