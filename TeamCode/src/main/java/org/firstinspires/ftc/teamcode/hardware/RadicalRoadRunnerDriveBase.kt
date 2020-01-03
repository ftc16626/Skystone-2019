package org.firstinspires.ftc.teamcode.hardware

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.control.PIDCoefficients
import com.acmerobotics.roadrunner.control.PIDFController
import com.acmerobotics.roadrunner.drive.DriveSignal
import com.acmerobotics.roadrunner.drive.MecanumDrive
import com.acmerobotics.roadrunner.followers.HolonomicPIDVAFollower
import com.acmerobotics.roadrunner.followers.TrajectoryFollower
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.profile.MotionProfile
import com.acmerobotics.roadrunner.profile.MotionProfileGenerator
import com.acmerobotics.roadrunner.profile.MotionState
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import com.acmerobotics.roadrunner.trajectory.constraints.MecanumConstraints
import com.acmerobotics.roadrunner.util.NanoClock
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.util.CompetitionStatus
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveConstants
import org.firstinspires.ftc.teamcode.util.DashboardUtil
import org.firstinspires.ftc.teamcode.hardware.roadrunner.RadicalMecanumLocalizer

class RadicalRoadRunnerDriveBase(val robot: Robot) : MecanumDrive(
    DriveConstants.kV,
    DriveConstants.kA,
    DriveConstants.kStatic,
    DriveConstants.TRACK_WIDTH
) {
    @Config
    object PIDConstants {
        @JvmField
        var TRANSLATIONAL_PID = PIDCoefficients(0.0, 0.0, 0.0)
        @JvmField
        var HEADING_PID = PIDCoefficients(0.0, 0.0, 0.0)
    }

    private val IN_TO_MM = 0.393701

    private enum class Mode {
        IDLE,
        TURN,
        FOLLOW_TRAJECTORY
    }

    private var dashboard: FtcDashboard? = null
    private val clock: NanoClock

    private var mode: Mode

    private val turnController: PIDFController
    private var turnProfile: MotionProfile? = null
    private var turnStart = 0.0

    private val constraints: DriveConstraints
    private val follower: TrajectoryFollower

    private val lastWheelPositions = mutableListOf<Double>()
    private var lastTimestamp = 0.0

    init {
        if (CompetitionStatus.IN_COMPETITION) {
            dashboard = FtcDashboard.getInstance()
            dashboard?.telemetryTransmissionInterval = 1
        }

        clock = NanoClock.system()

        mode =
            Mode.IDLE

        turnController = PIDFController(PIDConstants.HEADING_PID)
        turnController.setInputBounds(0.0, 2 * Math.PI)

        constraints =
            MecanumConstraints(
                DriveConstants.BASE_CONSTRAINTS,
                DriveConstants.TRACK_WIDTH
            )
        follower = HolonomicPIDVAFollower(
            PIDConstants.TRANSLATIONAL_PID,
            PIDConstants.TRANSLATIONAL_PID,
            PIDConstants.HEADING_PID
        )

        localizer =
            RadicalMecanumLocalizer(
                this,
                true
            )
    }

    fun trajectoryBuilder(): TrajectoryBuilder {
        return TrajectoryBuilder(poseEstimate, poseEstimate.heading, constraints)
    }

    fun turn(angle: Double) {
        val heading = poseEstimate.heading

        turnProfile = MotionProfileGenerator.generateSimpleMotionProfile(
            MotionState(heading, 0.0, 0.0, 0.0),
            MotionState(heading + angle, 0.0, 0.0, 0.0),
            constraints.maxAngVel,
            constraints.maxAngAccel,
            constraints.maxAngJerk
        )

        turnStart = clock.seconds()
        mode =
            Mode.TURN
    }

    fun turnSync(angle: Double) {
        turn(angle)
        waitForIdle()
    }

    fun turnSync(angle: Double, callback: () -> Unit) {
        turn(angle)
        waitForIdle()
        callback()
    }

    fun followTrajectory(trajectory: Trajectory) {
        follower.followTrajectory(trajectory)
        mode =
            Mode.FOLLOW_TRAJECTORY
    }

    fun followTrajectorySync(trajectory: Trajectory) {
        followTrajectory(trajectory)
        waitForIdle()
    }

    fun getLastError(): Pose2d {
        return when (mode) {
            Mode.FOLLOW_TRAJECTORY -> follower.lastError
            Mode.TURN -> Pose2d(0.0, 0.0, turnController.lastError)
            Mode.IDLE -> Pose2d()
        }
    }

    fun update() {
        updatePoseEstimate()

        val currentPose = poseEstimate
        val lastError = getLastError()

        var packet: TelemetryPacket? = null
        var fieldOverlay: Canvas? = null
        if (!CompetitionStatus.IN_COMPETITION) {
            packet = TelemetryPacket()
            fieldOverlay = packet.fieldOverlay()

            packet.put("mode", mode)

            packet.put("x", currentPose.x)
            packet.put("y", currentPose.y)
            packet.put("heading", currentPose.heading)

            packet.put("xError", lastError.x)
            packet.put("yError", lastError.y)
            packet.put("headingError", lastError.heading)
        }

        when (mode) {
            Mode.IDLE -> return
            Mode.TURN -> {
                val t = clock.seconds() - turnStart

                val targetState = turnProfile!![t]

                turnController.targetPosition = targetState.x

                val targetOmega = targetState.v
                val targetAlpha = targetState.a
                val correction = turnController.update(currentPose.heading, targetOmega)

                setDriveSignal(
                    DriveSignal(
                        Pose2d(0.0, 0.0, targetOmega + correction),
                        Pose2d(0.0, 0.0, targetAlpha)
                    )
                )

                if (t >= turnProfile!!.duration()) {
                    mode =
                        Mode.IDLE
                    setDriveSignal(DriveSignal())
                }
            }
            Mode.FOLLOW_TRAJECTORY -> {
                setDriveSignal(follower.update(currentPose))

                val trajectory = follower.trajectory

                if (!CompetitionStatus.IN_COMPETITION) {
                    fieldOverlay?.setStrokeWidth(1)
                    fieldOverlay?.setStroke("#4CAF50")
                    DashboardUtil.drawSampledPath(fieldOverlay, trajectory.path)

                    fieldOverlay?.setStroke("#F44336")

                    val t = follower.elapsedTime()
                    DashboardUtil.drawRobot(fieldOverlay, trajectory[t])

                    fieldOverlay?.setStroke("#3F51B5")
                    fieldOverlay?.fillCircle(
                        currentPose.x * IN_TO_MM,
                        currentPose.y * IN_TO_MM,
                        3.0
                    )

                    if (!follower.isFollowing()) {
                        mode =
                            Mode.IDLE
                        setDriveSignal(DriveSignal())
                    }
                }
            }
        }

        if (!CompetitionStatus.IN_COMPETITION) dashboard?.sendTelemetryPacket(packet)
    }

    fun waitForIdle() {
        while (!Thread.currentThread().isInterrupted && isBusy()) {
            update()
        }
    }

    fun isBusy(): Boolean {
        return mode != Mode.IDLE
    }

    fun getPIDCoefficients(runMode: DcMotor.RunMode): PIDCoefficients {
        return robot.subsystemDriveTrainMecanum.getPIDCoefficients(runMode)
    }

    fun setPIDCoefficients(runMode: DcMotor.RunMode, coefficients: PIDCoefficients) {
        robot.subsystemDriveTrainMecanum.setPIDCoefficients(runMode, coefficients)
    }

    override fun getWheelPositions(): List<Double> {
        val bulkData = robot.bulkDataMother ?: return listOf(0.0, 0.0, 0.0, 0.0)

        return robot.subsystemDriveTrainMecanum.motors.map {
            DriveConstants.encoderTicksToInches(
                bulkData.getMotorCurrentPosition(it).toDouble()
            )
        }
    }

    fun getWheelVelocities(): List<Double> {
        val bulkData = robot.bulkDataMother ?: return listOf(0.0, 0.0, 0.0, 0.0)

        return robot.subsystemDriveTrainMecanum.motors.map {
            DriveConstants.encoderTicksToInches(
                bulkData.getMotorVelocity(it).toDouble()
            )
        }
    }

    override fun setMotorPowers(
        frontLeft: Double,
        rearLeft: Double,
        rearRight: Double,
        frontRight: Double
    ) {
        val motors = robot.subsystemDriveTrainMecanum.motors
        motors[0].power = frontLeft
        motors[1].power = frontRight
        motors[2].power = rearRight
        motors[3].power = rearLeft
    }

    override val rawExternalHeading: Double
        get() = robot.subsystemIMU.heading
}