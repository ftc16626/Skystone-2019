package org.firstinspires.ftc.teamcode.gamepadextended;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.gamepadextended.GamepadConfig.StickControl;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadType;

public class GamepadExtended extends Gamepad {

  private GamepadConfig config = new GamepadConfig(
      StickControl.STRAFE_LEFT_TURN_RIGHT_STICK,
      false, false,
      false, false
  );

  private GamepadListener listener = null;
  private final int buttonsToCount = 15;
  private boolean[] buttonPastValues = new boolean[buttonsToCount];
  private boolean[] buttonValues = new boolean[buttonsToCount];
  private GamepadEventName[] eventNameList = GamepadEventName.values();

  public double getMagnitudeStrafeStick() {
    return Math.min(Math.hypot(getStrafeStickX(), getStrafeStickY()), 1);
  }

  public double getMagnitudeTurnStick() {
    return Math.min(Math.hypot(getTurnStickX(), getTurnStickY()), 1);
  }

  public double getAngleStrafeStick() {
    return Math.atan2(getStrafeStickX(), getStrafeStickX()) - Math.PI / 4;
  }

  public double getAngleTurnStick() {
    return Math.atan2(getTurnStickX(), getTurnStickY()) - Math.PI / 4;
  }

  public float getStrafeStickX() {
    float stickX = config.controlScheme == StickControl.STRAFE_LEFT_TURN_RIGHT_STICK
        ? left_stick_x : right_stick_x;

    if (config.invertStrafeStickX) {
      stickX *= -1;
    }

    return stickX;
  }

  public float getStrafeStickY() {
    float stickY = config.controlScheme == StickControl.STRAFE_LEFT_TURN_RIGHT_STICK
        ? left_stick_y : right_stick_y;

    if (config.invertStrafeStickY) {
      stickY *= -1;
    }

    return stickY;
  }

  public float getTurnStickX() {
    float stickX = config.controlScheme == StickControl.STRAFE_LEFT_TURN_RIGHT_STICK
        ? right_stick_x : left_stick_x;

    if (config.invertTurnStickX) {
      stickX *= -1;
    }

    return stickX;
  }

  public float getTurnStickY() {
    float stickY = config.controlScheme == StickControl.STRAFE_LEFT_TURN_RIGHT_STICK
        ? right_stick_y : left_stick_y;

    if (config.invertTurnStickY) {
      stickY *= -1;
    }

    return stickY;
  }

  public void setConfig(GamepadConfig config) {
    this.config = config;
  }

  public void setListener(GamepadListener listener) {
    this.listener = listener;
  }

  public void update() {
    if(listener == null) return;

    buttonValues[0] = dpad_up;
    buttonValues[1] = dpad_down;
    buttonValues[2] = dpad_left;
    buttonValues[3] = dpad_right;
    buttonValues[4] = a;
    buttonValues[5] = b;
    buttonValues[6] = x;
    buttonValues[7] = y;
    buttonValues[8] = guide;
    buttonValues[9] = start;
    buttonValues[10] = back;
    buttonValues[11] = left_bumper;
    buttonValues[12] = right_bumper;
    buttonValues[13] = left_stick_button;
    buttonValues[14] = right_stick_button;

    for(int i = 0; i < buttonsToCount; i++) {
      if(buttonValues[i] && !buttonPastValues[i]) {
        listener.actionPerformed(eventNameList[i], GamepadEventType.BUTTON_PRESSED, GamepadType.DRIVER);

        buttonPastValues[i] = true;
      } else if(!buttonValues[i] && buttonPastValues[i]) {
        listener.actionPerformed(eventNameList[i], GamepadEventType.BUTTON_RELEASED, GamepadType.DRIVER);

        buttonPastValues[i] = false;
      }
    }
  }
}
