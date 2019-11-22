package org.firstinspires.ftc.teamcode.testing;

import com.ftc16626.missioncontrol.MissionControl;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadType;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.firstinspires.ftc.teamcode.gamepadextended.DriverInterface;
import org.openftc.revextensions2.ExpansionHubEx;
import org.openftc.revextensions2.ExpansionHubMotor;
import org.openftc.revextensions2.RevBulkData;

@TeleOp(name = "Gamepad Driver Aid Teleop", group = "Testing")
@Disabled
public class GamepadDriverAidTestTeleop extends OpMode implements GamepadListener {

  RevBulkData bulkData;
  RevBulkData bulkData2;
  ExpansionHubEx expansionHub;

  private MainHardware robot;
  private DriverInterface driverInterface;

  private MissionControl missionControl;

  private boolean isBackServoDown = false;

  private boolean inited = false;

  private int sliderPos = 0;
  private final int maxSlider = 7000;

  private int sliderInitOffset = 0;

  @Override
  public void init() {
    missionControl = ((FtcRobotControllerActivity) hardwareMap.appContext).missionControl;

    robot = new MainHardware(hardwareMap);
    driverInterface = new DriverInterface(gamepad1, gamepad2, this);
    driverInterface.driver.setProfile(missionControl.getPilotProfileHandler().getCurrentProfile());

    telemetry.addData("Status", "Initialized");

    expansionHub = robot.expansionHubMain;

    sliderInitOffset = robot.expansionHubDaughter.getBulkInputData().getMotorCurrentPosition(robot.motorSlider);
  }

  @Override
  public void loop() {
    if (!inited) {
      robot.init();
      robot.swingyServo.setPosition(1);

      inited = true;
    }

    driverInterface.update();

    bulkData = expansionHub.getBulkInputData();
    bulkData2 = robot.expansionHubDaughter.getBulkInputData();

    handleControlDriving();
    handleControlStarboardServo();
    handleSliderMotor();

    robot.update();

    telemetry.addData("Distance", robot.sliderRange.getDistance(DistanceUnit.MM));
    telemetry.addData("Current Profile", driverInterface.driver.getProfile().name);
    telemetry.addData("Invert X", driverInterface.driver.getProfile().invertStrafeStickX);
    telemetry.addData("Invert Y", driverInterface.driver.getProfile().invertStrafeStickY);
//    telemetry.addData("Vel 1", robot.drive.motorVelFrontLeft);

//    telemetry.addData("ENcoder 0", bulkData.getMotorCurrentPosition(robot.drive.motorFrontLeft));
//    telemetry.addData("ENcoder 1", bulkData.getMotorCurrentPosition(robot.drive.motorFrontRight));
//    telemetry.addData("ENcoder 2", bulkData.getMotorCurrentPosition(robot.drive.motorBackLeft));
//    telemetry.addData("ENcoder 3", bulkData.getMotorCurrentPosition(robot.drive.motorBackRight));

    telemetry.addData("Pos X", robot.drive.getPosition().getX());
    telemetry.addData("Pos Y", robot.drive.getPosition().getY());
    telemetry.addData("Heading", Math.toDegrees(robot.drive.getHeading()));
  }

  private void handleControlDriving() {
    double magnitude = 0;
    double angle = 0;
    double turn = 0;

    double realMag = driverInterface.driver.getStrafeStickMagnitude() / 2;
    double realAngle = driverInterface.driver.getStrafeStickAngle();
    double realTurn = driverInterface.driver.getTurnStickX() / 2;

    if (driverInterface.driver.gamepad.right_trigger > 0.7) {
      turn = realTurn;
      magnitude = realMag;
      angle = realAngle;
    } else if (driverInterface.driver.getTurnStickX() != 0) {
      turn = realTurn;
    } else {
      magnitude = realMag;
      angle = realAngle;
    }

    if (driverInterface.driver.gamepad.left_trigger > 0.7) {
      double deg = Math.toDegrees(realAngle);
      double rounded = Math.round(deg / 45) * 45;
      angle = Math.toRadians(rounded);
    }

    if (driverInterface.driver.gamepad.left_bumper) {
      if (magnitude != 0) {
        magnitude *= 2;
      }
      if (turn != 0) {
        turn *= 2;
      }
    } else if (driverInterface.driver.gamepad.right_bumper) {
//      if (magnitude != 0) {
//        magnitude /= 4;
//      }
    } else {
//      magnitude *= 1 - driverInterface.driver.gamepad.left_trigger;
    }

//    if (driverInterface.driver.getProfile().enableFieldCentric) {
//      angle += Math.toRadians(robot.imu.getGlobalHeading() % 360);
//      telemetry.addData("field centric", "true " + angle);
//    } else {
//      telemetry.addData("field centric", "false " + angle);
//    }

    robot.drive.setAngle(angle);
    robot.drive.setPower(magnitude);
    robot.drive.setTurn(turn);
  }

