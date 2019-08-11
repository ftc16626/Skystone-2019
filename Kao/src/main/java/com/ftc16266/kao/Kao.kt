package com.ftc16266.kao

import android.content.Context
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import com.ftc16266.kao.parts.BodyPart
import com.ftc16266.kao.parts.Eye
import com.ftc16266.kao.parts.Head

class Kao(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var faceRadius: Float = 0f

    private var centerX: Float = 0f
    private var centerY: Float = 0f

    private val eyeWidth = 120f
    private val eyeHeight = 180f
    private val eyeCenterOffsetX = 120f

    private var head: Head? = null
    private var eye1: Eye? = null
    private var eye2: Eye? = null

    private val colors = object {
        var faceBg: Int = ContextCompat.getColor(getContext(), R.color.faceBg)
        var eyeBg: Int = ContextCompat.getColor(getContext(), R.color.eyeBg)
    }

    private val entities: ArrayList<BodyPart> = ArrayList()

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
            centerX = (width / 2).toFloat()
            centerY = (height / 2).toFloat()
            head = Head(centerX, centerY, faceRadius, colors.faceBg)

            eye1 = Eye(centerX - eyeCenterOffsetX, centerY, eyeWidth, eyeHeight, colors.eyeBg)
            eye2 = Eye(centerX + eyeCenterOffsetX, centerY, eyeWidth, eyeHeight, colors.eyeBg)

            entities.add(head!!)
            entities.add(eye1!!)
            entities.add(eye2!!)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        for (e in entities) {
            e.draw(canvas)
        }
    }
}