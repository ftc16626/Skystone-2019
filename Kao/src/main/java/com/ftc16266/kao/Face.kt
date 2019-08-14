package com.ftc16266.kao

import android.graphics.Canvas
import com.ftc16266.kao.parts.BodyPart

abstract class Face {
    protected val bodyParts: ArrayList<BodyPart> = ArrayList()

    fun draw(canvas: Canvas) {
        for(e in bodyParts) {
            e.draw(canvas)
        }
    }
}