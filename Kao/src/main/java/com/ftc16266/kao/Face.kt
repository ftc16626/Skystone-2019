package com.ftc16266.kao

import android.graphics.Canvas
import android.os.Handler
import android.os.Looper.loop
import android.view.View
import com.ftc16266.kao.parts.BodyPart

abstract class Face(private val view: View) {
    protected val bodyParts: ArrayList<BodyPart> = ArrayList()
    protected val settings = mutableMapOf<String, Any>()

    protected var running = false
    protected var dirty = false

    private val handler: Handler = Handler()

    var tickDelay: Long = 60 / 1000

    fun draw(canvas: Canvas) {
        for (e in bodyParts) {
            e.draw(canvas)
        }
    }

    fun setSetting(key: String, value: Any) {
        settings[key] = value
    }

    fun start() {
        if (!running) {
            running = true

            handler.post(object : Runnable {
                override fun run() {

                    loop()

                    if(dirty) {
                        view.invalidate()
                        dirty = false
                    }

                    if (running)
                        handler.postDelayed(this, tickDelay)
                }
            })
        }
    }

    fun stop() {
        running = false
    }

    open fun loop() {}
}