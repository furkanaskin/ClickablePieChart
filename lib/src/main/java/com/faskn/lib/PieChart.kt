package com.faskn.lib

/**
 * Created by turkergoksu on 12-Aug-20
 */

class PieChart private constructor(
    var slices: Array<Slice>,
    var clickListener: ((String, Float) -> Unit)? = null,
    var sliceStartPoint: Float = 0f,
    var sliceWidth: Float = 80f
) {
    data class Builder(
        private var slices: Array<Slice>,
        private var clickListener: ((String, Float) -> Unit)? = null,
        private var sliceStartPoint: Float? = 90f,
        private var sliceWidth: Float? = 80f
    ) {
        init {
            initScaledArcs()
        }

        fun setSlices(slices: Array<Slice>) = apply { this.slices = slices }
        fun setClickListener(clickListener: ((String, Float) -> Unit)) =
            apply { this.clickListener = clickListener }

        fun setSliceStartPoint(sliceStartPoint: Float) =
            apply { this.sliceStartPoint = sliceStartPoint }

        fun setSliceWidth(sliceWidth: Float) = apply { this.sliceWidth = sliceWidth }

        fun getSlices() = slices

        fun build(): PieChart =
            PieChart(
                slices,
                clickListener,
                sliceStartPoint!!,
                sliceWidth!!
            )

        private fun initScaledArcs() {
            slices.forEachIndexed { i, slice ->
                val scaledValue = (slice.dataPoint / getSumOfDataPoints()) * 360
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
}