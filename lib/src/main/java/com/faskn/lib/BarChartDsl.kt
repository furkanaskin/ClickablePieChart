package com.faskn.lib

/**
 * Created by turkergoksu on 30-Aug-20
 */

@DslMarker
annotation class BarChartDsl
data class BarChart(
    var slices: ArrayList<Slice>,
    var clickListener: ((String, Float) -> Unit)?
) {
    fun build(): BarChart {
        initScaledValues()
        initPercentages()
        return BarChart(slices, clickListener)
    }

    private fun initScaledValues() {
        slices.forEachIndexed { _, slice ->
            slice.scaledValue =  (slice.dataPoint / getSumOfDataPoints())
        }
    }

    private fun initPercentages() {
        var remainder = 100
        slices.forEach { slice ->
            val percentage = (100 * slice.scaledValue!!.toInt())
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

fun buildBarChart(block: BarChartBuilder.() -> Unit) = BarChartBuilder().apply(block).build()

@PieChartDsl
class BarChartBuilder {
    private lateinit var slices: ArrayList<Slice>
    private var clickListener: ((String, Float) -> Unit)? = null

    fun slices(block: () -> ArrayList<Slice>) {
        slices = block()
    }

    fun clickListener(block: ((String, Float) -> Unit)?) {
        clickListener = block
    }


    fun build(): BarChart {
        initScaledValues()
        initPercentages()
        return BarChart(slices, clickListener)
    }

    private fun initScaledValues() {
        slices.forEachIndexed { i, slice ->
            val scaledValue = slice.dataPoint / getSumOfDataPoints()
            slice.scaledValue = scaledValue
        }
    }

    private fun initPercentages() {
        slices.forEachIndexed { index,slice ->
            val percentage = (100 * slice.scaledValue!!).toInt()
            slices[index].percentage = percentage
        }
    }

    private fun getSumOfDataPoints(): Float {
        return slices.sumByDouble { slice -> slice.dataPoint.toDouble() }.toFloat()
    }
}