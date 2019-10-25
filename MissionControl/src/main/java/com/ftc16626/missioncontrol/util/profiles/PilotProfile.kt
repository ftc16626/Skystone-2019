package com.ftc16626.missioncontrol.util.profiles

import com.google.gson.Gson

data class PilotProfile(
    @JvmField val name: String,
    @JvmField val controlScheme: StickControl,
    @JvmField val invertStrafeStickX: Boolean,
    @JvmField val invertStrafeStickY: Boolean,
    @JvmField val invertTurnStickX: Boolean,
    @JvmField val invertTurnStickY: Boolean,
    @JvmField val stickResponseCurve: StickResponseCurve,
    @JvmField val enableFieldCentric: Boolean
) {
    fun toJSON(): String {
        return Gson().toJson(this)
    }
}