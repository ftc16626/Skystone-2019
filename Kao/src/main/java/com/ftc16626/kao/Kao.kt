package com.ftc16626.kao

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.View

class Kao(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private var faceRadius: Float = 0f

    private var width = 0f
    private var height = 0f

    private val displayMetrics = DisplayMetrics()

    var currentFace: Face

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

        val dimen = getMetrics()
        width = dimen[0]
        height = dimen[1]

        currentFace = DefaultFace(faceRadius, width, height, this)
        currentFace.setSetting("breathing", true)
        currentFace.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        currentFace.draw(canvas)
    }

    fun hideView() {
        visibility = GONE
        currentFace.stop()
    }

    fun showView() {
        visibility = VISIBLE
        currentFace.start()
    }

    fun setFace(face: Face) {
        currentFace = face
    }

    private fun getMetrics(): Array<Float> {
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)

        val realHeight = displayMetrics.heightPixels + getStatusBarHeight()
        val realWidth = displayMetrics.widthPixels

        width = realWidth.toFloat()
        height = realHeight.toFloat()

        return arrayOf(width, height)
    }

    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if(resourceId > 0) result = resources.getDimensionPixelOffset(resourceId)

        return result * 2
    }
}