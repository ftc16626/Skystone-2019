package org.firstinspires.ftc.teamcode.gamepadextended;

import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.teamcode.gamepadextended.listener.GamepadListener;

public class DriverInterface {
  public GamepadExtended driver;
  public GamepadExtended aid;

  public DriverInterface(Gamepad driver, Gamepad aid) {
    this.driver = new GamepadExtended(driver);
    this.aid = new GamepadExtended(aid);
  }

  public DriverInterface(Gamepad driver, Gamepad aid, GamepadListener listener) {
    this.driver = new GamepadExtended(driver);
    this.aid = new GamepadExtended(aid);

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
    this.driver = new GamepadExtended(gamepad);
  }

  public void setDriver(GamepadExtended gamepadExtended) {
    this.driver = gamepadExtended;
  }

  public void setAid(Gamepad gamepad) {
    this.aid = new GamepadExtended(gamepad);
  }

  public void setAid(GamepadExtended gamepadExtended) {
    this.aid = gamepadExtended;
  }
}
