package org.firstinspires.ftc.teamcode.testing;

import com.ftc16626.missioncontrol.MissionControl;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
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

  private MissionControl missionControl;

  private double starboardServoPos = 0;

  @Override
  public void init() {
    missionControl = ((FtcRobotControllerActivity) hardwareMap.appContext).missionControl;

    robot = new MainHardware(hardwareMap);
    driverInterface = new DriverInterface(gamepad1, gamepad2, this);
    driverInterface.driver.setProfile(missionControl.getPilotProfileHandler().getCurrentProfile());

    robot.init();
    starboardServoPos = 1;
    robot.starboardServo.setPosition(starboardServoPos);
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
    // Temporary fix because I'm too lazy to change the settings
    // TODO FIX
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

    if(driverInterface.driver.getProfile().enableFieldCentric) {
      angle += Math.toRadians(robot.imu.getGlobalHeading() % 360);
    }

    robot.drive.setAngle(angle);
    robot.drive.setPower(magnitude);
    robot.drive.setTurn(turn);

    if(driverInterface.driver.gamepad.dpad_up) {
      starboardServoPos = Math.min(starboardServoPos + 0.05, 1);
      robot.starboardServo.setPosition(starboardServoPos);
    } else if(driverInterface.driver.gamepad.dpad_down) {
      starboardServoPos = Math.max(starboardServoPos - 0.05, 0);
      robot.starboardServo.setPosition(starboardServoPos);
    }

    telemetry.addData("Servo pos", starboardServoPos);

    robot.update();

    telemetry.addData("Current Profile", driverInterface.driver.getProfile().name);
    telemetry.addData("Invert X", driverInterface.driver.getProfile().invertStrafeStickX);
    telemetry.addData("Invert Y", driverInterface.driver.getProfile().invertStrafeStickY);
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if (gamepadType == GamepadType.DRIVER) {
      if (eventType == GamepadEventType.BUTTON_PRESSED) {
        switch (eventName) {
          case START:
            missionControl.getPilotProfileHandler().incremementPosition();
            driverInterface.driver.setProfile(missionControl.getPilotProfileHandler().getCurrentProfile());
            break;
          case X:
            robot.intake.toggle();
            break;
          case A:
            robot.intake.reverse();
            break;
        }
      }
    }
  }
}
