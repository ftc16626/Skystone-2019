package com.ftc16266.missioncontrol

import java.text.SimpleDateFormat
import java.util.*

class LogModel @JvmOverloads constructor(msg: String, tag: String, time: Date = Date()) {
    var msg: String = msg;
    var tag: String = tag;
    var time: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(time)
}