package com.example.heartratioapp.history_activity.recycler

import com.example.heartratioapp.model_classes.Pulse
import java.time.LocalDateTime

data class HeartRatioEntry(var date:LocalDateTime, var heartRate: Pulse): Comparable<HeartRatioEntry> {

    override fun compareTo(other: HeartRatioEntry): Int {
        return this.date.compareTo(other.date)
    }
}