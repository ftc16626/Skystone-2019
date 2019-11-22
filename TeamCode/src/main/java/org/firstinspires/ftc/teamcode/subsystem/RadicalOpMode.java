package org.firstinspires.ftc.teamcode.subsystem;

import android.util.Log;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadType;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;

public abstract class RadicalOpMode extends OpMode implements GamepadListener {

  protected SubsystemHandler subsystemHandler = new SubsystemHandler();
  protected MainHardware robot;

  private boolean initRanOnce = false;
  private boolean mountRanOnce = false;

  public void overridenInit() {

  }

  @Override
  public void init() {
//    if (!initRanOnce) {

//      initRanOnce = true;
//    }

    robot = new MainHardware(hardwareMap);

//    subsystemHandler.initLoop();

    overridenInit();
    subsystemHandler.onInit();
  }

  @Override
  public void init_loop() {
    subsystemHandler.initLoop();
  }

  @Override
  public void loop() {
    if (!mountRanOnce) {
      subsystemHandler.onMount();

      mountRanOnce = true;
    }

    subsystemHandler.update();
  }

  @Override
  public void stop() {
    subsystemHandler.onStop();
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {

  }
}
