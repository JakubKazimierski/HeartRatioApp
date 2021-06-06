package com.example.heartratioapp.settings_activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.heartratioapp.R
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        onClickTime()
        activateSwitch()
    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private fun activateSwitch() {
        val switch = findViewById<Switch>(R.id.notificationSwitch)
        switch.isChecked = Settings.notifyOn
        switch.setOnCheckedChangeListener {_, checked ->
            Settings.notifyOn = checked
        }
    }

    private fun onClickTime() {
        val textView = findViewById<TextView>(R.id.textView)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)

        timePicker.hour = Settings.notifyTime.hour
        timePicker.minute = Settings.notifyTime.minute

        timePicker.setOnTimeChangedListener { _, hour, minute ->
            val setTime = LocalTime.of(hour, minute)
            val msg = "Notification time is: ${setTime.format(DateTimeFormatter.ofPattern("hh:mm"))}"
            textView.text = msg
            textView.visibility = ViewGroup.VISIBLE
            Settings.notifyTime = setTime
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Settings.saveSettings()
    }
}