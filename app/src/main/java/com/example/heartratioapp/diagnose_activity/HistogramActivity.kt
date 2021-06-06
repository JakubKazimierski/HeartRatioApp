package com.example.heartratioapp.diagnose_activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.heartratioapp.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import java.util.*

class HistogramActivity : AppCompatActivity() {

    var barChart: BarChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histogram)
        barChart = findViewById<View>(R.id.barGraph) as BarChart
        val intent: Intent = getIntent()
        val a = intent.getIntArrayExtra("ARRAY")
        if (a != null) {
            show(a)
        }
    }

    fun show(array: IntArray) {
        val entries = ArrayList<BarEntry>()
        for (i in 0..89) {
            val x = 250 + i * 25
            val y = array[i]
            entries.add(BarEntry(x.toFloat(), y.toFloat()))
        }
        val barDataSet = BarDataSet(entries, "Liczba uderzeń w poszczególnych przedziałach")
        val timestamps = ArrayList<String>()
        for (i in 0..89) {
            val builder = StringBuilder()
            builder.append(250 + 25 * i)
            builder.append(" - ")
            builder.append(250 + 25 * (i + 1))
            timestamps.add(builder.toString())
        }
        val theData = BarData(barDataSet)
        theData.barWidth = 25f
        theData.setDrawValues(false)
        barChart!!.data = theData
        barChart!!.setTouchEnabled(true)
        barChart!!.isDragEnabled = true
        barChart!!.setScaleEnabled(true)
        barChart!!.setFitBars(true) // make the x-axis fit exactly all bars
        barChart!!.invalidate() // refresh
    }

}