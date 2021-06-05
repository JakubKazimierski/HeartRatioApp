package com.example.heartratioapp.diagnose_activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.heartratioapp.R

class StatsActivity : AppCompatActivity(){


    var t1: TextView? = null
    var t2:TextView? = null
    var t3:TextView? = null
    var t4:TextView? = null
    var t5:TextView? = null
    var t6:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        val intent = intent
        val max = Math.round(intent.getDoubleExtra("max", 0.0)).toDouble()
        val min = Math.round(intent.getDoubleExtra("min", 0.0)).toDouble()
        val SDNN =
            Math.round(intent.getDoubleExtra("SDNN", 0.0)).toDouble()
        val MSSD =
            Math.round(intent.getDoubleExtra("MSSD", 0.0)).toDouble()
        val mean =
            Math.round(intent.getDoubleExtra("mean", 0.0)).toDouble()
        val numberOfIntervals = intent.getIntExtra("numOfInt", 0)
        t1 = findViewById<View>(R.id.textView6) as TextView
        t2 = findViewById<View>(R.id.textView7) as TextView
        t3 = findViewById<View>(R.id.textView8) as TextView
        t4 = findViewById<View>(R.id.textView9) as TextView
        t5 = findViewById<View>(R.id.textView10) as TextView
        t6 = findViewById<View>(R.id.textView11) as TextView
        t1!!.append(mean.toString())
        t2!!.append(max.toString())
        t3!!.append(min.toString())
        t4!!.append(SDNN.toString())
        t5!!.append(MSSD.toString())
        t6!!.append(numberOfIntervals.toString())
    }

}