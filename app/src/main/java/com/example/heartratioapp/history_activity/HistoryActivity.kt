package com.example.heartratioapp.history_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.heartratioapp.R
import com.example.heartratioapp.history_activity.recycler.HeartRatioEntry
import com.example.heartratioapp.history_activity.recycler.HistoryAdapter
import java.time.LocalDateTime

class HistoryActivity : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    private val data: Array<HeartRatioEntry> = simData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recycler = findViewById(R.id.historyRecycler)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        data.sort()
        recycler.adapter = HistoryAdapter(data)
    }

    private fun simData() : Array<HeartRatioEntry>{
        return arrayOf(
            HeartRatioEntry(LocalDateTime.of(2021, 5, 12, 10,43), 70f),
            HeartRatioEntry(LocalDateTime.of(2021, 5, 14, 12,43), 90f),
            HeartRatioEntry(LocalDateTime.of(2021, 5, 13, 11,5), 65f),
            HeartRatioEntry(LocalDateTime.of(2021, 5, 15, 10,50), 74f)
        )
    }
}