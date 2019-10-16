package org.firstinspires.ftc.teamcode.gamepadextended;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.gamepadextended.GamepadConfig.StickControl;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadType;

public class GamepadExtended {

  private Gamepad gamepad;

  public GamepadExtended(Gamepad gamepad) {
    this.gamepad = gamepad;
  }

  private GamepadConfig config = new GamepadConfig(
      "",
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
        ? gamepad.left_stick_x : gamepad.right_stick_x;

    if (config.invertStrafeStickX) {
      stickX *= -1;
    }

    return stickX;
  }

  public float getStrafeStickY() {
    float stickY = config.controlScheme == StickControl.STRAFE_LEFT_TURN_RIGHT_STICK
        ? gamepad.left_stick_y : gamepad.right_stick_y;

    if (config.invertStrafeStickY) {
      stickY *= -1;
    }

    return stickY;
  }

  public float getTurnStickX() {
    float stickX = config.controlScheme == StickControl.STRAFE_LEFT_TURN_RIGHT_STICK
        ? gamepad.right_stick_x : gamepad.left_stick_x;

    if (config.invertTurnStickX) {
      stickX *= -1;
    }

    return stickX;
  }

  public float getTurnStickY() {
    float stickY = config.controlScheme == StickControl.STRAFE_LEFT_TURN_RIGHT_STICK
        ? gamepad.right_stick_y : gamepad.left_stick_y;

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

    buttonValues[0] = gamepad.dpad_up;
    buttonValues[1] = gamepad.dpad_down;
    buttonValues[2] = gamepad.dpad_left;
    buttonValues[3] = gamepad.dpad_right;
    buttonValues[4] = gamepad.a;
    buttonValues[5] = gamepad.b;
    buttonValues[6] = gamepad.x;
    buttonValues[7] = gamepad.y;
    buttonValues[8] = gamepad.guide;
    buttonValues[9] = gamepad.start;
    buttonValues[10] = gamepad.back;
    buttonValues[11] = gamepad.left_bumper;
    buttonValues[12] = gamepad.right_bumper;
    buttonValues[13] = gamepad.left_stick_button;
    buttonValues[14] = gamepad.right_stick_button;

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
