package org.firstinspires.ftc.teamcode.subsystem;

public abstract class Subsystem {

  boolean on = true;

  abstract void update();
  abstract void transitionToUpdate();

  abstract void initLoop();
  abstract void init();

  Subsystem turnOn() {
    on = true;
    return this;
  }
  Subsystem turnOff() {
    on = false;
    return this;
  }
}
