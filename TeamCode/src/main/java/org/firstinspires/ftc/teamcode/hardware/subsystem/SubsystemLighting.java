package org.firstinspires.ftc.teamcode.hardware.subsystem;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver.BlinkinPattern;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;

public class SubsystemLighting extends Subsystem {

  private RevBlinkinLedDriver blinkinLedDriver;
  private RevBlinkinLedDriver.BlinkinPattern TELEOP_PATTERN_SLOW = BlinkinPattern.CP2_BREATH_SLOW;
  private RevBlinkinLedDriver.BlinkinPattern TELEOP_PATTERN_FAST = BlinkinPattern.CP2_BREATH_FAST;
  private RevBlinkinLedDriver.BlinkinPattern ENDGAME_PATTERN = BlinkinPattern.CP1_HEARTBEAT_FAST;
//  private RevBlinkinLedDriver.BlinkinPattern CAPSTONE_PATTERN = BlinkinPattern.CP1_2_BEATS_PER_MINUTE;
  private RevBlinkinLedDriver.BlinkinPattern CAPSTONE_PATTERN = BlinkinPattern.BEATS_PER_MINUTE_PARTY_PALETTE;

  private State currentState = State.TELEOP;

  private ElapsedTime gameTimer = new ElapsedTime();

  private final double TELEOP_FAST_TIME = 60;
  private final double ENDGAME_TIME = 90;

  private boolean setTeleopFast = false;
  private boolean setEndgame = false;

  public SubsystemLighting(Robot robot, RadicalOpMode opMode) {
    blinkinLedDriver = robot.hwMap.get(RevBlinkinLedDriver.class, "blinkin");
    blinkinLedDriver.setPattern(TELEOP_PATTERN_SLOW);
  }

  @Override
  public void onMount() {
    blinkinLedDriver.setPattern(TELEOP_PATTERN_SLOW);
    currentState = State.TELEOP;
  }

  @Override
  public void update() {
    if (!setTeleopFast && gameTimer.seconds() > TELEOP_FAST_TIME) {
      blinkinLedDriver.setPattern(TELEOP_PATTERN_FAST);
      setTeleopFast = true;
      currentState = State.TELEOP_FAST;
    }

    if (!setEndgame && gameTimer.seconds() > ENDGAME_TIME) {
      blinkinLedDriver.setPattern(ENDGAME_PATTERN);
      setEndgame = true;
      currentState = State.ENDGAME;
    }
  }

  public void setCapstone(boolean set) {
    if(set)
      blinkinLedDriver.setPattern(CAPSTONE_PATTERN);
    else {
      switch(currentState) {
        case TELEOP:
          blinkinLedDriver.setPattern(TELEOP_PATTERN_SLOW);
          break;
        case TELEOP_FAST:
          blinkinLedDriver.setPattern(TELEOP_PATTERN_FAST);
          break;
        case ENDGAME:
          blinkinLedDriver.setPattern(ENDGAME_PATTERN);
          break;
      }

    }
  }

  enum State {
    TELEOP,
    TELEOP_FAST,
    ENDGAME
  }
}
