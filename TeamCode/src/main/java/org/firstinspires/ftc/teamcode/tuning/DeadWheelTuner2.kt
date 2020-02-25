package org.firstinspires.ftc.teamcode.tuning

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.util.Angle
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.MovingStatistics
import org.firstinspires.ftc.teamcode.tuning.DeadWheelTuner2.DeadWheelTuner2Settings.NUM_TURNS
import org.firstinspires.ftc.teamcode.util.DashboardUtil

@Autonomous(name="Tuning - Dead Wheel Tuner 2", group="tuning")
class DeadWheelTuner2 : LinearOpMode() {
    @Config
    object DeadWheelTuner2Settings {
        @JvmField
        var NUM_TURNS = 10.0
    }

    override fun runOpMode() {
        val drive = DriveBaseMecanumOld(hardwareMap)

        telemetry.addLine("Press play to begin the track width tuner routine")

        waitForStart()

        if(isStopRequested) return

        telemetry.clearAll()
        telemetry.addLine("Running...")
        telemetry.update()

        drive.turn(Math.toRadians(360 * NUM_TURNS + 270))

        var imuHeadingAccumulator = 0.0
        var imuLastHeading = 0.0

        var deadWheelHeadingAccumulator = 0.0
        var lastDeadWheelHeading = 0.0

        while(!isStopRequested && drive.isBusy) {
            val packet = TelemetryPacket()
            val fieldOverlay = packet.fieldOverlay()

            DashboardUtil.drawRobot(fieldOverlay, Pose2d(0.0, 0.0, drive.poseEstimate.heading))

            val imuHeading = drive.rawExternalHeading
            imuHeadingAccumulator += Angle.norm(imuHeading- imuLastHeading)
            imuLastHeading = imuHeading

            val deadWheelHeading = drive.poseEstimate.heading
            deadWheelHeadingAccumulator += Angle.norm(deadWheelHeading - lastDeadWheelHeading)
            lastDeadWheelHeading = deadWheelHeading

            packet.put("imu", Math.toDegrees(imuHeadingAccumulator))
            packet.put("heading", Math.toDegrees(deadWheelHeadingAccumulator))

            FtcDashboard.getInstance().sendTelemetryPacket(packet)

            drive.update()
        }
    }

}