package org.firstinspires.ftc.teamcode.hardware.util

import android.util.Log
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.util.Range

open class GhostServo
@JvmOverloads constructor(
    val servo: Servo,
    @JvmField val min: Double = 0.0,
    @JvmField val max: Double = 1.0,
    @JvmField val rotationSpeed: Double = 0.25, // Seconds per 60 degrees
    @JvmField val sweepAngle: Double = 300.0, // Angle that 0-1 travels
    @JvmField val scaleSpeed: Double = 1.0, // Scale the effective by
    @JvmField val reversed: Boolean = false
) {

    var DEBUG = false

    init {
        servo.scaleRange(min, max)
        if(reversed) servo.direction = Servo.Direction.REVERSE
    }

    private var currentPosition = 0.0
    private var targetPosition = 0.0
    private var currentMode = Modes.IDLE

    private val constrainedSweep = (max - min) * sweepAngle
//     How many seconds it takes to travel the constrained sweep
    private val realRotationSpeed = constrainedSweep / 60 * rotationSpeed * scaleSpeed

    private val arbitraryCurrentPositionThreshold = 0.05

    private val timer = ElapsedTime()
    private var lastTime = 0.0

    fun update() {
        val currentTime = timer.seconds()
//        if(Math.abs(targetPosition - currentPosition) < arbitraryCurrentPositionThreshold) {
//            currentMode = Modes.IDLE
//            currentPosition = targetPosition
//        }

        if(currentMode == Modes.TRAVELING) {
            if(targetPosition > currentPosition) {
                currentPosition += realRotationSpeed * (currentTime - lastTime)
            } else {
                currentPosition -= realRotationSpeed * (currentTime - lastTime)
            }
        }

//        currentPosition = Range.clip(currentPosition, 0.0, 1.0)

        if(DEBUG) {
            val packet = TelemetryPacket()
            packet.put("targetpos", targetPosition)
            packet.put("currentpos", currentPosition)

            FtcDashboard.getInstance().sendTelemetryPacket(packet)
            Log.i("pos", targetPosition.toString() + "   " + currentPosition.toString())
        }

        lastTime = currentTime
    }

    fun setPosition(pos: Double) {
        servo.position = pos

        targetPosition = pos
        currentMode = Modes.TRAVELING
    }

    fun getEstimate(): Double {
        return currentPosition
    }

    fun isMoving(): Boolean {
        return currentMode == Modes.TRAVELING
    }

    private enum class Modes {
        IDLE,
        TRAVELING
    }
}