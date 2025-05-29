package com.example.voxai.presentation.ui.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.content.ContextCompat
import com.example.voxai.R

class RecordingStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        isAntiAlias = true
        color = ContextCompat.getColor(context, R.color.accent)
    }

    private var isRecording = false
    private var pulseRadius = 0f
    private var pulseAlpha = 255
    private var animator: ValueAnimator? = null

    fun setRecording(recording: Boolean) {
        if (isRecording != recording) {
            isRecording = recording
            if (recording) {
                startPulseAnimation()
            } else {
                stopPulseAnimation()
            }
            invalidate()
        }
    }

    private fun startPulseAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1000
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                pulseRadius = animation.animatedValue as Float * width / 2
                pulseAlpha = ((1 - animation.animatedValue as Float) * 255).toInt()
                invalidate()
            }
            start()
        }
    }

    private fun stopPulseAnimation() {
        animator?.cancel()
        animator = null
        pulseRadius = 0f
        pulseAlpha = 255
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val baseRadius = width.coerceAtMost(height) / 4f

        // Dibujar círculo base
        paint.alpha = 255
        canvas.drawCircle(centerX, centerY, baseRadius, paint)

        // Dibujar pulso si está grabando
        if (isRecording) {
            paint.alpha = pulseAlpha
            canvas.drawCircle(centerX, centerY, baseRadius + pulseRadius, paint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopPulseAnimation()
    }
} 