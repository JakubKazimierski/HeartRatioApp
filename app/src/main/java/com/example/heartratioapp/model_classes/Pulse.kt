package com.example.heartratioapp.model_classes

import android.graphics.Color

class Pulse(val bpm: Int) {
    enum class Diagnose(val color: Int) {
        CRITICALLY_LOW(Color.parseColor("#FF4D4D")),
        VERY_LOW(Color.parseColor("#FFCD69")),
        LOW(Color.parseColor("#E5FF6F")),
        EXCELLENT(Color.parseColor("#DFFF93")),
        HIGH(Color.parseColor("#E5FF6F")),
        VERY_HIGH(Color.parseColor("#FFCD69")),
        CRITICALLY_HIGH(Color.parseColor("#FF4D4D")),
        ERROR(Color.parseColor("#999999"))
    }

    fun evaluate(): Diagnose {
        return when (bpm) {
            0 -> Diagnose.ERROR
            in 1..40 -> Diagnose.CRITICALLY_LOW
            in 41..55 -> Diagnose.VERY_LOW
            in 56..70 -> Diagnose.LOW
            in 71..89 -> Diagnose.EXCELLENT
            in 90..110 -> Diagnose.HIGH
            in 111..125 -> Diagnose.VERY_HIGH
            else -> Diagnose.CRITICALLY_HIGH
        }
    }
}