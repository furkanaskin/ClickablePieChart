package com.faskn.clickablepiechart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.faskn.lib.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Kotlin DSL example
        val pieChartDSL = buildChart {
            slices { provideSlices() }
            sliceWidth { 80f }
            sliceStartPoint { -90f }
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

        //OR SET WITH CUSTOMER LEGEND ADAPTER
        //chart2.setPieChart(pieChart)
        //chart2.showLegend(legendLayout2,CustomLegendAdapter())


        val barChart = BarChart(
            slices = provideSlices(), clickListener = null
        ).build()

        val barChartDSL = buildBarChart {
            slices { provideSlices() }
            clickListener { percentage, index ->
                // ...
            }
        }

        chart3.setBarChart(barChart)
        chart3.showLegend(legendLayout3)


        chart4.setBarChart(barChartDSL)
        chart4.showLegend(
            rootLayout = legendLayout4,
            layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        )
    }

    private fun provideSlices(): ArrayList<Slice> {
        return arrayListOf(
            Slice(
                2F,
                R.color.colorPrimary,
                "Google"
            ),
            Slice(
                2F,
                R.color.colorPrimaryDark,
                "Facebook"
            ),
            Slice(
                1F,
                R.color.materialIndigo600,
                "Twitter"
            ),
            Slice(
                4F,
                R.color.colorAccent,
                "Other"
            )
        )
    }
}
