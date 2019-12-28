package org.firstinspires.ftc.teamcode.auto.justpark;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.auto.RoadRunnerBaseOpmode;

@Autonomous(name="Just Park Wall - Blue/Red")
public class AutoOpParkWall extends RoadRunnerBaseOpmode {
  @Override
  public void onMount() {
    robot.subsystemFoundationGrabber.drop();
  }
}
