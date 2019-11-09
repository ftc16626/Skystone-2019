package org.firstinspires.ftc.teamcode.subsystem

class SubsystemHandler {
    val subsystems = mutableListOf<Subsystem>();

    fun add(system: Subsystem) {
        subsystems.add(system)
    }

    fun update() {

    }
}