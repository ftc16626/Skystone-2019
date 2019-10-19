package org.firstinspires.ftc.teamcode.gamepadextended

data class GamepadProfile (
    @JvmField val name: String,
    @JvmField val controlScheme: StickControl,
    @JvmField val invertStrafeStickX: Boolean,
    @JvmField val invertStrafeStickY: Boolean,
    @JvmField val invertTurnStickX: Boolean,
    @JvmField val invertTurnStickY: Boolean,
    @JvmField val stickResponseCurve: StickResponseCurve,
    @JvmField val enableFieldCentric: Boolean
) {
    enum class StickControl {
        STRAFE_RIGHT_TURN_LEFT_STICK,
        STRAFE_LEFT_TURN_RIGHT_STICK
    }

}

