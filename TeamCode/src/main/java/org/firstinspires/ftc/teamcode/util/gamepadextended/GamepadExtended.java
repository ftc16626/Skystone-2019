package org.firstinspires.ftc.teamcode.util.gamepadextended;

import com.ftc16626.missioncontrol.util.profiles.PilotProfile;
import com.ftc16626.missioncontrol.util.profiles.StickControl;
import com.ftc16626.missioncontrol.util.profiles.StickResponseCurve;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadType;

public class GamepadExtended {

  public Gamepad gamepad;
  public GamepadType type;

  public GamepadExtended(Gamepad gamepad, GamepadType type) {
    this.gamepad = gamepad;
    this.type = type;
  }

  private PilotProfile profile = new PilotProfile(
      "Blank Profile",
      StickControl.STRAFE_LEFT_TURN_RIGHT_STICK,
      true, false,
      true, false,
      StickResponseCurve.CUBED,
      false
  );

  private GamepadListener listener = null;
  private final int BUTTONS_TO_COUNT = 15;
  private boolean[] buttonPastValues = new boolean[BUTTONS_TO_COUNT];
  private boolean[] buttonValues = new boolean[BUTTONS_TO_COUNT];
  private GamepadEventName[] eventNameList = GamepadEventName.values();

  public double getStrafeStickMagnitude() {
    return Math.min(Math.hypot(getStrafeStickX(), getStrafeStickY()), 1);
  }

  public double getTurnStickMagnitude() {
    return Math.min(Math.hypot(getTurnStickX(), getTurnStickY()), 1);
  }

  public double getStrafeStickAngle() {
    return Math.atan2(getStrafeStickY(), getStrafeStickX()) - Math.PI / 4;
  }

  public double getTurnStickAngle() {
    return Math.atan2(getTurnStickY(), getTurnStickX()) - Math.PI / 4;
  }

  public float getStrafeStickX() {
    float stickX = profile.controlScheme == StickControl.STRAFE_LEFT_TURN_RIGHT_STICK
        ? gamepad.left_stick_x : gamepad.right_stick_x;

    if (!profile.invertStrafeStickX) {
      stickX *= -1;
    }

    if(profile.stickResponseCurve == StickResponseCurve.CUBED) {
      stickX = (float) Math.pow(stickX, 3);
    }

    return stickX;
  }

  public float getStrafeStickY() {
    float stickY = profile.controlScheme == StickControl.STRAFE_LEFT_TURN_RIGHT_STICK
        ? gamepad.left_stick_y : gamepad.right_stick_y;

    if (!profile.invertStrafeStickY) {
      stickY *= -1;
    }

    if(profile.stickResponseCurve == StickResponseCurve.CUBED) {
      stickY = (float) Math.pow(stickY, 3);
    }

    return stickY;
  }

  public float getTurnStickX() {
    float stickX = profile.controlScheme == StickControl.STRAFE_LEFT_TURN_RIGHT_STICK
        ? gamepad.right_stick_x : gamepad.left_stick_x;

    if (!profile.invertTurnStickX) {
      stickX *= -1;
    }

    if(profile.stickResponseCurve == StickResponseCurve.CUBED) {
      stickX = (float) Math.pow(stickX, 3);
    }

    return stickX;
  }

  public float getTurnStickY() {
    float stickY = profile.controlScheme == StickControl.STRAFE_LEFT_TURN_RIGHT_STICK
        ? gamepad.right_stick_y : gamepad.left_stick_y;

    if (!profile.invertTurnStickY) {
      stickY *= -1;
    }

    if(profile.stickResponseCurve == StickResponseCurve.CUBED) {
      stickY = (float) Math.pow(stickY, 3);
    }

    return stickY;
  }

  public void setProfile(PilotProfile profile) {
    if(profile != null) this.profile = profile;
  }

  public PilotProfile getProfile() {
    return this.profile;
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

    for(int i = 0; i < BUTTONS_TO_COUNT; i++) {
      if(buttonValues[i] && !buttonPastValues[i]) {
        listener.actionPerformed(eventNameList[i], GamepadEventType.BUTTON_PRESSED, type);

        buttonPastValues[i] = true;
      } else if(!buttonValues[i] && buttonPastValues[i]) {
        listener.actionPerformed(eventNameList[i], GamepadEventType.BUTTON_RELEASED, type);

        buttonPastValues[i] = false;
      }
    }
  }
}
