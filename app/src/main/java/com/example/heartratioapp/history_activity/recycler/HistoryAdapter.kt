package com.example.heartratioapp.history_activity.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.heartratioapp.R
import com.example.heartratioapp.model_classes.Pulse
import java.time.format.DateTimeFormatter

class HistoryAdapter(private val data: ArrayList<HeartRatioEntry>) : RecyclerView.Adapter<HistoryAdapter.EntryHolder>() {
    class EntryHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateView : TextView = view.findViewById(R.id.dateView)
        val bpmView : TextView = view.findViewById(R.id.bpmView)
        val background : ConstraintLayout = view.findViewById(R.id.itemBackground)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_entry_item, parent, false)
        return EntryHolder(view)
    }

    override fun onBindViewHolder(holder: EntryHolder, position: Int) {
        val entry = data[position]

        holder.dateView.text = entry.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        holder.bpmView.text = entry.heartRate.bpm.toString()
        holder.background.setBackgroundColor(entry.heartRate.evaluate().color)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}