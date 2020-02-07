package com.ftc16626.missioncontrol.util.statemachine

class StateMachine<StateEnum, TransitionEnum> {
    private val stateList = mutableListOf<State<StateEnum, TransitionEnum>>()
    private var currentPosition = 0

    private val stateMap = mutableMapOf<StateEnum, State<StateEnum, TransitionEnum>>()

    var currentState: StateEnum? = null

    fun state(state: State<StateEnum, TransitionEnum>): StateMachine<StateEnum, TransitionEnum> {
        if (stateMap.any { it.value == state.value })
            throw Error("State already exists in machine")

        stateMap[state.value] = state
        stateList.add(state)

        if (stateList.size == 1)
            currentState = state.value

        return this
    }

    fun transition() {
        if (currentPosition + 1 >= stateList.size)
            throw Error("Cannot progress state machine any further")

        if (stateList[currentPosition].customTransition)
            throw Error("This state requires a custom transition")

        val lastState = currentState

        currentPosition++
        currentState = stateList[currentPosition].value

        (stateMap[stateList[currentPosition].value] as State<StateEnum, TransitionEnum>)
            .transitionCallbackList.forEach {
            it(lastState!!)
        }
    }

    fun transition(event: TransitionEnum) {
        if (!stateList[currentPosition].customTransition) {
            transition()
            return
        }

        val targetState = stateMap[stateList[currentPosition].getStateAfterTransition(event)]
        val position = stateList.indexOf(targetState)

        val lastState = currentState

        currentPosition = position
        currentState = stateList[currentPosition].value

        (stateMap[stateList[currentPosition].value] as State<StateEnum, TransitionEnum>)
            .transitionCallbackList.forEach {
            it(lastState!!)
        }
    }

    fun manualSet(state: StateEnum) {
        val targetState = stateMap[state]
        val targetIndex = stateList.indexOf(targetState)

        val lastState = currentState

        currentPosition = targetIndex
        currentState = stateList[currentPosition].value

        (stateMap[stateList[currentPosition].value] as State<StateEnum, TransitionEnum>)
            .transitionCallbackList.forEach {
            it(lastState!!)
        }
    }
}