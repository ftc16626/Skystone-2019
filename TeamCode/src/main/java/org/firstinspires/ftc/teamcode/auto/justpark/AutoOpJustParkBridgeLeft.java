package org.firstinspires.ftc.teamcode.auto.justpark;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.auto.RoadRunnerBaseOpmode;

@Autonomous(name = "Just Park Bridge Strafe - Left")
public class AutoOpJustParkBridgeLeft extends RoadRunnerBaseOpmode {

  private Trajectory trajectory;

  private final double DISTANCE_BACK = 0;
  private final double DISTANCE_LEFT = 29;
  private final double DELAY = 0;

  private ElapsedTime elapsedTime = new ElapsedTime();
  private boolean ran = false;

  @Override
  public void onMount() {
    TrajectoryBuilder builder = drive.trajectoryBuilder();

    if (DISTANCE_BACK != 0) {
      builder.back(DISTANCE_BACK);
    }

    if (DISTANCE_LEFT != 0) {
      builder.strafeLeft(DISTANCE_LEFT);
    }

    builder.addDisplacementMarker(() -> robot.subsystemFoundationGrabber.drop());

    trajectory = builder.build();

    elapsedTime.reset();
  }

  @Override
  public void update() {
    if (elapsedTime.seconds() - DELAY > 0 && !ran) {
      drive.followTrajectory(trajectory);
      ran = true;
    }

//    drive.update();
  }
}
