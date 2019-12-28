package com.ftc16626.missioncontrol

import com.ftc16626.missioncontrol.util.statemachine.State
import com.ftc16626.missioncontrol.util.statemachine.StateMachine
import org.junit.Test

class StateMachineTest {
    private var callbackCounter = 0
    private var callbackValue: MyState? = null
    private val callbackTest: (from: MyState) -> Unit = {
        callbackCounter++
        callbackValue = it
    }

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

    @Test
    fun stateMachine_callbackWorks() {
        val stateMachine = buildStateMachine()

        stateMachine.transition()

        assert(callbackCounter == 1)
        assert(callbackValue == MyState.INIT)
    }

    private fun buildStateMachine(): StateMachine<MyState, Transition> {
        val machine = StateMachine<MyState, Transition>()

        machine
            .state(State(MyState.INIT))
            .state(State<MyState, Transition>(MyState.WAITING)
                .onTransition(callbackTest))
            .state(
                State<MyState, Transition>(MyState.SAMPLING)
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