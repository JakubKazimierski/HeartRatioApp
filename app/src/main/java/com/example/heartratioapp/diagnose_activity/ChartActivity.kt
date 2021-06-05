package com.example.heartratioapp.diagnose_activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.heartratioapp.R
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet

import java.util.*

class ChartActivity : AppCompatActivity() {

    var lineChart: LineChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        val intent = intent
        val audioBytes = intent.getFloatArrayExtra("floats")

        lineChart = findViewById(R.id.linechart)

        val lineDataSet = LineDataSet(lineChartDataSet(audioBytes), "data set")
        val iLineDataSets = ArrayList<ILineDataSet>()
        iLineDataSets.add(lineDataSet)

        val lineData = LineData(iLineDataSets)

//        lineChart.setData(lineData)
//
//        lineDataSet.setDrawCircles(false)
//        lineDataSet.color = Color.RED
//
//        val xAxis = lineChart.getXAxis()
//        xAxis.axisMaximum = 30f
//        lineChart.invalidate()
    }


    private fun lineChartDataSet(audiobytes: FloatArray?): ArrayList<Entry>? {
        val dataSet =
            ArrayList<Entry>()
        for (i in audiobytes!!.indices) {
            val x = i / 44.1f
            val y = audiobytes!![i]
            dataSet.add(Entry(x, y))
        }
        return dataSet
    }

}