  private void handleControlStarboardServo() {
//    if (driverInterface.aid.gamepad.dpad_up) {
//      robot.raiseStarboardServo();
//    } else if (driverInterface.aid.gamepad.dpad_down) {
//      robot.lowerStarboardServo();
//    }
//
//    telemetry.addData("Servo pos", robot.getStarboardServoPos());
  }

  private void handleSliderMotor() {
    int currentPos = bulkData2.getMotorCurrentPosition(robot.motorSlider) - sliderInitOffset;
    double distance = robot.sliderRange.getDistance(DistanceUnit.MM);

    if (driverInterface.aid.gamepad.left_stick_y != 0) {
      float stick = driverInterface.aid.gamepad.left_stick_y;
//      telemetry.addData("TEST", stick);
//      if (stick < 0 && currentPos > -6000) {
//        robot.motorSlider.setPower(stick);
//      } else if(stick > 0 && currentPos < 0){
//        robot.motorSlider.setPower(stick);
//      }
      if(stick < 0 && distance < 580) {
        robot.motorSlider.setPower(stick);
      } else if(stick > 0 && distance > 20) {
        robot.motorSlider.setPower(stick);
      } else {
        robot.motorSlider.setPower(0);
      }
//        robot.motorSlider.setPower(stick);
    } else {
      robot.motorSlider.setPower(0);
    }

    sliderPos += driverInterface.aid.gamepad.left_stick_y;
    sliderPos = Math.max(0, sliderPos);
    sliderPos = Math.min(sliderPos, maxSlider);
//    robot.motorSlider.setTargetPosition(sliderPos);

//    int currentPos = Math.abs(bulkData2.getMotorCurrentPosition(robot.motorSlider));
//    if(currentPos > sliderPos) {
//      robot.motorSlider.setPower(0.6);
//    } else if(currentPos < sliderPos) {
//      robot.motorSlider.setPower(-0.6);
//    } else {
//      robot.motorSlider.setPower(0);
//    }

    telemetry.addData("Slider pos", currentPos);
  }

  private void toggleBackServo() {
    isBackServoDown = !isBackServoDown;

    if (isBackServoDown) {
      robot.backRightServo.setPosition(0);
    } else {
      robot.backRightServo.setPosition(1);
    }
  }

  @Override
  public void actionPerformed(
      GamepadEventName eventName,
      GamepadEventType eventType,
      GamepadType gamepadType
  ) {
    if (gamepadType == GamepadType.DRIVER) {
      if (eventType == GamepadEventType.BUTTON_PRESSED) {
        switch (eventName) {
          case START:
            missionControl.getPilotProfileHandler().incremementPosition();
            driverInterface.driver
                .setProfile(missionControl.getPilotProfileHandler().getCurrentProfile());
            break;
        }
      }
    } else {
      if (eventType == GamepadEventType.BUTTON_PRESSED) {
        switch (eventName) {
          case LEFT_BUMPER:
//            if(robot.intake.isMotorOn) robot.intake.toggle(false);
//            robot.intake.toggle();
            if (!robot.intake.isMotorOn) {
              robot.intake.directionBackward();
              robot.intake.toggle(true);
            } else if (robot.intake.power > 1) {
              robot.intake.directionBackward();
            } else {
              robot.intake.toggle(false);
            }
            break;
          case RIGHT_BUMPER:
//            if(robot.intake.isMotorOn) robot.intake.toggle(false);
//            else robot.intake.toggle(true);
            if (!robot.intake.isMotorOn) {
              robot.intake.directionForward();
              robot.intake.toggle(true);
            } else if (robot.intake.power < 1) {
              robot.intake.directionForward();
            } else {
              robot.intake.toggle(false);
            }

            robot.intake.directionForward();
//            robot.intake.reverse();
            break;
//          case B:
//            robot.intake.toggleIntakeOpen();
//            break;
          case Y:
            toggleBackServo();
            break;
        }
      }
    }
  }
}
