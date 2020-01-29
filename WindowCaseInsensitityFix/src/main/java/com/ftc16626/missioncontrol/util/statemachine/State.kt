package com.ftc16626.missioncontrol.util.statemachine

open class State<StateEnum, TransitionEnum>(val value: StateEnum) {
    var customTransition = false
    private val transitionList = mutableMapOf<TransitionEnum, StateEnum>()

    val transitionCallbackList =
        mutableListOf<(from: StateEnum) -> Unit>()

    open fun on(event: TransitionEnum, newState: StateEnum): State<StateEnum, TransitionEnum> {
        customTransition = true

        if (transitionList.containsKey(event))
            throw Error("Transition already exists in state")
        transitionList[event] = newState

        return this
    }

    fun getStateAfterTransition(transition: TransitionEnum): StateEnum {
        if (!transitionList.containsKey(transition))
            throw Error("State does not contain custom transition")

        return transitionList[transition]!!
    }

    fun onTransition(callback: (from: StateEnum) -> Unit): State<StateEnum, TransitionEnum> {
        transitionCallbackList.add(callback)

        return this
    }
}