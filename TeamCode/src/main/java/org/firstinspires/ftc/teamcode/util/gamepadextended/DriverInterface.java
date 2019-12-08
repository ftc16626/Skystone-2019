package org.firstinspires.ftc.teamcode.util.gamepadextended;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadType;

public class DriverInterface {
  public GamepadExtended driver;
  public GamepadExtended aid;

  public DriverInterface(Gamepad driver, Gamepad aid) {
    this.driver = new GamepadExtended(driver, GamepadType.DRIVER);
    this.aid = new GamepadExtended(aid, GamepadType.AID);
  }

  public DriverInterface(Gamepad driver, Gamepad aid, GamepadListener listener) {
    this.driver = new GamepadExtended(driver, GamepadType.DRIVER);
    this.aid = new GamepadExtended(aid, GamepadType.AID);

    this.driver.setListener(listener);
    this.aid.setListener(listener);
  }

  public void update() {
    this.driver.update();
    this.aid.update();
  }

  public void switchGamepads() {
    GamepadExtended temp = driver;
    this.driver = aid;
    this.aid = temp;
  }

  public void setDriver(Gamepad gamepad) {
    this.driver = new GamepadExtended(gamepad, GamepadType.DRIVER);
  }

  public void setDriver(GamepadExtended gamepadExtended) {
    this.driver = gamepadExtended;
  }

  public void setAid(Gamepad gamepad) {
    this.aid = new GamepadExtended(gamepad, GamepadType.AID);
  }

  public void setAid(GamepadExtended gamepadExtended) {
    this.aid = gamepadExtended;
  }
}
