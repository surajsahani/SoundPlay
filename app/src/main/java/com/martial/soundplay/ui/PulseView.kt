package com.martial.soundplay.ui

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.martial.soundplay.R

class PulseView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val ringPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        color = com.martial.soundplay.ThemeManager.get(context).primaryContainer
    }

    private var phase = 0f
    private var animator: ValueAnimator? = null

    fun startPulsing() {
        if (animator?.isRunning == true) return
        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 3000L
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                phase = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    fun stopPulsing() {
        animator?.cancel()
        animator = null
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val baseRadius = width.coerceAtMost(height) / 2f * 0.42f // inner circle radius

        // Draw 3 concentric rings that pulse outward
        for (i in 1..3) {
            val offset = (phase + i * 0.33f) % 1f
            val radius = baseRadius + (baseRadius * 0.8f * offset)
            val alpha = ((1f - offset) * 60).toInt().coerceIn(0, 255)
            ringPaint.alpha = alpha
            ringPaint.strokeWidth = 2f * resources.displayMetrics.density
            canvas.drawCircle(cx, cy, radius, ringPaint)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopPulsing()
    }
}
