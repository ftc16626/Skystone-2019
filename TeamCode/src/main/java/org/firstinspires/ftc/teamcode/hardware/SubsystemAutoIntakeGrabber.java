package org.firstinspires.ftc.teamcode.hardware;

import android.util.Log;
import com.ftc16626.missioncontrol.util.statemachine.State;
import com.ftc16626.missioncontrol.util.statemachine.StateMachine;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.util.ElapsedTime;
import java.util.Objects;
import org.firstinspires.ftc.teamcode.subsystem.HardwareSubsystem;
import org.firstinspires.ftc.teamcode.subsystem.RadicalOpMode;
import org.openftc.revextensions2.RevBulkData;

public class SubsystemAutoIntakeGrabber extends HardwareSubsystem {

  private StateMachine<MyState, Transition> stateMachine = buildStateMachine();

  private final DigitalChannel frontBeamBreak, backSwitch;
  private final String[] switchIds = new String[]{
      "intakeBeamBreak", "intakeSwitch"
  };

  ElapsedTime grabbedBlockTime = new ElapsedTime();

  private final double IDLE_GRABBLOCK_TRANSITION_TIME = 0.3;
  private final double LIFTED_POSITION = 0.2;

  public SubsystemAutoIntakeGrabber(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);

    frontBeamBreak = robot.hwMap.digitalChannel.get(switchIds[0]);
    backSwitch = robot.hwMap.digitalChannel.get(switchIds[1]);
  }

  @Override
  public void update() {
    RevBulkData bulkData = getRobot().getBulkDataRight();

    Log.i("SWITCH", Boolean.toString(bulkData.getDigitalInputState(frontBeamBreak)));

    switch (Objects.requireNonNull(stateMachine.getCurrentState())) {
      case IDLE:
        if (!bulkData.getDigitalInputState(backSwitch)) {
          stateMachine.transition();
          grabbedBlockTime.reset();
        }
        break;
      case GRABBED_BLOCK:
        getRobot().subsystemVirtual4Bar.clamp();
        if (grabbedBlockTime.seconds() > IDLE_GRABBLOCK_TRANSITION_TIME) {
          stateMachine.transition();
        }
        break;
      case LIFTED_BLOCK:
        getRobot().subsystemVirtual4Bar.setPosition(LIFTED_POSITION);
//        stateMachine.transition(Transition.RESET);
        stateMachine.transition();
        break;
    }
  }

  private StateMachine<MyState, Transition> buildStateMachine() {
    StateMachine<MyState, Transition> stateMachine = new StateMachine<>();

    stateMachine
        .state(new State<>(MyState.IDLE))
        .state(new State<>(MyState.GRABBED_BLOCK))
        .state(
            new State<MyState, Transition>(MyState.LIFTED_BLOCK)
//                .on(Transition.RESET, MyState.IDLE)
        )
        .state(new State<>(MyState.AFTER));

    return stateMachine;
  }

  enum MyState {
    IDLE,
    GRABBED_BLOCK,
    LIFTED_BLOCK,
    AFTER
  }

  enum Transition {
    RESET
  }
}
