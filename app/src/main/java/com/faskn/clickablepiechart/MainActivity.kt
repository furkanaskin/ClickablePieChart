package com.faskn.clickablepiechart

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.faskn.lib.Slice
import com.faskn.lib.pieChart
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chart.setCenterColor(R.color.white)

        // Kotlin DSL example
        val pieChart1 = pieChart {
            slices {
                arrayOf(
                    Slice(Random.nextInt(1000, 3000).toFloat(), R.color.colorPrimary),
                    Slice(Random.nextInt(1000, 2000).toFloat(), R.color.colorPrimaryDark),
                    Slice(Random.nextInt(1000, 5000).toFloat(), R.color.materialIndigo600),
                    Slice(Random.nextInt(1000, 10000).toFloat(), R.color.colorAccent)
                )
            }

            sliceWidth { 80f }
            sliceStartPoint { 0f }
            clickListener { s, fl ->
                Log.d("ses", "s " + s)
                Log.d("ses", "f " + fl.toString())
            }
        }

        chart.setPieChart(pieChart1)
    }
}
