package com.faskn.lib

/**
 * Created by Furkan on 6.08.2020
 */

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.widget.ImageViewCompat
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


class ClickablePieChart @JvmOverloads constructor(
    context: Context,
    private val attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var slicePaint: Paint = Paint()
    private var centerPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var rectF: RectF? = null
    private var touchX = 0f
    private var touchY = 0f

    // PieChart variables
    private var pieChart: PieChart? = null
    private lateinit var slices: List<Slice>

    // Animation variables
    private var animator: ValueAnimator? = null
    private var currentSweepAngle = 0

    // Attributes
    private var popupText: String? = null

    init {
        initAttributes(attrs)
    }

    private fun init() {
        slicePaint.isAntiAlias = true
        slicePaint.isDither = true
        slicePaint.style = Paint.Style.FILL

        centerPaint.color = Color.WHITE
        centerPaint.style = Paint.Style.FILL

        initSlices()
        startAnimation()
    }

    private fun initAttributes(attrs: AttributeSet?) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.ClickablePieChart, 0, 0)

        try {
            popupText = typedArray.getString(R.styleable.ClickablePieChart_popupText) ?: ""
        } finally {
            typedArray.recycle()
        }
    }

    private fun initSlices() {
        slices = pieChart?.slices?.toList()!!
    }

    private fun startAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofInt(0, 360).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                currentSweepAngle = valueAnimator.animatedValue as Int
                invalidate()
            }
        }
        animator?.start()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        rectF = RectF(
            0f,
            0f,
            width.coerceAtMost(height).toFloat(),
            width.coerceAtMost(height).toFloat()
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (pieChart != null) {
            slices.forEach { slice ->
                val arc = slice.arc!!
                if (currentSweepAngle > arc.startAngle + arc.sweepAngle) {
                    slicePaint.color = ContextCompat.getColor(context, slice.color)
                    canvas?.drawArc(
                        rectF!!,
                        pieChart?.sliceStartPoint!! + arc.startAngle,
                        arc.sweepAngle,
                        true,
                        slicePaint
                    )
                } else {
                    if (currentSweepAngle > arc.startAngle) {
                        slicePaint.color = ContextCompat.getColor(context, slice.color)
                        canvas?.drawArc(
                            rectF!!,
                            pieChart?.sliceStartPoint!! + arc.startAngle,
                            currentSweepAngle - arc.startAngle,
                            true,
                            slicePaint
                        )
                    }
                }
            }

            val centerX = (measuredWidth / 2).toFloat()
            val centerY = (measuredHeight / 2).toFloat()
            val radius = centerX.coerceAtMost(centerY)

            canvas!!.drawCircle(
                rectF!!.centerX(),
                rectF!!.centerY(),
                radius - pieChart?.sliceWidth!!,
                centerPaint
            )
        }
    }

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

                Log.v("qqq", touchAngle.toString())


                touchAngle -= pieChart?.sliceStartPoint ?: 0f

                if (touchAngle < 0) {
                    touchAngle += 360.0
                }

                Log.v("qqq", touchAngle.toString())

                var total = 0.0f
                var forEachStopper = false // what a idiot stuff
                slices.forEachIndexed { index, slice ->
                    total += (slice.dataPoint) % 360f
                    if (touchAngle <= total && !forEachStopper) {
                        pieChart?.clickListener?.invoke(touchAngle.toString(), index.toFloat())
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
        val popupView = inflater.inflate(R.layout.popup_slice, null)
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(popupView, width, height, true)
        var center = slices[index].arc?.average()!! + pieChart?.sliceStartPoint?.toDouble()!!
        val halfRadius = rectF!!.centerX()

        popupView.findViewById<TextView>(R.id.textViewPopupText).text =
            "${slices[index].arc?.average()?.toInt()} $popupText"
        ImageViewCompat.setImageTintList(
            popupView.findViewById(R.id.imageViewPopupCircleIndicator),
            ColorStateList.valueOf(ContextCompat.getColor(context, slices[index].color))
        )

        val calculatedX =
            ((halfRadius) * cos(Math.toRadians(center))).toInt()
        val calculatedY =
            ((halfRadius) * sin(Math.toRadians(center))).toInt()

        val currentViewLocation = IntArray(2)
        this.getLocationOnScreen(currentViewLocation)

        val halfOfSliceWidth = (pieChart?.sliceWidth?.p2d(context)!! / 2).toInt()
        val popupWindowX =
            (currentViewLocation[0] + halfRadius.toInt()) + calculatedX -
                    (if (calculatedX < 0) -halfOfSliceWidth else halfOfSliceWidth)
        val popupWindowY =
            (currentViewLocation[1] + halfRadius.toInt()) + calculatedY -
                    (if (calculatedY < 0) -halfOfSliceWidth else halfOfSliceWidth)
        popupWindow.showAtLocation(
            this,
            Gravity.NO_GRAVITY,
            popupWindowX,
            popupWindowY
        )

        popupView.doOnPreDraw {
            popupWindow.update(
                (popupWindowX - (it.width / 2)),
                popupWindowY,
                popupWindow.width,
                popupWindow.height
            )
        }
    }

    fun setPieChart(pieChart: PieChart) {
        this.pieChart = pieChart
        init()
        invalidateAndRequestLayout()
    }

    fun setCenterColor(colorId: Int) {
        centerPaint.color = ContextCompat.getColor(context, colorId)
        invalidateAndRequestLayout()
    }

    private fun invalidateAndRequestLayout() {
        invalidate()
        requestLayout()
    }
}