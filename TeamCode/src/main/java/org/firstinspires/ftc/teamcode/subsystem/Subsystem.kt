package org.firstinspires.ftc.teamcode.subsystem

import android.util.Log

abstract class Subsystem {
    var on = true

    private var alreadyMovedToUpdate = false

    val subsystemHandler = SubsystemHandler()

    // Subsystem lifecycle
    // Press init button ->
    // init (runs once) ->
    // initLoop (loops until start is pressed) ->
    // onMount (runs once for any setup) ->
    // update (loops until game is finished)

    fun privateOnInit() {
        onInit()

        subsystemHandler.onInit()
    }

    fun privateInitLoop() {
        subsystemHandler.initLoop()

        initLoop()
    }

    fun privateOnMount() {
        onMount()

        alreadyMovedToUpdate = true

        subsystemHandler.onMount()
    }

    fun privateUpdate() {
        update()

        subsystemHandler.update()
    }

    fun privateOnStop() {
        onStop()

        subsystemHandler.onStop()
    }

    open fun onInit() {}
    open fun initLoop() {}

    open fun onMount() {}
    open fun update() {}

    open fun onStop() {}

    fun turnOn(): Subsystem {
        on = true
        onInit()

        if(alreadyMovedToUpdate) onMount()

        return this
    }

    fun turnOff(): Subsystem {
        on = false
        onStop()

        alreadyMovedToUpdate = false

        return this
    }
}