package com.ftc16626.missioncontrol

import com.ftc16626.missioncontrol.util.statemachine.State
import com.ftc16626.missioncontrol.util.statemachine.StateMachine
import org.junit.Test

class StateMachineTest {
    @Test
    fun stateMachine_initWorks() {
        val stateMachine = buildStateMachine()

        assert(stateMachine.currentState == MyState.INIT)
    }

    @Test
    fun stateMachine_simpleTransitionWorks() {
        val stateMachine = buildStateMachine()

        stateMachine.transition()
        assert(stateMachine.currentState == MyState.WAITING)
        stateMachine.transition()
        assert(stateMachine.currentState == MyState.SAMPLING)
    }

    @Test
    fun stateMachine_customTransitionWorks() {
        val stateMachine = buildStateMachine()

        stateMachine.transition()
        stateMachine.transition()
        stateMachine.transition(Transition.RESTART_SAMPLING)
        assert(stateMachine.currentState == MyState.WAITING)

        stateMachine.transition()
        stateMachine.transition(Transition.FINISH)
        assert(stateMachine.currentState == MyState.DONE)
    }

    private fun buildStateMachine(): StateMachine {
        val machine = StateMachine()

        machine
            .state(State(MyState.INIT))
            .state(State(MyState.WAITING))
            .state(
                State(MyState.SAMPLING)
                    .on(Transition.RESTART_SAMPLING, MyState.WAITING)
                    .on(Transition.FINISH, MyState.DONE)
            )
            .state(State(MyState.DONE))

        return machine
    }

    internal enum class MyState {
        INIT,
        WAITING,
        SAMPLING,
        DONE
    }

    internal enum class Transition {
        RESTART_SAMPLING,
        FINISH
    }
}