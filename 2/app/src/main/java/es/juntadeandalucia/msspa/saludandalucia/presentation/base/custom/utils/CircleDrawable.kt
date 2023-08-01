package es.juntadeandalucia.msspa.saludandalucia.presentation.base.custom.utils

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.Typeface
import android.graphics.drawable.Drawable

class CircleDrawable(backgroundColor: Int) : Drawable() {

    private var circlePaint: Paint
    private var textPaint: Paint
    private val rect: Rect = Rect()
    private var count: Int = 0

    init {
        circlePaint = Paint()
        circlePaint.isAntiAlias = true
        circlePaint.style = Paint.Style.FILL
        circlePaint.color = backgroundColor
        textPaint = Paint()
        textPaint.color = Color.WHITE
        textPaint.typeface = Typeface.DEFAULT_BOLD
        textPaint.textSize = 25f
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
    }

    override fun draw(canvas: Canvas) {
        if (count == 0) {
            return
        }
        val bounds: Rect = bounds
        val width: Float = (bounds.right - bounds.left).toFloat()
        val radius = width / 3
        val x: Float = bounds.right - radius
        val y: Float = bounds.top.toFloat() + radius
        canvas.drawCircle(x, y, radius, circlePaint)
        textPaint.getTextBounds(count.toString(), 0, count.toString().length, rect)
        val height: Int = rect.bottom - rect.top
        val textY = y + height / 2
        canvas.drawText(count.toString(), x, textY, textPaint)
    }

    override fun setAlpha(alpha: Int) {}

    override fun setColorFilter(colorFilter: ColorFilter?) {}

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }

    fun setCount(count: Int) {
        this.count = count
    }
}
