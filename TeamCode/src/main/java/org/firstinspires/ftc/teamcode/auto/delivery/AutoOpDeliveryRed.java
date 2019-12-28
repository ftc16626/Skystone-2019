package org.firstinspires.ftc.teamcode.auto.delivery;

import org.firstinspires.ftc.teamcode.auto.RoadRunnerBaseOpmode;
import org.firstinspires.ftc.teamcode.auto.SubsystemVision;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;

public class AutoOpDeliveryRed extends RoadRunnerBaseOpmode {
  private boolean visionReady = false;
  private boolean pathSet = false;

  private SubsystemVision subsystemVision;
  private Subsystem pathLeft, pathMiddle, pathRight;

  public AutoOpDeliveryRed() {
    super();

    subsystemVision = new SubsystemVision(robot, this);

    pathLeft = new DeliveryPathRedLeft(robot,this).turnOff();
    pathMiddle = new DeliveryPathRedMiddle(robot, this).turnOff();
    pathRight = new DeliveryPathRedRight(robot, this).turnOff();

    subsystemHandler.add(subsystemVision);
    subsystemHandler.add(pathLeft);
    subsystemHandler.add(pathMiddle);
    subsystemHandler.add(pathRight);
  }

  @Override
  public void initLoop() {
    if(!visionReady && subsystemVision.pipeline.getDetectedSkystonePosition() != -1) {
      visionReady = true;
    }
  }

  @Override
  public void onMount() {
    if(visionReady) {
      setPath();
    }
  }

  @Override
  public void update() {
    if(!visionReady && subsystemVision.pipeline.getDetectedSkystonePosition() != -1) {
      visionReady = true;

      setPath();
    }
  }

  private void setPath() {
    // ensure that path is only set once in case the vision thing changes
    // so you don't have multiple paths clashing
    if(pathSet) return;

    switch(subsystemVision.pipeline.getDetectedSkystonePosition()) {
      case 0:
        pathLeft.turnOn();
        break;
      case 1:
        pathMiddle.turnOn();
        break;
      case 2:
        pathRight.turnOn();
        break;
    }

    pathSet = true;
  }
}
