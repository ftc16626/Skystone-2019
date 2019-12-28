package org.firstinspires.ftc.teamcode.auto.pullfoundation;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.ftc16626.missioncontrol.util.statemachine.State;
import com.ftc16626.missioncontrol.util.statemachine.StateMachine;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import kotlin.Unit;
import org.firstinspires.ftc.teamcode.auto.RoadRunnerBaseOpmode;

@Autonomous(name="Pull Foundation - Red")
public class AutoOpPullFoundationRed extends RoadRunnerBaseOpmode {
  private Trajectory trajectoryBeforeTurn;
  private Trajectory trajectoryAfterTurn;

  private final double DISTANCE_LEFT1 = 300;
  private final double DISTANCE_FORWARD1 = 500;

  private final double DISTANCE_FORWARD2 = 200;
  private final double DISTANCE_BACKWARD2 = 200;

  private StateMachine<MyState, Transition> stateMachine =
      new StateMachine<MyState, Transition>()
          .state(new State<>(MyState.INIT))
          .state(new State<>(MyState.FIRST_TRAJECTORY))
          .state(new State<MyState, Transition>(MyState.TURNING)
              .onTransition((MyState from) -> {
                drive.turnSync(Math.toRadians(-90), () -> {
                  hackyTransitionCall();

                  return Unit.INSTANCE;
                });

                return Unit.INSTANCE;
              }))
          .state(new State<MyState, Transition>(MyState.SECOND_TRAJECTORY)
              .onTransition((MyState from) -> {
                drive.followTrajectory(trajectoryAfterTurn);
                return Unit.INSTANCE;
              }));

  @Override
  public void onInit() {
    trajectoryBeforeTurn = drive.trajectoryBuilder()
        .addMarker(() -> {
          stateMachine.transition();

          return Unit.INSTANCE;
        })
        .strafeLeft(DISTANCE_LEFT1)
        .forward(DISTANCE_FORWARD1)
        .addMarker(() -> {
          stateMachine.transition();

          return Unit.INSTANCE;
        })
        .build();

    trajectoryAfterTurn = drive.trajectoryBuilder()
        .forward(DISTANCE_FORWARD2)
        .back(DISTANCE_BACKWARD2)
        .build();
  }

  @Override
  public void onMount() {
    drive.followTrajectory(trajectoryBeforeTurn);
  }

  private void hackyTransitionCall() {
    stateMachine.transition();
  }

  enum MyState {
    INIT,
    FIRST_TRAJECTORY,
    TURNING,
    SECOND_TRAJECTORY,
  }

  enum Transition {}
}
