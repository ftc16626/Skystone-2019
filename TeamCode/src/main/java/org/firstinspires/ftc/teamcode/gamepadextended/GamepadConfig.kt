package org.firstinspires.ftc.teamcode.gamepadextended

data class GamepadConfig (
    @JvmField val name: String,
    @JvmField val controlScheme: StickControl,
    @JvmField val invertStrafeStickX: Boolean,
    @JvmField val invertStrafeStickY: Boolean,
    @JvmField val invertTurnStickX: Boolean,
    @JvmField val invertTurnStickY: Boolean,
    @JvmField val stickResponseCurve: StickResponseCurve
) {
    enum class StickControl {
        STRAFE_RIGHT_TURN_LEFT_STICK,
        STRAFE_LEFT_TURN_RIGHT_STICK
    }

}

