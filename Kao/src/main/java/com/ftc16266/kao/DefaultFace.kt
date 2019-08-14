package com.ftc16266.kao

import android.graphics.Color
import com.ftc16266.kao.parts.Eye
import com.ftc16266.kao.parts.Head
import com.ftc16266.kao.parts.Mouth

class DefaultFace(faceRadius: Float, width: Float, height: Float): Face() {
    // Percentages - Size
    private val eyeWidthPercent = 0.465f
    private val eyeHeightPercent = 0.613f

    private val eyeCenterOffsetXPercent = 0.254f
    private val eyeCenterOffsetYPercent = -0.085f

    private val mouthWidthPercent = 0.317f
    private val mouthRadiusPercent = -0.105f
    private val mouthLineWidthPercent = 0.053f
    private val mouthCenterOffsetXPercent = 0
    private val mouthCenterOffsetYPercent = 0.529f

    // Values - Size/Offset
    private val headCenterOffsetX = 0f
    private val headCenterOffsetY = 0f

    private var faceRadius = faceRadius

    private var eyeWidthLeft = eyeWidthPercent * faceRadius
    private var eyeWidthRight = eyeWidthPercent * faceRadius

    private var eyeHeightLeft = eyeHeightPercent * faceRadius
    private var eyeHeightRight = eyeHeightPercent * faceRadius

    private var eyeCenterOffsetXLeft = eyeCenterOffsetXPercent * faceRadius
    private var eyeCenterOffsetXRight = eyeCenterOffsetXPercent * faceRadius

    private var eyeCenterOffsetYLeft = eyeCenterOffsetYPercent * faceRadius
    private var eyeCenterOffsetYRight = eyeCenterOffsetYPercent * faceRadius

    private var mouthWidth = mouthWidthPercent * faceRadius
    private var mouthRadius = mouthRadiusPercent * faceRadius
    private var mouthLineWidth = mouthLineWidthPercent * faceRadius
    private var mouthCenterOffsetX = mouthCenterOffsetXPercent * faceRadius
    private var mouthCenterOffsetY = mouthCenterOffsetYPercent * faceRadius

    // Anchors
    private val centerX: Float = width / 2
    private val centerY: Float = height / 2

    private val headAnchorX = centerX + headCenterOffsetX
    private val headAnchorY = centerY + headCenterOffsetY

    private val eyeLeftAnchorX = centerX - eyeCenterOffsetXLeft
    private val eyeLeftAnchorY = centerX + eyeCenterOffsetYLeft

    private val eyeRightAnchorX = centerX + eyeCenterOffsetXRight
    private val eyeRightAnchorY = centerX + eyeCenterOffsetYRight

    private val mouthAnchorX = centerX + mouthCenterOffsetX
    private var mouthAnchorY = centerY + mouthCenterOffsetY

    // Positions
    private var headX = headAnchorX
    private var headY = headAnchorY

    private var eyeLeftX = eyeLeftAnchorX
    private var eyeLeftY = eyeLeftAnchorY

    private var eyeRightX = eyeRightAnchorX
    private var eyeRightY = eyeRightAnchorY

    private var mouthX = mouthAnchorX
    private var mouthY = mouthAnchorY

    // Colors
    private var faceColor: Int = Color.parseColor("#FFCC21")
    private var eyeColor: Int = Color.parseColor("#1A202C")
    private var mouthColor: Int = Color.parseColor("#1A202C")

    // Entities
    private var head: Head = Head(headX, headY, faceRadius, faceColor)
    private var eyeLeft: Eye = Eye(eyeLeftX, eyeLeftY, eyeWidthLeft, eyeHeightLeft, eyeColor)
    private var eyeRight: Eye = Eye(eyeLeftX, eyeLeftY, eyeWidthRight, eyeHeightRight, eyeColor)
    private var mouth: Mouth = Mouth(mouthX, mouthY, mouthWidth, mouthRadius, mouthLineWidth, mouthColor)

    init {
        bodyParts.add(head)
        bodyParts.add(eyeLeft)
        bodyParts.add(eyeRight)
        bodyParts.add(mouth)
    }
}
