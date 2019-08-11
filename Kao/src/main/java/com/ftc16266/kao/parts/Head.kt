package com.ftc16266.kao.parts

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

class Head(x: Float, y: Float, radius: Float, bgColor: Int) {
    private var x = x
    private var y = y
    private var radius = radius

    private var bgColor = bgColor

    private val paint: Paint = Paint()

    init {
        paint.apply {
            isAntiAlias = true
            color = bgColor
            style = Paint.Style.FILL
        }
    }

    public fun draw(canvas: Canvas) {
        canvas.drawCircle(x, y, radius, paint)
    }
}