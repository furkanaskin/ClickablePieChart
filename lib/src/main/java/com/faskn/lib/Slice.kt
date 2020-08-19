package com.faskn.lib

/**
 * Created by turkergoksu on 12-Aug-20
 */

data class Slice(
    val dataPoint: Float,
    val color: Int,
    var arc: Arc? = null,
    var scaledValue : Float? = 0f
)

data class Arc(
    val startAngle: Float,
    val sweepAngle: Float
) {
    fun average(): Double =
        (startAngle / 2) + (sweepAngle / 2) + (((startAngle % 2) + (sweepAngle % 2)) / 2).toDouble()
}