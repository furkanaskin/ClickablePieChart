package com.faskn.lib

/**
 * Created by Furkan on 6.08.2020
 */

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


class ClickablePieChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var slicePaint: Paint = Paint()
    private var centerPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var sliceColors: IntArray = intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
    private var rectF: RectF? = null
    private var dataPoints: FloatArray = floatArrayOf()
    private var sliceStartPoint = 0F
    private var sliceWidth = 80f
    private var touchX = 0f
    private var touchY = 0f
    private var clickListener: ((String, Float) -> Unit)? = null
    private var pointsArray = arrayListOf<Pair<Float, Float>>()

    init {
        slicePaint.isAntiAlias = true
        slicePaint.isDither = true
        slicePaint.style = Paint.Style.FILL

        centerPaint.color = Color.WHITE
        centerPaint.style = Paint.Style.FILL
    }

    private fun scale(): FloatArray {
        val scaledValues = FloatArray(dataPoints.size)
        for (i in dataPoints.indices) {
            scaledValues.fill((dataPoints[i] / getTotal()) * 360, i, dataPoints.size)
        }
        return scaledValues
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        rectF = RectF(
            0f,
            0f,
            width.coerceAtMost(height).toFloat(),
            width.coerceAtMost(height).toFloat()
        )

        val scaledValues = scale()

        for (i in scaledValues.indices) {
            slicePaint.color = ContextCompat.getColor(context, sliceColors[i])
            canvas!!.drawArc(rectF!!, sliceStartPoint, scaledValues[i], true, slicePaint)
            pointsArray.add(Pair(sliceStartPoint, sliceStartPoint + scaledValues[i]))
            sliceStartPoint += scaledValues[i]
        }

        val centerX = (measuredWidth / 2).toFloat()
        val centerY = (measuredHeight / 2).toFloat()
        val radius = centerX.coerceAtMost(centerY)

        canvas!!.drawCircle(rectF!!.centerX(), rectF!!.centerY(), radius - sliceWidth, centerPaint)
    }

    private fun getTotal(): Float = dataPoints.sum()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.x
                touchY = event.y
                true
            }
            MotionEvent.ACTION_UP -> {
                var touchAngle = Math.toDegrees(
                    atan2(
                        touchY - (measuredHeight / 2).toDouble(),
                        touchX - (measuredWidth / 2).toDouble()
                    )
                )

                touchAngle -= sliceStartPoint
                touchAngle %= 360

                if (touchAngle < 0) {
                    touchAngle += 360.0
                }

                var total = 0.0f
                var forEachStopper = false // what a idiot stuff
                dataPoints.forEachIndexed { index, data ->
                    total += data % 360f
                    if (touchAngle <= total && !forEachStopper) {
                        clickListener?.invoke(touchAngle.toString(), index.toFloat())
                        forEachStopper = true
                        showInfoPopup(index)
                    }
                }
                forEachStopper = false
                true
            }
            else -> false
        }
    }

    private fun showInfoPopup(index: Int) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.layout_info, null)
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(popupView, width, height, true)
        var center = pointsArray[index].toList().average()
        val halfRadius = rectF!!.centerX()

        popupView.findViewById<TextView>(R.id.textViewInfo).text = center.toString()

        val calculatedX =
            ((halfRadius) * cos(Math.toRadians(center))).toInt()
        val calculatedY =
            ((halfRadius) * sin(Math.toRadians(center))).toInt()

        val currentViewLocation = IntArray(2)
        this.getLocationOnScreen(currentViewLocation)

        popupWindow.showAtLocation(
            this,
            Gravity.NO_GRAVITY,
            (currentViewLocation[0] + halfRadius.toInt()) + calculatedX,
            (currentViewLocation[1] + halfRadius.toInt()) + calculatedY
        )

        val currentData = dataPoints[index]

    }

    fun setSliceWidth(width: Float) {
        sliceWidth = width.p2d(context)
    }

    fun setListener(listener: (String, Float) -> (Unit)) {
        clickListener = listener
    }

    fun setStartPoint(point: Float) {
        sliceStartPoint = point
    }

    fun setDataPoints(data: FloatArray) {
        dataPoints = data
        invalidateAndRequestLayout()
    }

    fun setCenterColor(colorId: Int) {
        centerPaint.color = ContextCompat.getColor(context, colorId)
        invalidateAndRequestLayout()
    }

    fun setSliceColor(colors: IntArray) {
        sliceColors = colors
        invalidateAndRequestLayout()
    }

    private fun invalidateAndRequestLayout() {
        invalidate()
        requestLayout()
    }
}