package com.faskn.lib

/**
 * Created by Furkan on 6.08.2020
 */

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
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

class ClickableBarChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var slicePaint: Paint = Paint().apply {
        isAntiAlias = true
        isDither = true
        style = Paint.Style.FILL
    }

    private var touchX = 0f
    private var touchY = 0f

    // PieChart variables
    private var barChart: BarChart? = null
    private var slices: ArrayList<Slice>? = null

    // Animation variables
    private var animator: ValueAnimator? = null
    private var showPopup = true
    private var animationDuration: Int = 1000

    // Attributes
    private var popupText: String? = null
    private var showPercentage = false
    private var currentAnimationPercentage = 0

    private var orientation: Orientation = Orientation.HORIZONTAL


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

            showPopup = typedArray.getBoolean(R.styleable.ClickablePieChart_showPopup, true)

            orientation = Orientation.valueOf(
                typedArray.getString(R.styleable.ClickablePieChart_orientation)?.toUpperCase()
                    ?: Orientation.VERTICAL.name
            )

        } finally {
            typedArray.recycle()
        }
    }

    private fun startAnimation() {
        animator?.cancel()
        animator = ValueAnimator.ofInt(0, 100).apply {
            duration = animationDuration.toLong()
            interpolator = LinearInterpolator()
            addUpdateListener { valueAnimator ->
                currentAnimationPercentage = valueAnimator.animatedValue as Int
                invalidate()
            }
        }
        animator?.start()
    }

    private fun initSlices() {
        slices = barChart?.slices
    }


    /*override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        rectF = RectF(
            0f,
            0f,
            width.coerceAtMost(height).toFloat(),
            width.coerceAtMost(height).toFloat()
        )
    }*/

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        var startPercentage = 0F

        if (slices.isNullOrEmpty().not()) {
            slices?.forEach { slice ->

                var endPercentage = (startPercentage + slice.percentage!!.toFloat())
                slicePaint.color = ContextCompat.getColor(context, slice.color)

                if (orientation == Orientation.HORIZONTAL) {

                    if (startPercentage < currentAnimationPercentage && endPercentage <= currentAnimationPercentage) {
                        canvas.drawRect(
                            (measuredWidth * startPercentage) / 100,
                            0F,
                            ((measuredWidth * (startPercentage + slice.percentage!!.toFloat())) / 100),
                            measuredHeight.toFloat(),
                            slicePaint
                        )
                    } else if (startPercentage < currentAnimationPercentage && endPercentage > currentAnimationPercentage) {
                        canvas.drawRect(
                            (measuredWidth * startPercentage) / 100,
                            0F,
                            ((measuredWidth * currentAnimationPercentage) / 100).toFloat(),
                            measuredHeight.toFloat(),
                            slicePaint
                        )
                    }

                } else {

                    if (startPercentage < currentAnimationPercentage && endPercentage <= currentAnimationPercentage) {
                        canvas.drawRect(
                            0F,
                            measuredHeight - (((measuredHeight * (startPercentage + slice.percentage!!.toFloat())) / 100)),
                            measuredWidth.toFloat(),
                            measuredHeight - ((measuredHeight * startPercentage) / 100),
                            slicePaint
                        )
                    } else if (startPercentage < currentAnimationPercentage && endPercentage > currentAnimationPercentage) {
                        canvas.drawRect(
                            0F,
                            measuredHeight - (((measuredHeight * currentAnimationPercentage) / 100).toFloat()),
                            measuredWidth.toFloat(),
                            measuredHeight - ((measuredHeight * startPercentage) / 100),
                            slicePaint
                        )
                    }
                }



                startPercentage = endPercentage
            }
        } else {
            slicePaint.color = ContextCompat.getColor(context, R.color.semiGray)
            canvas.drawRect(
                0F,
                measuredHeight.toFloat(),
                measuredWidth.toFloat(),
                0F,
                slicePaint
            )
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchX = event.x
                touchY = event.y
                true
            }
            MotionEvent.ACTION_UP -> {

                touchX = event.x
                touchY = event.y


                var currentPercentage = if (orientation == Orientation.VERTICAL) {
                    100 - ((touchY * 100) / measuredHeight)
                } else {
                    (touchX * 100) / measuredWidth
                }
                var calculatedPercentage = 0F

                run {
                    slices?.forEachIndexed { index, slice ->

                        val start = calculatedPercentage
                        val end = calculatedPercentage + slice.percentage!!.toFloat()

                        if (start <= currentPercentage && end > currentPercentage && showPopup) {
                            barChart?.clickListener?.invoke(
                                calculatedPercentage.toString(),
                                index.toFloat()
                            )

                            showInfoPopup(index, event, ((start + end) / 2).toInt())
                            return@run
                        }

                        calculatedPercentage = end
                    }
                }
                true
            }
            else -> false
        }
    }

    private fun showInfoPopup(index: Int, event: MotionEvent, center: Int) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_slice, null)
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val popupWindow = PopupWindow(popupView, width, height, true)
        var parent = this

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

        popupWindow.setBackgroundDrawable(ColorDrawable())
        popupWindow.showAtLocation(
            this,
            Gravity.NO_GRAVITY,
            event.x.toInt(),
            event.y.toInt()
        )

        val currentViewLocation = IntArray(2)
        this.getLocationOnScreen(currentViewLocation)


        if (orientation == Orientation.VERTICAL) {
            popupView.doOnPreDraw {
                popupWindow.update(
                    currentViewLocation[0] + (parent.measuredWidth / 2) - (it.width / 2),
                    (currentViewLocation[1] +  parent.measuredHeight - (parent.measuredHeight * center) / 100),
                    popupWindow.width,
                    popupWindow.height
                )
            }
        } else {
            popupView.doOnPreDraw {
                popupWindow.update(
                    currentViewLocation[0] + ((parent.measuredWidth * center) / 100) - (it.width / 2),
                    currentViewLocation[1] + (parent.measuredHeight / 2),
                    popupWindow.width,
                    popupWindow.height
                )
            }
        }


    }

    fun setBarChart(barChart: BarChart) {
        this.barChart = barChart
        init()
        invalidateAndRequestLayout()
    }


    fun showPopup(show: Boolean) {
        showPopup = show
    }

    fun showLegend(
        rootLayout: ViewGroup,
        adapter: LegendAdapter = LegendAdapter(),
        orientation: Int = LinearLayoutManager.VERTICAL
    ) {
        val recyclerView = RecyclerView(context)
        val linearLayoutManager =
            LinearLayoutManager(
                context, if (orientation == LinearLayoutManager.VERTICAL) {
                    LinearLayoutManager.VERTICAL
                } else {
                    LinearLayoutManager.HORIZONTAL
                }, false
            )
        recyclerView.layoutManager = linearLayoutManager
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


    enum class Orientation {
        VERTICAL,
        HORIZONTAL;
    }
}