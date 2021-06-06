package com.example.heartratioapp.history_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.heartratioapp.R
import com.example.heartratioapp.history_activity.recycler.HeartRatioEntry
import com.example.heartratioapp.history_activity.recycler.HistoryAdapter
import com.example.heartratioapp.model_classes.Pulse
import java.io.File
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HistoryActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val data: ArrayList<HeartRatioEntry> = simData()

        recycler = findViewById(R.id.historyRecycler)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        data.sort()
        recycler.adapter = HistoryAdapter(data)
    }

    private fun simData() : ArrayList<HeartRatioEntry> {

        var ratios = ArrayList<HeartRatioEntry>()


        val filepath = this.getExternalCacheDir().toString() +"/bpm.txt"

        val inputStream: InputStream = File(filepath).inputStream()
        val lineList = mutableListOf<String>()

        inputStream.bufferedReader().forEachLine { lineList.add(it) }
        lineList.forEach {

            val dateTime = LocalDateTime.parse(it.split("*")[1], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            ratios.add(HeartRatioEntry(dateTime, Pulse(it.split("*")[0].toInt())))
        }



        return ratios

    }
}