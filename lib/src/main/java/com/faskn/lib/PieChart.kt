package com.faskn.lib

/**
 * Created by turkergoksu on 12-Aug-20
 */

class PieChart private constructor() {
    data class Builder(
        private var slices: Array<Slice>,
        private var clickListener: ((String, Float) -> Unit)? = null,
        private var sliceStartPoint: Float? = 0f,
        private var sliceWidth: Float? = 80f
    ) {
        fun setSlices(slices: Array<Slice>) = apply { this.slices = slices }
        fun setClickListener(clickListener: ((String, Float) -> Unit)) =
            apply { this.clickListener = clickListener }

        fun setSliceStartPoint(sliceStartPoint: Float) =
            apply { this.sliceStartPoint = sliceStartPoint }

        fun setSliceWidth(sliceWidth: Float) = apply { this.sliceWidth = sliceWidth }

        fun getSlices() = slices
    }
}