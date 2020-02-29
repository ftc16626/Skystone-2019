package org.firstinspires.ftc.teamcode.teleop.subsystem;

import android.util.Log;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.hardware.subsystem.SubsystemLift;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.firstinspires.ftc.teamcode.util.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadType;

public class SubsystemTeleLift extends Subsystem implements GamepadListener {

  private final Robot robot;
  private final DriverInterface driverInterface;

  private double speed = 15;
  private double downSpeed = 60;

  private int position = 0;

  private int blockEncoderCount = 200;

  private int targetStage = 0;
  private final int MAX_LEVEL = 9;

  public SubsystemTeleLift(Robot robot, DriverInterface driverInterface) {
    this.driverInterface = driverInterface;
    driverInterface.addListener(this);

    this.robot = robot;
  }

  @Override
  public void onInit() {
    robot.subsystemLift.setPosition(0);
  }

  @Override
  public void update() {
    double leftStick = -driverInterface.aid.gamepad.left_stick_y;
    if (leftStick > 0) {
      position += leftStick * speed;
    } else if (leftStick < 0) {
      position += leftStick * downSpeed;
    }

    position = Range.clip(position, 0, SubsystemLift.MAX_HEIGHT);
    robot.subsystemLift.setPosition(position);
  }

  private int getStateTargetTick(int stage) {
    Log.i("stages", Integer.toString(stage));

    // Stage 1 is actually block 5 since you dont need to use the lift for the 5th block
    if (stage == 1) {
      return 120;
    } else if (stage == 2) {
      return 315;
    } else if (stage == 3) {
      return 476;
    } else if (stage == 4) {
      return 659;
    } else if (stage == 5) {
      return 798;
    } else if (stage == 6) {
      return 986;
    } else if (stage == 7) {
      return 1131;
    } else if (stage == 8) {
      return 1337;
    } else if (stage == 9) {
      return 1558;
    }

    return 0;
  }


  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if (gamepadType == GamepadType.AID && eventType == GamepadEventType.BUTTON_PRESSED) {
//      if(eventName == GamepadEventName.DPAD_UP) {
//        position = Range.clip(position + blockEncoderCount, 0, SubsystemLift.MAX_HEIGHT);
//      } else if(eventName == GamepadEventName.DPAD_DOWN) {
//        position = Range.clip(position - blockEncoderCount, 0, SubsystemLift.MAX_HEIGHT);
//      }

      if (eventName == GamepadEventName.DPAD_UP) {
        targetStage++;
        targetStage = Range.clip(targetStage, 0, MAX_LEVEL);
        position = getStateTargetTick(targetStage);
      } else if (eventName == GamepadEventName.DPAD_DOWN) {
        targetStage--;
        targetStage = Range.clip(targetStage, 0, MAX_LEVEL);
        position = getStateTargetTick(targetStage);
      }

      if (eventName == GamepadEventName.Y) {
        position = getStateTargetTick(targetStage);
      }

      if (eventName == GamepadEventName.DPAD_LEFT) {
        robot.subsystemLift.adjustZeroOffset(-20);
      }

      if (eventName == GamepadEventName.LEFT_STICK_BUTTON) {
        position = 0;
      }
    }
  }
}
