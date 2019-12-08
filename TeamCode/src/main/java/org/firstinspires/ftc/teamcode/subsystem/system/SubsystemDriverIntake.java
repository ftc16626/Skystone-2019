package org.firstinspires.ftc.teamcode.subsystem.system;

import com.ftc16626.missioncontrol.util.statemachine.State;
import com.ftc16626.missioncontrol.util.statemachine.StateMachine;
import java.util.Objects;
import org.firstinspires.ftc.teamcode.util.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadType;
import org.firstinspires.ftc.teamcode.hardware.MainHardware;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.jetbrains.annotations.NotNull;

public class SubsystemDriverIntake extends Subsystem implements GamepadListener {

  DriverInterface driverInterface;

  private StateMachine stateMachine = buildStateMachine();

  SubsystemDriverIntake(
      @NotNull MainHardware robot,
      @NotNull RadicalOpMode opMode,
      @NotNull DriverInterface driverInterface) {
    super(robot, opMode);

    this.driverInterface = driverInterface;
  }

  @Override
  public void update() {
    if (driverInterface.aid.gamepad.b) {
      getRobot().intake.open();
    } else {
      getRobot().intake.close();
    }
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
    if (gamepadType == GamepadType.AID) {
      if (eventType == GamepadEventType.BUTTON_PRESSED) {
        switch (eventName) {
          case LEFT_BUMPER:
            switch ((MyState) Objects.requireNonNull(stateMachine.getCurrentState())) {
              case OFF:
              case SPINNING_OUT:
                stateMachine.transition(Transition.SPIN_IN);
                break;
              case SPINNING_IN:
                stateMachine.transition(Transition.TURN_OFF);
                break;
            }
            break;
          case RIGHT_BUMPER:
            switch ((MyState) Objects.requireNonNull(stateMachine.getCurrentState())) {
              case OFF:
              case SPINNING_IN:
                stateMachine.transition(Transition.SPIN_OUT);
                break;
              case SPINNING_OUT:
                stateMachine.transition(Transition.TURN_OFF);
                break;
            }
            break;
        }

        setMotorPowers();
      }
    }
  }

  private void setMotorPowers() {
    switch ((MyState) Objects.requireNonNull(stateMachine.getCurrentState())) {
      case OFF:
        getRobot().intake.toggle(false);
        break;
      case SPINNING_IN:
        getRobot().intake.toggle(true);
        getRobot().intake.directionBackward();
        break;
      case SPINNING_OUT:
        getRobot().intake.toggle(true);
        getRobot().intake.directionForward();
        break;
    }
  }

  private StateMachine buildStateMachine() {
    StateMachine stateMachine = new StateMachine();

    stateMachine
        .state(
            new State(MyState.OFF)
                .on(Transition.SPIN_IN, MyState.SPINNING_IN)
                .on(Transition.SPIN_OUT, MyState.SPINNING_OUT)
        )
        .state(
            new State(MyState.SPINNING_IN)
                .on(Transition.TURN_OFF, MyState.OFF)
                .on(Transition.SPIN_OUT, MyState.SPINNING_OUT)
        )
        .state(
            new State(MyState.SPINNING_OUT)
                .on(Transition.TURN_OFF, MyState.OFF)
                .on(Transition.SPIN_IN, MyState.SPINNING_IN)
        );

    return stateMachine;
  }

  enum MyState {
    OFF,
    SPINNING_IN,
    SPINNING_OUT
  }

  enum Transition {
    SPIN_OUT,
    SPIN_IN,
    TURN_OFF
  }
}
