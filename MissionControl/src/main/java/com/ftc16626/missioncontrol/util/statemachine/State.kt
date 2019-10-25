package com.ftc16626.missioncontrol.util.statemachine

open class State(val value: Enum<*>) {
    var customTransition = false
    private val transitionList = mutableMapOf<Enum<*>, Enum<*>>()

    open fun on(event: Enum<*>, newState: Enum<*>): State {
        customTransition = true

        if(transitionList.containsKey(event))
            throw Error("Transition already exists in state")
        transitionList[event] = newState

        return this
    }

    fun getStateAfterTransition(transition: Enum<*>): Enum<*> {
        if(!transitionList.containsKey(transition))
            throw Error("State does not contain custom transition")

        return transitionList[transition]!!
    }
}