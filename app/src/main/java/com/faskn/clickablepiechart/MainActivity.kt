package com.faskn.clickablepiechart

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chart.setCenterColor(R.color.white)
     /*   chart.setStartPoint(-90f)*/
        chart.setSliceWidth(250f)
        chart.setListener { data, index ->
           /* Toast.makeText(this, index.toString(), Toast.LENGTH_SHORT).show()*/
        }

        chart.setSliceColor(
            intArrayOf(
                R.color.brown700,
                R.color.materialRed700,
                R.color.materialIndigo600,
                R.color.materialRed400
            )
        )
        chart.setDataPoints(floatArrayOf(30f, 60f, 120f, 150f))
    }
}