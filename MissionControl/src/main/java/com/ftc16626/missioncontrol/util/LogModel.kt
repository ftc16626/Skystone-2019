package com.ftc16626.missioncontrol.util

import java.text.SimpleDateFormat
import java.util.*

class LogModel @JvmOverloads constructor(var msg: String, var tag: String, time: Date = Date()) {
    var time: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(time)
}