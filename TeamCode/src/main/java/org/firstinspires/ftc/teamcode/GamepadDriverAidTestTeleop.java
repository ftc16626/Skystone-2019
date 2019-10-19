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

@TeleOp(name = "Gamepad Driver Aid Teleop", group = "Testing")
public class GamepadDriverAidTestTeleop extends OpMode implements GamepadListener {

  private MainHardware robot;
  private DriverInterface driverInterface;

  private GamepadProfile enzoProfile = new GamepadProfile(
      "Enzo's Profile",
      StickControl.STRAFE_LEFT_TURN_RIGHT_STICK,
      false, false,
      true, false,
      StickResponseCurve.CUBED,
      false);
  private GamepadProfile mattProfile = new GamepadProfile(
      "Matt's Profile",
      StickControl.STRAFE_RIGHT_TURN_LEFT_STICK,
      true, false,
      true, false,
      StickResponseCurve.CUBED,
      false);
  private GamepadProfile emilioProfile = new GamepadProfile(
      "Emilio's Profile",
      StickControl.STRAFE_LEFT_TURN_RIGHT_STICK,
      true, false,
      true, false,
      StickResponseCurve.RAW,
      false);
  private GamepadProfile emilioProfileCentricProfile = new GamepadProfile(
      "Emilio's Profile But Field Centric",
      StickControl.STRAFE_LEFT_TURN_RIGHT_STICK,
      true, false,
      true, false,
      StickResponseCurve.RAW,
      true);

  private GamepadProfile[] ProfileList = new GamepadProfile[]{enzoProfile, mattProfile, emilioProfile, emilioProfileCentricProfile};
  private int currentProfilePos = 0;

  @Override
  public void init() {
    robot = new MainHardware(hardwareMap);
    driverInterface = new DriverInterface(gamepad1, gamepad2, this);
    driverInterface.driver.setProfile(ProfileList[currentProfilePos]);

    telemetry.addData("Status", "Initialized");
  }

  @Override
  public void loop() {
    driverInterface.update();

    double magnitude = 0;
    double angle = 0;
    double turn = 0;

    double realMag = driverInterface.driver.getStrafeStickMagnitude();
    double realAngle = driverInterface.driver.getStrafeStickAngle();
    double realTurn = driverInterface.driver.getTurnStickX();

    if (driverInterface.driver.gamepad.right_bumper) {
      turn = realMag;
      magnitude = realMag;
      angle = realAngle;
    } else if(driverInterface.driver.getTurnStickX() != 0) {
      turn = realTurn;
    } else {
      magnitude = realMag;
      angle = realAngle;
    }

    if(driverInterface.driver.gamepad.left_bumper) {
      double deg = Math.toDegrees(realAngle);
      double rounded = Math.round(deg / 45) * 45;
      angle = Math.toRadians(rounded);
    }

    if(driverInterface.driver.gamepad.b) {
      if(magnitude != 0) {
        magnitude /= 2;
      }
    }

    if(ProfileList[currentProfilePos].enableFieldCentric) {
      angle += Math.toRadians(robot.imu.getGlobalHeading() % 360);
    }

    robot.drive.setAngle(angle);
    robot.drive.setPower(magnitude);
    robot.drive.setTurn(turn);

    robot.update();

    telemetry.addData("Current Profile", ProfileList[currentProfilePos].name);
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if (gamepadType == GamepadType.DRIVER) {
      if (eventType == GamepadEventType.BUTTON_PRESSED) {
        switch (eventName) {
          case START:
            incrementProfile();
            break;
        }
      }
    }
  }

  private void incrementProfile() {
    currentProfilePos++;
    if (currentProfilePos >= ProfileList.length) {
      currentProfilePos = 0;
    }
    driverInterface.driver.setProfile(ProfileList[currentProfilePos]);
  }
}
