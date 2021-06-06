package com.example.heartratioapp.diagnose_activity

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaPlayer
import android.media.audiofx.BassBoost
import android.media.audiofx.NoiseSuppressor
import android.media.audiofx.Visualizer
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import be.tarsos.dsp.AudioEvent
import be.tarsos.dsp.GainProcessor
import be.tarsos.dsp.filters.HighPass
import be.tarsos.dsp.filters.LowPassFS
import be.tarsos.dsp.io.TarsosDSPAudioFormat
import com.example.heartratioapp.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.*

class DiagnoseActivity : AppCompatActivity(){

    lateinit var player : MediaPlayer
    lateinit var path : String
    lateinit var mVisualizer : com.gauravk.audiovisualizer.visualizer.BarVisualizer

    lateinit var visualizer: Visualizer






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE), 111)
        }


        setContentView(R.layout.activity_diagnose)


        //risky


    }




}