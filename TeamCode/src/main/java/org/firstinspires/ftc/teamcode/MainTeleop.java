package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.gamepadextended.GamepadConfig;
import org.firstinspires.ftc.teamcode.gamepadextended.GamepadConfig.StickControl;
import org.firstinspires.ftc.teamcode.gamepadextended.StickResponseCurve;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadType;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.firstinspires.ftc.teamcode.gamepadextended.DriverInterface;

@TeleOp(name = "Main TeleOp", group = "Mecanum")
public class MainTeleop extends OpMode implements GamepadListener {

  private MainHardware robot;
  private DriverInterface driverInterface;

  private GamepadConfig enzoConfig = new GamepadConfig(
      "Enzo's Config",
      StickControl.STRAFE_LEFT_TURN_RIGHT_STICK,
      false, false,
      false, false,
      StickResponseCurve.CUBED);
  private GamepadConfig mattConfig = new GamepadConfig(
      "Matt's Config",
      StickControl.STRAFE_RIGHT_TURN_LEFT_STICK,
      true, false,
      false, false,
      StickResponseCurve.CUBED);
  private GamepadConfig emilioConfig = new GamepadConfig(
      "Emilio's Config",
      StickControl.STRAFE_LEFT_TURN_RIGHT_STICK,
      true, false,
      false, false,
      StickResponseCurve.CUBED);

  private GamepadConfig[] configList = new GamepadConfig[]{enzoConfig, mattConfig, emilioConfig};
  private int currentConfigPos = 0;

  @Override
  public void init() {
    robot = new MainHardware(hardwareMap);
    driverInterface = new DriverInterface(gamepad1, gamepad2, this);
    driverInterface.driver.setConfig(configList[currentConfigPos]);

    telemetry.addData("Status", "Initialized");
  }

  @Override
  public void loop() {
    driverInterface.update();

    double magnitude = driverInterface.driver.getStrafeStickMagnitude();
    double angle = driverInterface.driver.getStrafeStickAngle();
    double turn = driverInterface.driver.getTurnStickX();

    robot.drive.setAngle(angle);
    robot.drive.setPower(magnitude);
    robot.drive.setTurn(turn);

    robot.update();

    telemetry.addData("Current Config", configList[currentConfigPos].name);
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if (gamepadType == GamepadType.DRIVER) {
      if (eventType == GamepadEventType.BUTTON_PRESSED) {
        switch (eventName) {
          case START:
            incrementConfig();
            break;
        }
      }
    }
  }

  private void incrementConfig() {
    currentConfigPos++;
    if (currentConfigPos >= configList.length) {
      currentConfigPos = 0;
    }
    driverInterface.driver.setConfig(configList[currentConfigPos]);
  }

}
