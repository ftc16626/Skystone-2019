package com.ftc16626.missioncontrol.util

import com.ftc16626.missioncontrol.LogModel
import java.io.File
import java.io.FileOutputStream

class Scribe() {
    private var writingToFile = false

    private var currentFileOutputStream: FileOutputStream? = null

    fun beginWrite(file: File) {
        if (currentFileOutputStream != null) {
            currentFileOutputStream!!.close()
        }
        currentFileOutputStream = FileOutputStream(file)
        writingToFile = true
    }

    fun closeWrite() {
        if (currentFileOutputStream != null) {
            currentFileOutputStream!!.close()
        }
        writingToFile = false
    }

    fun writeLine(string: String) {
        write("$string\n")
    }

    fun writeLine(log: LogModel) {
        val tag = log.tag
        val msg = log.msg
        val time = log.time

        write("$tag $msg $time\n")
    }

    fun write(string: String) {
        if (!writingToFile) return

        if (currentFileOutputStream != null) {
            currentFileOutputStream!!.write(string.toByteArray())
        }
    }
}