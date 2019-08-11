package com.ftc16266.kao

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import com.ftc16266.kao.parts.Head

class Kao(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var faceRadius: Float = (0).toFloat()

    private var head: Head? = null

    private val colors = object {
        var faceBg: Int = ContextCompat.getColor(getContext(), R.color.faceBg)
    }

    private inline fun View.waitForLayout(crossinline f: () -> Unit) = with(viewTreeObserver) {
        addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        })
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

        waitForLayout {
            head = Head((width / 2).toFloat(), (height / 2).toFloat(), faceRadius, colors.faceBg)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        head?.draw(canvas)
    }
}