package com.example.heartratioapp.settings_activity

import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.heartratioapp.R
import java.time.LocalTime

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        onClickTime()
    }

    private fun onClickTime() {
        val textView = findViewById<TextView>(R.id.textView)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)
        timePicker.setOnTimeChangedListener { _, hour, minute ->
            if (textView != null) {
                val msg = "Notification time is: $hour:$minute"
                textView.text = msg
                textView.visibility = ViewGroup.VISIBLE
            }
            val notifyTime : LocalTime = LocalTime.of(hour, minute)
            Settings.notifyTime = notifyTime
        }
    }
}