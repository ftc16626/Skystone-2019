package org.firstinspires.ftc.teamcode.gamepadextended;

public class GamepadConfig {
  public final StickControl controlScheme;
  public final boolean invertStrafeStickX;
  public final boolean invertStrafeStickY;
  public final boolean invertTurnStickX;
  public final boolean invertTurnStickY;

  public GamepadConfig(
      StickControl controlScheme,
      boolean invertStrafeStickX, boolean invertStrafeStickY,
      boolean invertTurnStickX, boolean invertTurnStickY) {

    this.controlScheme = controlScheme;
    this.invertStrafeStickX = invertStrafeStickX;
    this.invertStrafeStickY = invertStrafeStickY;
    this.invertTurnStickX = invertTurnStickX;
    this.invertTurnStickY = invertTurnStickY;
  }

  public static enum StickControl {
    STRAFE_RIGHT_TURN_LEFT_STICK,
    STRAFE_LEFT_TURN_RIGHT_STICK
  }
}
