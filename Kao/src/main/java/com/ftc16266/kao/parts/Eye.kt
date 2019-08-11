package com.ftc16266.kao.parts

import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build

class Eye(x: Float, y: Float, width: Float, height: Float, bgColor: Int): BodyPart {
    override var x = x
    override var y = y
    private var width = width
    private var height = height

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawOval(x - width / 2, y - height / 2, x + width / 2, y + height / 2, paint)
        }
    }
}