package com.ftc16266.kao.parts

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class Mouth(x: Float, y: Float, width: Float, curveRadius: Float, lineWidth: Float, bgColor: Int): BodyPart {
    override var x = x
    override var y = y

    private var x1 = x - width / 2
    private var x2 = x + width / 2
    private var y1 = y
    private var y2 = y

    private var curveRadius = curveRadius
    private var lineWidth = lineWidth

    private var bgColor = bgColor

    override val paint: Paint = Paint()
    private val path = Path()

    init {
        paint.apply {
            isAntiAlias = true
            color = bgColor
            style = Paint.Style.STROKE
            strokeWidth = lineWidth
        }

        val midX = x1 + ((x2 - x1) / 2)
        val midY = y1 + ((y2 - y1) / 2)
        val xDiff = midX - x1
        val yDiff = midY - y1

        val angle = (atan2(yDiff.toDouble(), xDiff.toDouble()) * (180 / Math.PI)) - 90
        val angleRadians = Math.toRadians(angle)
        val pointX = (midX + curveRadius * cos(angleRadians)).toFloat()
        val pointY = (midY + curveRadius * sin(angleRadians)).toFloat()

        path.moveTo(x1, y1);
        path.cubicTo(x1, y1, pointX, pointY, x2, y2)
    }

    override fun draw(canvas: Canvas) {
        canvas.drawPath(path, paint)
    }
}