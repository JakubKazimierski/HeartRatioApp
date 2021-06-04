package com.example.heartratioapp.measure_activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.heartratioapp.R
import com.example.heartratioapp.measure_activity.recorder.RecordThread
import com.example.heartratioapp.measure_activity.recorder.RecorderSurface


class MeasureActivity : AppCompatActivity() {

    lateinit var record : MediaRecorder
    lateinit var path : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measure)


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
        }

        record = MediaRecorder()

        findViewById<Button>(R.id.rec_start_btn).setOnClickListener {
            it.visibility = View.GONE


            startRec()
        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {

        }
    }

    override fun onPause() {
        super.onPause()
        stopAnim()
        finish()
    }

    private fun startRec() {
        startAnim()
        path = this.getExternalCacheDir().toString() + "/heartBeat.3gp"

        record.setAudioSource(MediaRecorder.AudioSource.MIC)
        record.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        record.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
        record.setOutputFile(path)
        record.prepare()
        record.start()

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

        record.stop()
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

