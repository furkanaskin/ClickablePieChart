package com.faskn.lib

/**
 * Created by turkergoksu on 30-Aug-20
 */

@DslMarker
annotation class PieChartDsl
data class PieChart(
    var slices: ArrayList<Slice>,
    var clickListener: ((String, Float) -> Unit)?,
    var sliceStartPoint: Float?,
    var sliceWidth: Float?
) {
    fun build(): PieChart {
        initScaledArcs()
        initPercentages()
        return PieChart(slices, clickListener, sliceStartPoint, sliceWidth)
    }

    private fun initScaledArcs() {
        slices.forEachIndexed { i, slice ->
            val scaledValue = (slice.dataPoint / getSumOfDataPoints()) * 360
            slice.scaledValue = scaledValue
            if (i != 0) {
                slice.arc = Arc(
                    slices[i - 1].arc?.sweepAngle!!,
                    slices[i - 1].arc?.sweepAngle!!.plus(scaledValue)
                )
            } else {
                slice.arc = Arc(0f, scaledValue)
            }
        }
    }

    private fun initPercentages() {
        var remainder = 100
        slices.forEach { slice ->
            val percentage = (100 * slice.scaledValue!!.toInt()) / 360
            slice.percentage = percentage
            remainder -= percentage
        }
        var i = 0
        while (remainder != 0) {
            slices[i].percentage = slices[i].percentage!! + 1
            remainder -= 1
            i = (i + 1) % 4
        }
    }

    private fun getSumOfDataPoints(): Float {
        return slices.sumByDouble { slice -> slice.dataPoint.toDouble() }.toFloat()
    }
}

fun buildChart(block: PieChartBuilder.() -> Unit) = PieChartBuilder().apply(block).build()

@PieChartDsl
class PieChartBuilder {
    private lateinit var slices: ArrayList<Slice>
    private var clickListener: ((String, Float) -> Unit)? = null
    private var sliceStartPoint = 0f
    private var sliceWidth = 80f

    fun slices(block: () -> ArrayList<Slice>) {
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
        slices.forEachIndexed { i, slice ->
            val scaledValue = (slice.dataPoint / getSumOfDataPoints()) * 360
            slice.scaledValue = scaledValue
            if (i != 0) {
                slice.arc = Arc(
                    slices[i - 1].arc?.sweepAngle!!,
                    slices[i - 1].arc?.sweepAngle!!.plus(scaledValue)
                )
            } else {
                slice.arc = Arc(0f, scaledValue)
            }
        }
    }

    private fun getSumOfDataPoints(): Float {
        return slices.sumByDouble { slice -> slice.dataPoint.toDouble() }.toFloat()
    }
}