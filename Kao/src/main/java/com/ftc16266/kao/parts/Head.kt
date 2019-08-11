package com.ftc16266.kao.parts

import android.graphics.Canvas
import android.graphics.Paint

class Head(x: Float, y: Float, radius: Float, bgColor: Int): BodyPart {
    override var x = x
    override var y = y
    private var radius = radius

    private var bgColor = bgColor

    override val paint: Paint = Paint()

    init {
        paint.apply {
            isAntiAlias = true
            color = bgColor
            style = Paint.Style.FILL
        }
    }

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(x, y, radius, paint)
    }
}