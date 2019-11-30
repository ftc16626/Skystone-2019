package com.ftc16626.missioncontrol.math

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration
import org.firstinspires.ftc.robotcore.external.navigation.Position
import org.firstinspires.ftc.robotcore.external.navigation.Velocity

data class Vector3(val x: Double = 0.0, val y: Double = 0.0, val z: Double = 0.0) {
    constructor(accel: Acceleration): this(accel.xAccel, accel.yAccel, accel.zAccel)
    constructor(vel: Velocity): this(vel.xVeloc, vel.yVeloc, vel.zVeloc)
    constructor(pos: Position): this(pos.x, pos.y, pos.z)
}
