package org.firstinspires.ftc.teamcode.hardware

import com.qualcomm.hardware.bosch.BNO055IMU
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import org.firstinspires.ftc.robotcore.external.navigation.Orientation

class SimpleIMU(var imu: BNO055IMU) {
    private val parameters = BNO055IMU.Parameters()

    private var angles = Orientation()
    private var lastHeading = 0.0
    var globalHeading = 0.0

    private val useMagnetometer = false

    init {
        parameters.mode = if (useMagnetometer)
            BNO055IMU.SensorMode.NDOF // NDOF uses magnetometer
        else
            BNO055IMU.SensorMode.IMU

        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
        parameters.calibrationDataFile = "RadicalIMUCalibration.json"

        imu.initialize(parameters)
    }

    fun update() {
        angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES)

        // Global heading
        val headingNow = this.heading.toDouble()
        var deltaAngle = headingNow - lastHeading

        if(deltaAngle < -180) {
            deltaAngle += 360.0
        } else if(deltaAngle > 180) {
            deltaAngle -= 360.0
        }

        globalHeading += deltaAngle
        lastHeading = headingNow
    }

    val heading: Float
        get() = angles.firstAngle

    val roll: Float
        get() = angles.secondAngle

    val pitch: Float
        get() = angles.thirdAngle
}