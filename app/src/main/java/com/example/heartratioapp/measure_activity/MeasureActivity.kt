package com.example.heartratioapp.measure_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.heartratioapp.R
import com.example.heartratioapp.measure_activity.recorder.RecordThread
import com.example.heartratioapp.measure_activity.recorder.RecorderSurface

class MeasureActivity : AppCompatActivity() {
    private lateinit var btn : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measure)

        btn = findViewById<Button>(R.id.rec_start_btn)
        btn.setOnClickListener {
            startRec()
        }
    }

    private fun startRec() {
        // Start animation
        val thread : RecordThread = findViewById<RecorderSurface>(R.id.recorderSurface).recordThread
        thread.running = true
        thread.start()
        // Remove button
        btn.visibility = View.GONE

        //TODO: Implement Logic
    }

    fun endRec() {
        // Stop animation
        val sur = findViewById<RecorderSurface>(R.id.recorderSurface)
        sur.recordThread.running = false
        sur.visibility = View.GONE

        //TODO: Implement Logic
    }
}