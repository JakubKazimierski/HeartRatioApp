package com.example.heartratioapp.history_activity.recycler

import java.time.LocalDateTime

data class HeartRatioEntry(var date:LocalDateTime, var bpm:Float): Comparable<HeartRatioEntry> {

    override fun compareTo(other: HeartRatioEntry): Int {
        return this.date.compareTo(other.date)
    }
}