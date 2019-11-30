package org.firstinspires.ftc.teamcode.subsystem

import org.firstinspires.ftc.teamcode.hardware.MainHardware

abstract class Subsystem(val robot: MainHardware, val opMode: RadicalOpMode) {
    var on = true

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
        return this
    }

    fun turnOff(): Subsystem {
        on = false
        return this
    }
}