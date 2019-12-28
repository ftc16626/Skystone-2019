package org.firstinspires.ftc.teamcode.auto.justpark;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.auto.RoadRunnerBaseOpmode;

@Autonomous(name="Just Park Bridge Strafe - Right")
public class AutoOpJustParkBridgeRight extends RoadRunnerBaseOpmode {
  private Trajectory trajectory;

  private final double DISTANCE_BACK = 0;
  private final double DISTANCE_RIGHT = 450;
  private final double DELAY = 0;

  private ElapsedTime elapsedTime = new ElapsedTime();
  private boolean ran = false;

  @Override
  public void onInit() {
    trajectory = drive.trajectoryBuilder()
        .back(DISTANCE_BACK)
        .strafeRight(DISTANCE_RIGHT)
        .build();
  }

  @Override
  public void onMount() {
    elapsedTime.reset();
  }

  @Override
  public void update() {
    if(elapsedTime.seconds() - DELAY > 0 && !ran) {
      drive.followTrajectory(trajectory);
      ran = true;
    }
  }
}
