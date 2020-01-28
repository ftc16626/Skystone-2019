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

  private StateMachine<MyStateTeleop, TransitionTeleop> stateMachineTeleop = buildStateMachineTeleop();
  private StateMachine<MyStateEngame, TransitionEngame> stateMachineEndgame = buildStateMachineEndgame();

  private final DigitalChannel frontBeamBreak, backSwitch;
  private final String[] switchIds = new String[]{
      "intakeBeamBreak", "intakeSwitch"
  };

  private final double LIFTED_POSITION = 0.2;

  ElapsedTime grabbedBlockTime = new ElapsedTime();
  private final double IDLE_GRABBLOCK_TRANSITION_TIME = 0.3;

  ElapsedTime capstoneElapsedTime = new ElapsedTime();
  ElapsedTime initialLiftElapsed = new ElapsedTime();
  ElapsedTime liftGrabElapsed = new ElapsedTime();
  private boolean ENDGAME_TOGGLED = false;
  private final double ENDGAME_CAPSTONE_SET_TIME = 0.7;
  private final double ENDGAME_LIFT_WAIT = 0.5;
  private final double ENDGAME_LIFT_LOWER = 0.2;
  private final double ENDGAME_LIFT_CLAMP = ENDGAME_LIFT_LOWER + 0.1;

  public SubsystemAutoIntakeGrabber(Robot robot, RadicalOpMode opMode) {
    super(robot, opMode);

    frontBeamBreak = robot.hwMap.digitalChannel.get(switchIds[0]);
    backSwitch = robot.hwMap.digitalChannel.get(switchIds[1]);
  }

  @Override
  public void update() {

    if (!ENDGAME_TOGGLED) {
      handleTeleop();
    } else {
      handleEndgame();
    }
  }

  private void handleTeleop() {
    RevBulkData bulkData = getRobot().getBulkDataRight();

    if(bulkData == null) return;

    switch (Objects.requireNonNull(stateMachineTeleop.getCurrentState())) {
      case IDLE:
        if (!bulkData.getDigitalInputState(backSwitch)) {
          grabbedBlockTime.reset();
          stateMachineTeleop.transition();
        }
        break;
      case GRABBED_BLOCK:
        getRobot().subsystemVirtual4Bar.clamp();
        if (grabbedBlockTime.seconds() > IDLE_GRABBLOCK_TRANSITION_TIME) {
          stateMachineTeleop.transition();
        }
        break;
      case LIFTED_BLOCK:
        getRobot().subsystemVirtual4Bar.setPosition(LIFTED_POSITION);
        stateMachineTeleop.transition();
        break;
    }
  }

  private void handleEndgame() {
    RevBulkData bulkData = getRobot().getBulkDataRight();

    if(bulkData == null) return;

    Log.i("STATE", Objects.requireNonNull(stateMachineEndgame.getCurrentState()).toString());

    switch (Objects.requireNonNull(stateMachineEndgame.getCurrentState())) {
      case IDLE:
        if (!bulkData.getDigitalInputState(backSwitch)) {
          getRobot().subsystemVirtual4Bar.setPosition(LIFTED_POSITION);
          initialLiftElapsed.reset();
          stateMachineEndgame.transition();
        }
        break;
      case LIFTING_ARM:
        if (initialLiftElapsed.seconds() > ENDGAME_LIFT_WAIT) {
          capstoneElapsedTime.reset();
          stateMachineEndgame.transition();
        }
        break;
      case SET_CAPSTONE:
        getRobot().subsystemAutoCapstone.lower();
        if (capstoneElapsedTime.seconds() > ENDGAME_CAPSTONE_SET_TIME) {
          liftGrabElapsed.reset();
          stateMachineEndgame.transition();
        }
        break;
      case GRABBED_BLOCK:
        getRobot().subsystemVirtual4Bar.setPosition(0);
        if (liftGrabElapsed.seconds() > ENDGAME_LIFT_LOWER) {
          getRobot().subsystemVirtual4Bar.clamp();
        }

        if (liftGrabElapsed.seconds() > ENDGAME_LIFT_CLAMP) {
          stateMachineEndgame.transition();
        }
        break;
      case LIFTED_BLOCK:
        getRobot().subsystemVirtual4Bar.setPosition(LIFTED_POSITION);
        stateMachineEndgame.transition();
        break;
    }
  }

  public void setEndgame(boolean set) {
    ENDGAME_TOGGLED = set;

//    Log.i("STATE6", Objects.requireNonNull(stateMachineEndgame.getCurrentState()).toString());

//    if (ENDGAME_TOGGLED && stateMachineEndgame.getCurrentState() != MyStateEngame.IDLE) {
//      stateMachineEndgame.transition(TransitionEngame.RESET);
//    }
    stateMachineEndgame.manualSet(MyStateEngame.IDLE);
  }

  public void reset() {
    if (stateMachineTeleop.getCurrentState() == MyStateTeleop.TO_BE_RESET) {
      stateMachineTeleop.transition(TransitionTeleop.RESET);
      stateMachineEndgame.transition(TransitionEngame.RESET);
    }
  }

  private StateMachine<MyStateTeleop, TransitionTeleop> buildStateMachineTeleop() {
    StateMachine<MyStateTeleop, TransitionTeleop> stateMachine = new StateMachine<>();

    stateMachine
        .state(new State<>(MyStateTeleop.IDLE))
        .state(new State<>(MyStateTeleop.GRABBED_BLOCK))
        .state(new State<>(MyStateTeleop.LIFTED_BLOCK))
        .state(
            new State<MyStateTeleop, TransitionTeleop>(MyStateTeleop.TO_BE_RESET)
                .on(TransitionTeleop.RESET, MyStateTeleop.IDLE)
        );

    return stateMachine;
  }

  private StateMachine<MyStateEngame, TransitionEngame> buildStateMachineEndgame() {
    StateMachine<MyStateEngame, TransitionEngame> stateMachine = new StateMachine<>();

    stateMachine
        .state(new State<>(MyStateEngame.IDLE))
        .state(new State<MyStateEngame, TransitionEngame>(MyStateEngame.LIFTING_ARM)
//            .on(TransitionEngame.RESET, MyStateEngame.IDLE)
        )
        .state(new State<MyStateEngame, TransitionEngame>(MyStateEngame.SET_CAPSTONE)
//            .on(TransitionEngame.RESET, MyStateEngame.IDLE)
        )
        .state(new State<MyStateEngame, TransitionEngame>(MyStateEngame.GRABBED_BLOCK)
//            .on(TransitionEngame.RESET, MyStateEngame.IDLE)
        )
        .state(new State<MyStateEngame, TransitionEngame>(MyStateEngame.LIFTED_BLOCK)
//            .on(TransitionEngame.RESET, MyStateEngame.IDLE)
        )
        .state(
            new State<MyStateEngame, TransitionEngame>(MyStateEngame.TO_BE_RESET)
                .on(TransitionEngame.RESET, MyStateEngame.IDLE)
        );

    return stateMachine;
  }

  enum MyStateTeleop {
    IDLE,
    GRABBED_BLOCK,
    LIFTED_BLOCK,
    TO_BE_RESET
  }

  enum TransitionTeleop {
    RESET
  }

  enum MyStateEngame {
    IDLE,
    LIFTING_ARM,
    SET_CAPSTONE,
    GRABBED_BLOCK,
    LIFTED_BLOCK,
    TO_BE_RESET
  }

  enum TransitionEngame {
    RESET
  }
}
