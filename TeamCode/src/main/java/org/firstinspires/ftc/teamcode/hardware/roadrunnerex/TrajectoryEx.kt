package org.firstinspires.ftc.teamcode.hardware.roadrunnerex

import com.acmerobotics.roadrunner.geometry.Pose2d
import com.acmerobotics.roadrunner.geometry.Vector2d
import com.acmerobotics.roadrunner.trajectory.MarkerCallback
import com.acmerobotics.roadrunner.trajectory.TemporalMarker
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder
import com.acmerobotics.roadrunner.trajectory.constraints.DriveConstraints
import org.firstinspires.ftc.teamcode.hardware.roadrunnerex.trajectorystep.*
import org.slf4j.Marker

class TrajectoryEx(
    startPose: Pose2d,
    startHeading: Double,
    val baseConstraints: DriveConstraints
) {
    val trajectoryList = mutableListOf<TrajectoryStep>()

    private var currentTrajectoryBuilder: TrajectoryBuilder? = null
    private var currentConstraints = baseConstraints

    private var reversed = false
    private var lastPose = startPose
    private var lastHeading = startPose.heading

    private var built = false

    fun lineTo(pos: Vector2d) = addPath {
        currentTrajectoryBuilder!!.lineTo(pos)
    }

    fun lineToConstantHeading(pos: Vector2d) = addPath {
        currentTrajectoryBuilder!!.lineToConstantHeading(pos)
    }

    fun lineToLinearHeading(pos: Vector2d, heading: Double) = addPath {
        currentTrajectoryBuilder!!.lineToLinearHeading(pos, heading)
    }

    fun lineToSplineHeading(pos: Vector2d, heading: Double) = addPath {
        currentTrajectoryBuilder!!.lineToSplineHeading(pos, heading)
    }

    fun strafeTo(pos: Vector2d) = addPath {
        currentTrajectoryBuilder!!.strafeTo(pos)
    }

    fun forward(distance: Double) = addPath {
        if(reversed) currentTrajectoryBuilder!!.back(distance)
        else currentTrajectoryBuilder!!.forward(distance)
    }

    fun back(distance: Double) = addPath {
        if(reversed) currentTrajectoryBuilder!!.forward(distance)
        else currentTrajectoryBuilder!!.back(distance)
    }

    fun strafeLeft(distance: Double) = addPath {
        if(reversed) currentTrajectoryBuilder!!.strafeRight(distance)
        else currentTrajectoryBuilder!!.strafeLeft(distance)
    }

    fun strafeRight(distance: Double) = addPath {
        if(reversed) currentTrajectoryBuilder!!.strafeLeft(distance)
        else currentTrajectoryBuilder!!.strafeRight(distance)
    }

    fun splineTo(pose: Pose2d) = addPath {
        val modifiedPose = Pose2d(pose.x, pose.y, pose.heading + (if(reversed) 180 else 0))
        currentTrajectoryBuilder!!.splineTo(pose)
    }

    fun splineToConstantHeading(pose: Pose2d) = addPath {
        currentTrajectoryBuilder!!.splineToConstantHeading(pose)
    }

    fun splineToLinearHeading(pose: Pose2d, heading: Double) = addPath {
        currentTrajectoryBuilder!!.splineToLinearHeading(pose, heading)
    }

    fun splineToSplineHeading(pose: Pose2d, heading: Double) = addPath {
        currentTrajectoryBuilder!!.splineToSplineHeading(pose, heading)
    }

    fun addTemporalMarker(time: Double, callback: MarkerCallback) = addPath {
        currentTrajectoryBuilder!!.addTemporalMarker(time, callback)
    }

    fun addTemporalMarker(scale: Double, offset: Double, callback: MarkerCallback) = addPath {
        currentTrajectoryBuilder!!.addTemporalMarker({ scale * it + offset }, callback)
    }

    fun addTemporalMarker(time: (Double) -> Double, callback: MarkerCallback) = addPath {
        currentTrajectoryBuilder!!.addTemporalMarker(time, callback)
    }

    fun addSpatialMarker(point: Vector2d, callback: MarkerCallback) = addPath {
        currentTrajectoryBuilder!!.addSpatialMarker(point, callback)
    }

    fun addDisplacementMarker(callback: MarkerCallback) = addPath {
        currentTrajectoryBuilder!!.addDisplacementMarker(callback)
    }

    fun addDisplacementMarker(displacement: Double, callback: MarkerCallback) = addPath {
        currentTrajectoryBuilder!!.addDisplacementMarker(displacement, callback)
    }

    fun addDisplacementMarker(scale: Double, offset: Double, callback: MarkerCallback) = addPath {
        currentTrajectoryBuilder!!.addDisplacementMarker(scale, offset, callback)
    }

    fun addDisplacemetMarker(displacement: (Double) -> Double, callback: MarkerCallback) = addPath {
        currentTrajectoryBuilder!!.addDisplacementMarker(displacement, callback)
    }

    private fun addPath(f: () -> Unit): TrajectoryEx {
        if (built) throw Exception("Trajectory is already built. Cannot add new steps.")
        if (currentTrajectoryBuilder == null) newPath()

        f()

//        lastPose = currentTrajectoryBuilder!!.currentPose!!
        lastHeading = lastPose.heading

        return this
    }

    fun turn(angle: Double): TrajectoryEx {
        if (built) throw Exception("Trajectory is already built. Cannot add new steps.")

        pushPath()
        trajectoryList.add(TurnStep(angle))

        lastHeading += angle

        return this
    }

    fun waitSeconds(seconds: Double): TrajectoryEx {
        if (built) throw Exception("Trajectory is already built. Cannot add new steps.")

        pushPath()
        trajectoryList.add(WaitStep(seconds))

        return this
    }

    fun waitFor(callback: WaitCallback): TrajectoryEx {
        if (built) throw Exception("Trajectory is already built. Cannot add new steps.")

        pushPath()
        trajectoryList.add(WaitConditionalStep(callback))

        return this
    }

    fun setMaxVelocity(maxVel: Double): TrajectoryEx {
        if (built) throw Exception("Trajectory is already built. Cannot add new steps.")

        pushPath()
        currentConstraints = DriveConstraints(
            maxVel,
            currentConstraints.maxAccel,
            currentConstraints.maxJerk,
            currentConstraints.maxAngVel,
            currentConstraints.maxAngAccel,
            currentConstraints.maxAngJerk
        )

        return this
    }

    fun setMaxAccleration(maxAccel: Double): TrajectoryEx {
        if (built) throw Exception("Trajectory is already built. Cannot add new steps.")

        pushPath()
        currentConstraints = DriveConstraints(
            currentConstraints.maxVel,
            maxAccel,
            currentConstraints.maxJerk,
            currentConstraints.maxAngVel,
            currentConstraints.maxAngAccel,
            currentConstraints.maxAngJerk
        )

        return this
    }

    fun setConstraints(constraints: DriveConstraints): TrajectoryEx {
        if (built) throw Exception("Trajectory is already built. Cannot add new steps.")

        pushPath()
        currentConstraints = constraints

        return this
    }

    fun resetConstraints(): TrajectoryEx {
        if (built) throw Exception("Trajectory is already built. Cannot add new steps.")

        pushPath()
        currentConstraints = baseConstraints

        return this
    }

    fun reverse(): TrajectoryEx {
        reversed = !reversed

        return this
    }

    fun build(): TrajectoryEx {
        pushPath()
        built = true

        return this
    }

    private fun pushPath() {
        if (currentTrajectoryBuilder != null) {
            trajectoryList.add(PathStep(currentTrajectoryBuilder!!.build()))
        }

        currentTrajectoryBuilder = null
    }

    private fun newPath() {
        if (currentTrajectoryBuilder != null) {
            pushPath()
        }

        val lastPoseModified =
            Pose2d(lastPose.x, lastPose.y, lastPose.heading + (if (reversed) 180 else 0))
        currentTrajectoryBuilder = TrajectoryBuilder(lastPoseModified, lastHeading, currentConstraints)
    }
}