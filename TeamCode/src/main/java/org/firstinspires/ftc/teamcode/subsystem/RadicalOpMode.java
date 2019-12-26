package org.firstinspires.ftc.teamcode.subsystem;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.hardware.Robot;

public class RadicalOpMode extends LinearOpMode {

  protected SubsystemHandler subsystemHandler = new SubsystemHandler();
  protected Robot robot;

  protected void onInit() { }

  protected void initLoop() { }

  protected void onMount() { }

  protected void update() { }

  protected void onStop() { }

  @Override
  public void runOpMode() throws InterruptedException {
    robot = new Robot(hardwareMap, this);
    subsystemHandler.add(robot);

    onInit();
    subsystemHandler.onInit();

    while(!isStarted() && !isStopRequested()) {
      subsystemHandler.initLoop();
      initLoop();
    }

    subsystemHandler.onMount();
    onMount();

    while(opModeIsActive() && !isStopRequested()) {
      subsystemHandler.update();
      update();
    }

    subsystemHandler.onStop();
    onStop();
  }
}
