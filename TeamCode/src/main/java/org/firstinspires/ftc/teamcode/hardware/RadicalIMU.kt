package org.firstinspires.ftc.teamcode.hardware

import com.ftc16626.missioncontrol.math.imu.PositionIntegrator
import com.ftc16626.missioncontrol.math.Vector3
import com.qualcomm.hardware.bosch.BNO055IMU
import com.qualcomm.hardware.bosch.BNO055IMU.Parameters
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration
import org.firstinspires.ftc.robotcore.external.navigation.Orientation
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference
import java.util.Locale

class RadicalIMU(var imu: BNO055IMU, useMagnetometer: Boolean) {
    private val parameters: BNO055IMU.Parameters = Parameters()

    private var useMagnetometer = false

    private var lastOrientation: Orientation? = null
    private var lastHeading = 0.0
    var globalHeading = 0.0

    var acceleration = Acceleration()

    // Start new integrator with blank vectors
    private val positionIntegrator = PositionIntegrator(
        Vector3(),
        Vector3(),
        Vector3()
    )

    // Calibration
    var isCalibrating = false
    private var startCalibratingSensorBias = 0.0
    private var calibrationDuration = 5000.0

    private val calibAccelXSamples = mutableListOf<Double>()
    private val calibAccelYSamples = mutableListOf<Double>()
    private val calibAccelZSamples = mutableListOf<Double>()

    var biasAccelXMax: Double = 0.0
    var biasAccelYMax: Double = 0.0
    var biasAccelZMax: Double = 0.0

    var biasAccelXAvg: Double = 0.0
    var biasAccelYAvg: Double = 0.0
    var biasAccelZAvg: Double = 0.0

    val subtractBias = true
    val thresholdBias = true

    init {
        parameters.mode = if (useMagnetometer)
            BNO055IMU.SensorMode.NDOF
        else
            BNO055IMU.SensorMode.IMU // NDOF uses magnetometer
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC
        parameters.calibrationDataFile = "RadicalIMUCalibration.json"
        //    parameters.accelerationIntegrationAlgorithm = integrator;
        //    parameters.loggingEnabled = true;
        //    parameters.loggingTag = "IMU";

        this.useMagnetometer = useMagnetometer

        lastOrientation = Orientation()
        imu.initialize(parameters)
        //    imu.startAccelerationIntegration(initialPosition, initialVelocity, 100);
    }

    fun update() {
        if (isCalibrating) {
            if (System.currentTimeMillis() - startCalibratingSensorBias > calibrationDuration) {
                biasAccelXAvg = calibAccelXSamples.average()
                biasAccelYAvg = calibAccelYSamples.average()
                biasAccelZAvg = calibAccelZSamples.average()

                biasAccelXMax = calibAccelXSamples.max() ?: 0.0
                biasAccelYMax = calibAccelYSamples.max() ?: 0.0
                biasAccelZMax = calibAccelZSamples.max() ?: 0.0

                isCalibrating = false
            }

            val currAccel = imu.linearAcceleration
            calibAccelXSamples.add(currAccel.xAccel)
            calibAccelYSamples.add(currAccel.yAccel)
            calibAccelZSamples.add(currAccel.zAccel)

            return
        }

        // Global heading
        val headingNow = this.heading.toDouble()
        var deltaAngle = headingNow - lastHeading

        if (deltaAngle < -180) {
            deltaAngle += 360.0
        } else if (deltaAngle > 180) {
            deltaAngle -= 360.0
        }

        globalHeading += deltaAngle

        lastHeading = headingNow

        // Acceleration
        val accel = imu.linearAcceleration
        val nowTime = System.currentTimeMillis()

        if (subtractBias) {
            accel.xAccel = accel.xAccel - biasAccelXAvg
            accel.yAccel = accel.yAccel - biasAccelYAvg
            accel.zAccel = accel.zAccel - biasAccelZAvg
        }

        if (thresholdBias) {
            accel.xAccel = if (accel.xAccel > biasAccelXMax) accel.xAccel else 0.0
            accel.yAccel = if (accel.yAccel > biasAccelYMax) accel.yAccel else 0.0
            accel.zAccel = if (accel.zAccel > biasAccelZMax) accel.zAccel else 0.0
        }

        acceleration = Acceleration(
            accel.unit,
            accel.xAccel,
            accel.yAccel,
            accel.zAccel,
            accel.acquisitionTime
        )

        // Integrate
        positionIntegrator.update(Vector3(acceleration), nowTime.toDouble())
    }

    fun calibrateSensorBias(milliseconds: Double) {
        isCalibrating = true
        calibrationDuration = milliseconds
        startCalibratingSensorBias = System.currentTimeMillis().toDouble()
    }

    fun resetAngle() {
        lastOrientation = angles

        globalHeading = 0.0
    }

    fun formatAngleString(angleUnit: AngleUnit, angle: Double): String {
        return formatDegreesString(AngleUnit.DEGREES.fromUnit(angleUnit, angle))
    }

    fun formatDegreesString(degrees: Double): String {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees))
    }

    fun getVelocity(): Vector3 {
        return positionIntegrator.currentVel
    }

    fun getPosition(): Vector3 {
        return positionIntegrator.currentPos
    }

    val angles: Orientation
        get() = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES)

    val isCalibrated: Boolean
        get() {

            var magnetometerCalibrated = true
            if (useMagnetometer) {
                magnetometerCalibrated = imu.isMagnetometerCalibrated
            }

            return (imu.isAccelerometerCalibrated && imu.isGyroCalibrated && magnetometerCalibrated
                    && imu.isSystemCalibrated)
        }

    val isAccelCalibrated: Boolean
        get() = imu.isAccelerometerCalibrated

    val isGyroCalibrated: Boolean
        get() = imu.isGyroCalibrated

    val isMagCalibrated: Boolean
        get() = if (useMagnetometer) {
            imu.isMagnetometerCalibrated
        } else {
            true
        }

    val isSysCalibrated: Boolean
        get() = imu.isSystemCalibrated

    val systemStatus: String
        get() = imu.systemStatus.toString()

    val calibrationStatus: String
        get() = imu.calibrationStatus.toString()

    val heading: Float
        get() = angles.firstAngle

    val headingString: String
        get() = formatAngleString(angles.angleUnit, angles.firstAngle.toDouble())

    val roll: Float
        get() = angles.secondAngle

    val rollString: String
        get() = formatAngleString(angles.angleUnit, angles.firstAngle.toDouble())

    val pitch: Float
        get() = angles.thirdAngle

    val pitchString: String
        get() = formatAngleString(angles.angleUnit, angles.thirdAngle.toDouble())

    val gravity: Acceleration
        get() = imu.gravity
}
