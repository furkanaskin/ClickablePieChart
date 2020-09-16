package com.faskn.clickablepiechart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.faskn.lib.PieChart
import com.faskn.lib.Slice
import com.faskn.lib.buildChart
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Kotlin DSL example
        val pieChartDSL = buildChart {
            slices { provideSlices() }
            sliceWidth { 80f }
            sliceStartPoint { 0f }
            clickListener { angle, index ->
                // ...
            }
        }

        chart.setPieChart(pieChartDSL)
        chart.showLegend(legendLayout)

        val pieChart = PieChart(
            slices = provideSlices(), clickListener = null, sliceStartPoint = 0f, sliceWidth = 80f
        ).build()

        chart.setPieChart(pieChart)
        chart.showLegend(legendLayout)
    }

    private fun provideSlices(): ArrayList<Slice> {
        return arrayListOf(
            Slice(
                Random.nextInt(1000, 3000).toFloat(),
                R.color.colorPrimary,
                "Google"
            ),
            Slice(
                Random.nextInt(1000, 2000).toFloat(),
                R.color.colorPrimaryDark,
                "Facebook"
            ),
            Slice(
                Random.nextInt(1000, 5000).toFloat(),
                R.color.materialIndigo600,
                "Twitter"
            ),
            Slice(
                Random.nextInt(1000, 10000).toFloat(),
                R.color.colorAccent,
                "Other"
            )
        )
    }
}
