package com.example.heartratioapp.diagnose_activity

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.media.audiofx.BassBoost
import android.media.audiofx.NoiseSuppressor
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.heartratioapp.R
import com.example.heartratioapp.measure_activity.recorder.RecordThread
import com.example.heartratioapp.measure_activity.recorder.RecorderSurface

class DiagnoseActivity : AppCompatActivity(){

    lateinit var player : MediaPlayer
    lateinit var path : String
    lateinit var mVisualizer : com.gauravk.audiovisualizer.visualizer.BarVisualizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnose)
        startAnim()
    }


    private fun startAnim() {

        path = this.getExternalCacheDir().toString() + "/heartBeat.3gp"

        mVisualizer = findViewById(R.id.bar);
        player = MediaPlayer()
        val suppressor = NoiseSuppressor.create(
            player.audioSessionId)
        val bassBoost = BassBoost(0,player.audioSessionId)
        bassBoost.setStrength(1000)

        player.setDataSource(path)
        player.prepare()
        player.start()



        val audioSessionId: Int = player.getAudioSessionId()
        if (audioSessionId != -1){
            mVisualizer.setAudioSessionId(audioSessionId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mVisualizer != null) mVisualizer.release()
    }
}