package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.gamepadextended.GamepadProfile;
import org.firstinspires.ftc.teamcode.gamepadextended.GamepadProfile.StickControl;
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

  private GamepadProfile enzoProfile = new GamepadProfile(
      "Enzo's profile",
      StickControl.STRAFE_LEFT_TURN_RIGHT_STICK,
      false, false,
      false, false,
      StickResponseCurve.CUBED,
      false);
  private GamepadProfile mattProfile = new GamepadProfile(
      "Matt's profile",
      StickControl.STRAFE_RIGHT_TURN_LEFT_STICK,
      true, false,
      false, false,
      StickResponseCurve.CUBED,
      false);
  private GamepadProfile emilioProfile = new GamepadProfile(
      "Emilio's profile",
      StickControl.STRAFE_LEFT_TURN_RIGHT_STICK,
      true, false,
      false, false,
      StickResponseCurve.CUBED,
      false);

  private GamepadProfile[] profileList = new GamepadProfile[]{enzoProfile, mattProfile, emilioProfile};
  private int currentProfilePos = 0;

  @Override
  public void init() {
    robot = new MainHardware(hardwareMap);
    driverInterface = new DriverInterface(gamepad1, gamepad2, this);
    driverInterface.driver.setProfile(profileList[currentProfilePos]);

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

    telemetry.addData("Current profile", profileList[currentProfilePos].name);
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if (gamepadType == GamepadType.DRIVER) {
      if (eventType == GamepadEventType.BUTTON_PRESSED) {
        switch (eventName) {
          case START:
            incrementprofile();
            break;
        }
      }
    }
  }

  private void incrementprofile() {
    currentProfilePos++;
    if (currentProfilePos >= profileList.length) {
      currentProfilePos = 0;
    }
    driverInterface.driver.setProfile(profileList[currentProfilePos]);
  }

}
