package org.firstinspires.ftc.teamcode.tuning

import android.util.Log
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.util.Angle
import com.acmerobotics.roadrunner.util.Angle.norm
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.MovingStatistics
import org.firstinspires.ftc.robotcore.internal.system.Misc
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveConstants
import org.firstinspires.ftc.teamcode.tuning.DeadWheelTuner.DeadWheelTunerSettings.ANGLE
import org.firstinspires.ftc.teamcode.tuning.DeadWheelTuner.DeadWheelTunerSettings.DELAY
import org.firstinspires.ftc.teamcode.tuning.DeadWheelTuner.DeadWheelTunerSettings.NUM_TRIALS
import org.firstinspires.ftc.teamcode.util.DashboardUtil
import kotlin.math.abs

@Autonomous(name = "Tuning - Dead Wheel Tuner", group = "tuning")
class DeadWheelTuner : LinearOpMode() {

    @Config
    object DeadWheelTunerSettings {
        @JvmField
        var NUM_TRIALS = 5
        @JvmField
        var ANGLE = 180.0
        @JvmField
        var DELAY = 1000
    }

    override fun runOpMode() {
        val drive =
            DriveBaseMecanumOld(hardwareMap)

        telemetry.addLine("Press play to begin the track width tuner routine")
        telemetry.addLine("Make sure your robot has enough clearance to turn smoothly")
        telemetry.update()

        waitForStart()

        if (isStopRequested) return

        telemetry.clearAll()
        telemetry.addLine("Running...")
        telemetry.update()

        val trackWidthStats = MovingStatistics(NUM_TRIALS)

        var deadWheelHeadingAccumulator = 0.0
        var deadWheelLastHeading = 0.0

        var imuHeadingAccumulator = 0.0
        var imuLastHeading = 0.0

        for (i in 0 until NUM_TRIALS) {
//            drive.poseEstimate = Pose2d()
            // it is important to handle heading wraparounds
//            var imuHeadingAccumulator = 0.0
//            var imuLastHeading = 0.0


            drive.turn(Math.toRadians(ANGLE))

            while (!isStopRequested && drive.isBusy) {
                val imuHeading = drive.rawExternalHeading

                imuHeadingAccumulator += norm(imuHeading - imuLastHeading)
                imuLastHeading = imuHeading

                Log.i(
                    "IMUHEADING",
                    java.lang.Double.toString(Math.toDegrees(imuHeadingAccumulator))
                )

                val deadWheelHeading = drive.poseEstimate.heading
                val delta = deadWheelHeading - deadWheelLastHeading

                if(abs(delta) > 180) {
//                    delta

                }
                deadWheelHeadingAccumulator += Angle.normDelta(delta)
//                deadWheelHeadingAccumulator += norm(deadWheelHeading - deadWheelLastHeading)
                deadWheelLastHeading = deadWheelHeading

                Log.i(
                    "DEADWHEELHEADING",
                    java.lang.Double.toString(Math.toDegrees(deadWheelHeadingAccumulator))
                )

                Log.i("HEADING", "----------------------")

                Log.i(
                    "RATIO", (deadWheelHeadingAccumulator / imuHeadingAccumulator).toString()
                )

                val packet = TelemetryPacket()
                val fieldOverlay = packet.fieldOverlay()

                DashboardUtil.drawRobot(fieldOverlay, Pose2d(0.0, 0.0, drive.poseEstimate.heading))

                packet.put("imuangle", Math.toDegrees(imuHeadingAccumulator))
                packet.put("deadwheel", Math.toDegrees(deadWheelHeadingAccumulator))
                packet.put("ratio", deadWheelHeadingAccumulator / imuHeadingAccumulator)
//                packet.put("heading", drive.poseEstimate.heading)
//                packet.put("x", drive.poseEstimate.x)
//                packet.put("y", drive.poseEstimate.y)
                FtcDashboard.getInstance().sendTelemetryPacket(packet);

                drive.update()
            }

            val trackWidth =
                deadWheelHeadingAccumulator / imuHeadingAccumulator

            trackWidthStats.add(trackWidth)
            sleep(DELAY.toLong())
        }

        telemetry.clearAll()
        telemetry.addLine("Tuning complete")
        telemetry.addLine(
            Misc.formatInvariant(
                "Effective track width = %.2f (SE = %.3f)",
                trackWidthStats.mean,
                trackWidthStats.standardDeviation / Math.sqrt(NUM_TRIALS.toDouble())
            )
        )
        telemetry.update()

        while (!isStopRequested) {
            idle()
        }
    }
}

