package org.firstinspires.ftc.teamcode.teleop.subsystem;

import com.ftc16626.missioncontrol.util.statemachine.State;
import com.ftc16626.missioncontrol.util.statemachine.StateMachine;
import java.util.Objects;
import org.firstinspires.ftc.teamcode.hardware.Robot;
import org.firstinspires.ftc.teamcode.subsystem.Subsystem;
import org.firstinspires.ftc.teamcode.util.gamepadextended.DriverInterface;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventName;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadEventType;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadListener;
import org.firstinspires.ftc.teamcode.util.gamepadextended.listener.GamepadType;

public class SubsystemTeleIntake extends Subsystem implements GamepadListener {
  private final Robot robot;
  private final DriverInterface driverInterface;

  private StateMachine<MyState, Transition> stateMachine = buildStateMachine();

  private boolean lastSetZero = false;

  public SubsystemTeleIntake(Robot robot, DriverInterface driverInterface) {
    driverInterface.addListener(this);
    this.driverInterface = driverInterface;

    this.robot = robot;
  }

  @Override
  public void update() {
    float rightTrigger = driverInterface.driver.gamepad.right_trigger;
    float leftTrigger = driverInterface.driver.gamepad.left_trigger;

    if(leftTrigger > 0.05) {
      robot.subsystemIntake.setDirect(-leftTrigger, -leftTrigger);
      lastSetZero = false;
    } else if(rightTrigger > 0.05) {
      robot.subsystemIntake.setDirect(rightTrigger, rightTrigger);
      lastSetZero = false;
    } else if(!lastSetZero) {
      lastSetZero = true;
      robot.subsystemIntake.setDirect(0, 0);
    }
  }

  @Override
  public void actionPerformed(GamepadEventName eventName, GamepadEventType eventType,
      GamepadType gamepadType) {
//    if (gamepadType == GamepadType.DRIVER && eventType == GamepadEventType.BUTTON_PRESSED) {
//      switch (eventName) {
//        case LEFT_BUMPER:
//          switch (Objects.requireNonNull(stateMachine.getCurrentState())) {
//            case OFF:
//            case SPINNING_OUT:
//              stateMachine.transition(Transition.SPIN_IN);
//              break;
//            case SPINNING_IN:
//              stateMachine.transition(Transition.TURN_OFF);
//              break;
//          }
//          break;
//        case RIGHT_BUMPER:
//          switch (Objects.requireNonNull(stateMachine.getCurrentState())) {
//            case OFF:
//            case SPINNING_IN:
//              stateMachine.transition(Transition.SPIN_OUT);
//              break;
//            case SPINNING_OUT:
//              stateMachine.transition(Transition.TURN_OFF);
//              break;
//          }
//          break;
//      }
//
//      setMotorPowers();
//    }
  }

  private void setMotorPowers() {
    switch (Objects.requireNonNull(stateMachine.getCurrentState())) {
      case OFF:
        robot.subsystemIntake.setMotorOn(false);
        break;
      case SPINNING_IN:
        robot.subsystemIntake.setReversed(false);
        robot.subsystemIntake.setMotorOn(true);
        break;
      case SPINNING_OUT:
        robot.subsystemIntake.setReversed(true);
        robot.subsystemIntake.setMotorOn(true);
        break;
    }
  }

  private StateMachine<MyState, Transition> buildStateMachine() {
    StateMachine<MyState, Transition> stateMachine = new StateMachine<>();

    stateMachine
        .state(
            new State<MyState, Transition>(MyState.OFF)
                .on(Transition.SPIN_IN, MyState.SPINNING_IN)
                .on(Transition.SPIN_OUT, MyState.SPINNING_OUT)
        )
        .state(
            new State<MyState, Transition>(MyState.SPINNING_IN)
                .on(Transition.TURN_OFF, MyState.OFF)
                .on(Transition.SPIN_OUT, MyState.SPINNING_OUT)
        )
        .state(
            new State<MyState, Transition>(MyState.SPINNING_OUT)
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
