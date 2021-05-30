package com.example.heartratioapp.measure_activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.heartratioapp.R
import com.example.heartratioapp.measure_activity.recorder.RecordThread
import com.example.heartratioapp.measure_activity.recorder.RecorderSurface

class MeasureActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measure)

        findViewById<Button>(R.id.rec_start_btn).setOnClickListener {
            it.visibility = View.GONE
            startRec()
        }
    }

    override fun onPause() {
        super.onPause()
        stopAnim()
        finish()
    }

    private fun startRec() {
        startAnim()

        //TODO: Replace with logic
        mockRecording()
    }

    private fun mockRecording() {
        val handler = Handler()
        val r = Runnable {
            endRec()
        }
        handler.postDelayed(r, 3000)
    }

    private fun endRec() {
        stopAnim()

        //TODO: Replace with logic
        findViewById<TextView>(R.id.debugFinish).visibility = View.VISIBLE
    }

    private fun startAnim() {
        val thread : RecordThread = findViewById<RecorderSurface>(R.id.recorderSurface).recordThread
        thread.running = true
        thread.start()
    }

    private fun stopAnim() {
        val sur = findViewById<RecorderSurface>(R.id.recorderSurface)
        sur.recordThread.running = false
        sur.visibility = View.GONE
    }
}