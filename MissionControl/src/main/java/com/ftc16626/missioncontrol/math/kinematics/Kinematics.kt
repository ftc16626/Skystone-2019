package com.ftc16626.missioncontrol.math.kinematics

class Kinematics(
    val length: Double,
    val width: Double,
    val wheelRadius: Double
) {

    fun mecanumDrive(
        velocityFrontLeft: Double,
        velocityFrontRight: Double,
        velocityBackLeft: Double,
        velocityBackRight: Double
    ): DoubleArray {
        val vx =
            (velocityFrontLeft + velocityFrontRight + velocityBackLeft + velocityBackRight) * (wheelRadius / 4)
        val vy =
            (-velocityFrontLeft + velocityFrontRight + velocityBackLeft - velocityBackRight) * (wheelRadius / 4)
        val av =
            (-velocityFrontLeft + velocityFrontRight - velocityBackLeft + velocityBackRight) * (wheelRadius / (4 * (length + width)))

        return doubleArrayOf(vx, vy, av)
    }
}