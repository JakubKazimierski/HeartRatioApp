package com.example.heartratioapp.settings_activity

import android.content.SharedPreferences
import java.time.LocalTime

class Settings {

    companion object {
        private var sharedPref : SharedPreferences? = null
        var notifyTime : LocalTime = LocalTime.of(23,9)
        var notifyOn : Boolean = true
        var emergencyNumber : String = "+48 000 000 000"

        fun saveSettings() {
            sharedPref!!.edit().apply {
                putString("EmergencyNumber", emergencyNumber)
                putBoolean("NotifyOn", notifyOn)
                putInt("NotifyMinute", notifyTime.minute)
                putInt("NotifyHour", notifyTime.hour)
                apply()
            }
        }

        fun loadSettings(pref : SharedPreferences) {
            sharedPref = pref
            emergencyNumber = pref.getString("EmergencyNumber", "111").toString()
            notifyOn = pref.getBoolean("NotifyOn", true)
            val minute = pref.getInt("NotifyMinute", 0)
            val hour = pref.getInt("NotifyHour", 12)
            notifyTime = LocalTime.of(hour, minute)
        }
    }
}