package com.faskn.clickablepiechart

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.faskn.lib.PieChart
import com.faskn.lib.Slice
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chart.setCenterColor(R.color.white)

        // Example
        val pieChart0 = PieChart.Builder(
            arrayOf(
                Slice(30f, R.color.colorPrimary),
                Slice(60f, R.color.colorPrimaryDark),
                Slice(120f, R.color.materialIndigo600),
                Slice(150f, R.color.colorAccent)
            )
        ).setSliceStartPoint(-90f)
            .setClickListener { string, float ->
                Log.d("ses", "s " + string)
                Log.d("ses", "f " + float.toString())
            }
            .build()

        // Example 2
        val pieChart1 = PieChart.Builder(
            arrayOf(
                Slice(Random.nextInt(0, 100).toFloat(), R.color.colorPrimary),
                Slice(Random.nextInt(0, 100).toFloat(), R.color.colorPrimaryDark),
                Slice(Random.nextInt(0, 100).toFloat(), R.color.materialIndigo600),
                Slice(Random.nextInt(0, 100).toFloat(), R.color.colorAccent)
            )
        )
            .setClickListener { string, float ->
                Log.d("ses", "s " + string)
                Log.d("ses", "f " + float.toString())
            }
            .build()

        chart.setPieChart(pieChart0)
    }
}