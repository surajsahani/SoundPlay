package com.martial.soundplay.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.martial.soundplay.ThemeManager

class TimerRingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f * resources.displayMetrics.density
        color = 0x20000000 // very light grey
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f * resources.displayMetrics.density
        strokeCap = Paint.Cap.ROUND
        color = ThemeManager.get(context).primary
    }

    var progress: Float = 0f // 0..1
        set(value) {
            field = value.coerceIn(0f, 1f)
            invalidate()
        }

    private val rect = RectF()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val pad = progressPaint.strokeWidth / 2f + 4f
        rect.set(pad, pad, width - pad, height - pad)

        // Background ring
        canvas.drawArc(rect, 0f, 360f, false, bgPaint)

        // Progress arc
        if (progress > 0f) {
            canvas.drawArc(rect, -90f, 360f * progress, false, progressPaint)
        }
    }
}
