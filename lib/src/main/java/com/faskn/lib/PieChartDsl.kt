package com.faskn.lib

/**
 * Created by turkergoksu on 30-Aug-20
 */

@DslMarker
annotation class PieChartDsl
data class PieChart(
    var slices: Array<Slice>?,
    var clickListener: ((String, Float) -> Unit)?,
    var sliceStartPoint: Float?,
    var sliceWidth: Float?
)

fun pieChart(block: PieChartBuilder.() -> Unit) = PieChartBuilder().apply(block).build()

@PieChartDsl
class PieChartBuilder {
    private var slices: Array<Slice>? = null
    private var clickListener: ((String, Float) -> Unit)? = null
    private var sliceStartPoint = 0f
    private var sliceWidth = 80f

    fun slices(block: () -> Array<Slice>) {
        slices = block()
    }

    fun clickListener(block: ((String, Float) -> Unit)?) {
        clickListener = block
    }

    fun sliceStartPoint(block: () -> Float) {
        sliceStartPoint = block()
    }

    fun sliceWidth(block: () -> Float) {
        sliceWidth = block()
    }

    fun build(): PieChart {
        initScaledArcs()
        return PieChart(slices, clickListener, sliceStartPoint, sliceWidth)
    }

    private fun initScaledArcs() {
        if (slices != null) {
            slices?.forEachIndexed { i, slice ->
                val scaledValue = (slice.dataPoint / getSumOfDataPoints()) * 360
                slice.scaledValue = scaledValue
                if (i != 0) {
                    slice.arc = Arc(
                        slices!![i - 1].arc?.sweepAngle!!,
                        slices!![i - 1].arc?.sweepAngle!!.plus(scaledValue)
                    )
                } else {
                    slice.arc = Arc(0f, scaledValue)
                }
            }
        }
    }

    private fun getSumOfDataPoints(): Float {
        return slices?.sumByDouble { slice -> slice.dataPoint.toDouble() }?.toFloat() ?: 0f
    }
}