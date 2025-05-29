package com.example.voxai.presentation.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.voxai.R
import kotlin.math.min

class WaveformView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.accent)
        strokeWidth = 3f
        isAntiAlias = true
    }

    private var amplitudes = FloatArray(100) { 0f }
    private var currentIndex = 0

    fun addAmplitude(amplitude: Float) {
        amplitudes[currentIndex] = amplitude
        currentIndex = (currentIndex + 1) % amplitudes.size
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        
        val width = width.toFloat()
        val height = height.toFloat()
        val centerY = height / 2
        val barWidth = width / amplitudes.size

        for (i in amplitudes.indices) {
            val amplitude = amplitudes[i] * height / 2
            val left = i * barWidth
            val top = centerY - amplitude
            val right = left + barWidth
            val bottom = centerY + amplitude

            canvas.drawRect(left, top, right, bottom, paint)
        }
    }
} 