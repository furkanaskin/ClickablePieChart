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
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faskn.lib.legend.LegendAdapter
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

class ClickablePieChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var slicePaint: Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL
    }
    private var centerPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }
    private var rectF: RectF? = null
    private var touchX = 0f
    private var touchY = 0f

    // PieChart variables
    private var pieChart: PieChart? = null
    private var slices: ArrayList<Slice>? = null

    // Animation variables
    private var animator: ValueAnimator? = null
    private var currentSweepAngle = 0
    private var showPopup = true
    private var animationDuration: Int = 1000

    // Attributes
    private var popupText: String? = null
    private var showPercentage = false

    private var defaultLayoutManager =
        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


    init {
        initAttributes(attrs)
    }

    private fun init() {
        initSlices()
        startAnimation()
    }

    private fun initAttributes(attrs: AttributeSet?) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.ClickablePieChart, 0, 0)

        try {
            popupText = typedArray.getString(R.styleable.ClickablePieChart_popupText) ?: ""

            showPercentage =
                typedArray.getBoolean(R.styleable.ClickablePieChart_showPercentage, false)

            animationDuration =
                abs(typedArray.getInt(R.styleable.ClickablePieChart_animationDuration, 0))

            centerPaint.color = typedArray.getColor(
                R.styleable.ClickablePieChart_centerColor,
                ContextCompat.getColor(context, android.R.color.white)
            )

            showPopup = typedArray.getBoolean(R.styleable.ClickablePieChart_showPopup, true)
        } finally {
            typedArray.recycle()
        }
    }

    private fun initSlices() {
        slices = pieChart?.slices
    }

    private fun startAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofInt(0, 360).apply {
            duration = animationDuration.toLong()
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

        val centerX = (measuredWidth / 2).toFloat()
        val centerY = (measuredHeight / 2).toFloat()
        val radius = centerX.coerceAtMost(centerY)

        if (slices.isNullOrEmpty().not()) {
            slices?.forEach { slice ->
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

            canvas!!.drawCircle(
                rectF!!.centerX(),
                rectF!!.centerY(),
                radius - pieChart?.sliceWidth!!,
                centerPaint
            )

        } else {
            val width = pieChart?.sliceWidth ?: 80f
            slicePaint.color = ContextCompat.getColor(context, R.color.semiGray)
            canvas!!.drawArc(rectF!!, 0f, 360f, true, slicePaint)
            canvas.drawCircle(
                rectF!!.centerX(),
                rectF!!.centerY(),
                radius - width,
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

                touchAngle -= pieChart?.sliceStartPoint ?: 0f

                if (touchAngle < 0) {
                    touchAngle += 360.0
                }

                var total = 0.0f
                run {
                    slices?.forEachIndexed { index, slice ->
                        total += (slice.scaledValue ?: 0f) % 360f
                        if (touchAngle <= total && showPopup) {
                            pieChart?.clickListener?.invoke(touchAngle.toString(), index.toFloat())
                            showInfoPopup(index)
                            return@run
                        }
                    }
                }
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
        val center = slices?.get(index)?.arc?.average()!! + pieChart?.sliceStartPoint?.toDouble()!!
        val halfRadius = rectF!!.centerX()

        var popupText = "${slices?.get(index)!!.dataPoint.toInt()} $popupText"
        if (showPercentage) {
            popupText = "$popupText (%${slices?.get(index)!!.percentage})"
        }
        popupView.findViewById<TextView>(R.id.textViewPopupText).text = popupText

        ImageViewCompat.setImageTintList(
            popupView.findViewById(R.id.imageViewPopupCircleIndicator),
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    slices?.get(index)?.color ?: R.color.semiGray
                )
            )
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

        popupWindow.setBackgroundDrawable(ColorDrawable())
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

    fun showPopup(show: Boolean) {
        showPopup = show
    }

    fun showLegend(
        rootLayout: ViewGroup,
        adapter: LegendAdapter = LegendAdapter(),
        layoutManager: RecyclerView.LayoutManager = defaultLayoutManager
    ) {
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        slices?.toMutableList()?.let { adapter.setup(it) }
        recyclerView.overScrollMode = OVER_SCROLL_NEVER
        rootLayout.addView(recyclerView)
        invalidateAndRequestLayout()
    }

    private fun invalidateAndRequestLayout() {
        invalidate()
        requestLayout()
    }
}