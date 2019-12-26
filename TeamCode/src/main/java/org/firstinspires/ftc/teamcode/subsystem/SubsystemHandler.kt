package org.firstinspires.ftc.teamcode.subsystem

class SubsystemHandler {
    private val subsystems = mutableListOf<Subsystem>()

    fun add(system: Subsystem) {
        subsystems.add(system)
    }

    fun onInit() {
        for(sub in subsystems) {
            if(sub.on) {
                sub.privateOnInit()
            }
        }
    }

    fun initLoop() {
        for(sub in subsystems) {
            if(sub.on) {
                sub.privateInitLoop()
            }
        }
    }

    fun onMount() {
        for(sub in subsystems) {
            if(sub.on) {
                sub.privateOnMount()
            }
        }
    }

    fun update() {
        for(sub in subsystems) {
            if(sub.on) {
                sub.privateUpdate()
            }
        }
    }

    fun onStop() {
        for(sub in subsystems) {
            if(sub.on) {
                sub.privateOnStop()
            }
        }
    }
}