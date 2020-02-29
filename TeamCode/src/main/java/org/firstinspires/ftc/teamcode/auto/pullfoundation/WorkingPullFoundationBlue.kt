package org.firstinspires.ftc.teamcode.auto.pullfoundation

import android.util.Log
import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.canvas.Canvas
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.MarkerCallback
import com.acmerobotics.roadrunner.trajectory.Trajectory
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.auto.RoadRunnerBaseOpmode
import org.firstinspires.ftc.teamcode.hardware.roadrunner.DriveConstants
import org.firstinspires.ftc.teamcode.tuning.DriveBaseMecanumOld
import org.firstinspires.ftc.teamcode.util.DashboardUtil
import java.util.*

@Autonomous
class WorkingPullFoundationBlue : RoadRunnerBaseOpmode() {
    lateinit var driveOld: DriveBaseMecanumOld

    lateinit var trajectory1: Trajectory
    lateinit var trajectory2: Trajectory

    override fun onInit() {
        super.onInit()

        robot.subsystemLift.turnOff()
        robot.subsystemVirtual4Bar.turnOff()

        driveOld = DriveBaseMecanumOld(robot.hwMap)
        driveOld.poseEstimate = Pose2d(38.0, 63.0, Math.toRadians(90.0))

        trajectory1 = TrajectoryBuilder(
            // Start coordinate
            Pose2d(38.0, 63.0, Math.toRadians(90.0 + 180)),
            Math.toRadians(90.0),
            DriveConstants.BASE_CONSTRAINTS
        )
            // First target coordinate to drive up to the foundation
            .lineTo(
                // Coordinate
                Vector2d(42.0, 32.0), DriveConstraints(
                    20.0, 40.0, 0.0,
                    Math.toRadians(180.0),
                    Math.toRadians(180.0),
                    0.0
                )
            )
                // Change the offset number if you want to drop the grabber earlier or later
                // Negative numbers == earlier
            .addDisplacementMarker(1.0, -2.0, MarkerCallback {
                // Drop foundation grabber
                robot.subsystemFoundationGrabber.drop()
                // Wait half a second and then follow next trajectory
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        driveOld.followTrajectory(trajectory2)
                    }
                    // Half a second delay
                }, 500)
            })
            .build()

        // Next trajectory
        trajectory2 = TrajectoryBuilder(
            trajectory1.end(), trajectory1.end().heading,
            DriveConstants.BASE_CONSTRAINTS
        )
            // Next target (going back to the wall)
            .lineTo(Vector2d(38.0, 63.0))
            .addDisplacementMarker(MarkerCallback {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        // Turn
                        driveOld.turnSync(Math.toRadians(120.0))
                        // raise foundation grabber
                        robot.subsystemFoundationGrabber.raise()

                        val trajectory3 = TrajectoryBuilder(
                            Pose2d(),
                            0.0,
                            DriveConstants.BASE_CONSTRAINTS
                        )
//                            .strafeLeft(15.0)
                            // Move forward 40 inches and left 30 inches
                            .lineTo(Vector2d(40.0, 30.0))
                            .build()

                        driveOld.followTrajectory(trajectory3)
                    }
                }, 500)
            })
            .build()
//                Timer().schedule(object : TimerTask() {
//                    override fun run() {
//                        driveOld.turnSync(Math.toRadians(90.0))
//                        robot.subsystemFoundationGrabber.raise()
//
//                        val trajectory2 = TrajectoryBuilder(
//                            driveOld.poseEstimate,
//                            driveOld.poseEstimate.heading,
//                            DriveConstants.BASE_CONSTRAINTS
//                        )
//                            .lineTo(Vector2d(0.0, 37.0))
//                            .build()
//                    }
//                }, 500)
//            })
//            .addTemporalMarker(1.0, 0.0, MarkerCallback {
//                robot.subsystemFoundationGrabber.drop()
//                Handler().postDelayed({
//                    driveOld.turnSync(Math.toRadians(90.0))
//                }, 500)
//                Timer().schedule(object : TimerTask() {
//                    override fun run() {
//                        driveOld.turnSync(Math.toRadians(90.0))
//                    }
//                }, 500)

//                Handler().postDelayed({
//                    driveOld.followTrajectory(
//
//                    )
//                }, 2500)
//            })
    }

    override fun onMount() {
        super.onMount()

        driveOld.poseEstimate = Pose2d(38.0, 63.0, Math.toRadians(90.0))
        driveOld.followTrajectory(trajectory1)
    }

    override fun update() {
        driveOld.update()

        val packet =
            TelemetryPacket()

        val fieldOverlay: Canvas = packet.fieldOverlay()
        fieldOverlay.setStroke("#F44336")
        DashboardUtil.drawRobot(
            fieldOverlay,
            driveOld.poseEstimate
        )

        FtcDashboard.getInstance().sendTelemetryPacket(packet)

    }
}