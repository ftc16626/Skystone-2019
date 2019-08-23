package com.ftc16626.kao

import android.graphics.Color
import android.view.View
import com.ftc16626.kao.parts.Eye
import com.ftc16626.kao.parts.Head
import com.ftc16626.kao.parts.Mouth
import kotlin.math.PI
import kotlin.math.sin

class DefaultFace(faceRadius: Float, width: Float, height: Float, view: View) : Face(view = view) {
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
    private val eyeLeftAnchorY = centerY + eyeCenterOffsetYLeft

    private val eyeRightAnchorX = centerX + eyeCenterOffsetXRight
    private val eyeRightAnchorY = centerY + eyeCenterOffsetYRight

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
    private var eyeRight: Eye = Eye(eyeRightX, eyeRightY, eyeWidthRight, eyeHeightRight, eyeColor)
    private var mouth: Mouth =
        Mouth(mouthX, mouthY, mouthWidth, mouthRadius, mouthLineWidth, mouthColor)

    // Misc
    private var faceBreathingRate: Float = 1f / 100f
    private var faceBreathingProgress: Float = 0f
    private var faceBreathingAmplitude = 50
    private var faceBreathingDelay = 0.35
    private var faceBreathingFinishCheckpoint = 0f

    private var finishAnimationFaceBreathing = false
    private var completelyFinishFaceBreathing = false

    private var eyesMouthBreathingRate: Float = 1f / 100f
    private var eyesMouthBreathingProgress: Float = 0f
    private var eyesMouthBreathingAmplitude = 50
    private var eyesMouthBreathingDelay = 0
    private var eyesMouthBreathingFinishCheckpoint = 0f

    private var finishAnimationEyesMouthBreathing = false
    private var completelyFinishEyesMouthBreathing = false

    init {
        bodyParts.add(head)
        bodyParts.add(eyeLeft)
        bodyParts.add(eyeRight)
        bodyParts.add(mouth)

        // Settings
        setSetting("breathing", false)
        setSetting("abrupt-animation-cancel", false)
    }

    override fun loop() {
        if (settings["breathing"] as Boolean or finishAnimationFaceBreathing) {
            faceBreathingProgress += faceBreathingRate
            if (faceBreathingProgress > faceBreathingDelay)
                head.y =
                    headAnchorY + (sin(faceBreathingProgress - faceBreathingDelay) * faceBreathingAmplitude).toFloat()

            if (finishAnimationFaceBreathing) {
                if (faceBreathingProgress > faceBreathingFinishCheckpoint + PI) {
                    finishAnimationFaceBreathing = false
                    completelyFinishFaceBreathing = true
                }
            } else {
                completelyFinishFaceBreathing = false
            }

            dirty = true
        } else if (!(settings["breathing"] as Boolean) and !completelyFinishFaceBreathing and !(settings["abrupt-animation-cancel"] as Boolean)) {
            finishAnimationFaceBreathing = true
            faceBreathingFinishCheckpoint = faceBreathingProgress
        }

        if (settings["breathing"] as Boolean or finishAnimationEyesMouthBreathing) {
            eyesMouthBreathingProgress += eyesMouthBreathingRate
            if (eyesMouthBreathingProgress > eyesMouthBreathingDelay) {
                val offset =
                    +sin(eyesMouthBreathingProgress - eyesMouthBreathingDelay) * eyesMouthBreathingAmplitude
                eyeLeft.y = eyeLeftAnchorY + offset
                eyeRight.y = eyeRightAnchorY + offset
                mouth.y = mouthAnchorY + offset
            }

            if (finishAnimationEyesMouthBreathing) {
                if (eyesMouthBreathingProgress > eyesMouthBreathingFinishCheckpoint + PI) {
                    finishAnimationEyesMouthBreathing = false
                    completelyFinishEyesMouthBreathing = true
                }
            } else {
                completelyFinishEyesMouthBreathing = false
            }

            dirty = true
        } else if (!(settings["breathing"] as Boolean) and !completelyFinishEyesMouthBreathing and !(settings["abrupt-animation-cancel"] as Boolean)) {
            finishAnimationEyesMouthBreathing = true
            eyesMouthBreathingFinishCheckpoint = eyesMouthBreathingProgress
        }
    }
}
