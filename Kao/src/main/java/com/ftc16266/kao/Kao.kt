package com.ftc16266.kao

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.ftc16266.kao.parts.Head

class Kao(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var faceRadius: Float = (0).toFloat()

    private val head: Head

    private val colors = object {
        var faceBg: Int = 0xffcc21
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.Kao,
            0, 0
        ).apply {
            try {
                faceRadius = getDimension(R.styleable.Kao_faceRadius, (0).toFloat())
                Log.i("Kao", faceRadius.toString())
            } finally {
                recycle()
            }
        }

        head = Head(100.toFloat(), 100.toFloat(), faceRadius, colors.faceBg)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        head.draw(canvas)
    }
}