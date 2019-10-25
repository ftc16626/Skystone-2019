package com.ftc16626.missioncontrol.util.statemachine

class StateMachine {
    private val stateList = mutableListOf<State>()
    private var currentPosition = 0

    private val stateMap = mutableMapOf<Enum<*>, State>()

    var currentState: Enum<*>? = null

    fun state(state: State): StateMachine {
        if (stateMap.any { it.value == state.value })
            throw Error("State already exists in machine")

        stateMap[state.value] = state
        stateList.add(state)

        if(stateList.size == 1)
            currentState = state.value

        return this
    }

    fun transition() {
        if(currentPosition + 1 >= stateList.size)
            throw Error("Cannot progress state machine any further")

        if(stateList[currentPosition].customTransition)
            throw Error("This state requires a custom transition")

        currentPosition++
        currentState = stateList[currentPosition].value
    }

    fun transition(event: Enum<*>) {
        if(!stateList[currentPosition].customTransition) {
            transition()
            return
        }

        val targetState = stateMap[stateList[currentPosition].getStateAfterTransition(event)]
        val position = stateList.indexOf(targetState)

        currentPosition = position
        currentState = stateList[currentPosition].value
    }
}