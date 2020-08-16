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
                Slice(30f, R.color.brown700),
                Slice(60f, R.color.materialRed700),
                Slice(120f, R.color.materialIndigo600),
                Slice(150f, R.color.materialRed400)
            )
        )
            .setClickListener { string, float ->
                Log.d("ses", "s " + string)
                Log.d("ses", "f " + float.toString())
            }
            .build()

        // Example 2
        val pieChart1 = PieChart.Builder(
            arrayOf(
                Slice(Random.nextInt(0, 100).toFloat(), R.color.brown700),
                Slice(Random.nextInt(0, 100).toFloat(), R.color.materialRed700),
                Slice(Random.nextInt(0, 100).toFloat(), R.color.materialIndigo600),
                Slice(Random.nextInt(0, 100).toFloat(), R.color.materialRed400)
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