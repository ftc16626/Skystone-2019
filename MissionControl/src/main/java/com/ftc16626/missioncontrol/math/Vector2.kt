package com.ftc16626.missioncontrol.math

import kotlin.math.cos
import kotlin.math.sin

data class Vector2(val x: Double = 0.0, val y: Double = 0.0) {
    fun rotate(angle: Double): Vector2 {
        return Vector2((x * cos(angle)) - (y * sin(angle)), (x * sin(angle)) - (y * cos(angle)))
    }

    operator fun unaryMinus(): Vector2 {
        return Vector2(-x, -y)
    }

    operator fun plus(vec2: Vector2): Vector2 {
        return Vector2(x + vec2.x, y + vec2.y)
    }

    operator fun minus(vec2: Vector2): Vector2 {
        return Vector2(x - vec2.x, y - vec2.y)
    }

    operator fun times(scalar: Double): Vector2 {
        return Vector2(x * scalar, y * scalar)
    }

    operator fun div(scalar: Double): Vector2 {
        return Vector2(x * scalar, y * scalar)
    }
}