package com.example.heartratioapp.history_activity.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.heartratioapp.R
import java.time.format.DateTimeFormatter

class HistoryAdapter(private val data: Array<HeartRatioEntry>) : RecyclerView.Adapter<HistoryAdapter.EntryHolder>() {
    class EntryHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateView : TextView = view.findViewById(R.id.dateView)
        val bpmView : TextView = view.findViewById(R.id.bpmView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.history_entry_item, parent, false)
        return EntryHolder(view)
    }

    override fun onBindViewHolder(holder: EntryHolder, position: Int) {
        val entry = data[position]

        holder.dateView.text = entry.date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        holder.bpmView.text = entry.bpm.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }
}