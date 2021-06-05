package com.example.heartratioapp.settings_activity

import java.time.LocalTime

class Settings {

    companion object {
        var notifyTime : LocalTime = LocalTime.of(23,9)
        var notifyOn : Boolean = true
        var emergencyNumber : String = "+48 000 000 000"
    }
}