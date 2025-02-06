package com.example.journeytracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StopsAdapter(private val stops: List<Stop>) : RecyclerView.Adapter<StopsAdapter.StopViewHolder>() {

    // ViewHolder for each stop item
    class StopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stopName: TextView = itemView.findViewById(R.id.stopName)
        val stopDistance: TextView = itemView.findViewById(R.id.stopDistance)
        val visaRequirement: TextView = itemView.findViewById(R.id.visaRequirement)
    }

    // Inflate the item layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stop, parent, false)
        return StopViewHolder(view)
    }

    // Bind data to the views
    override fun onBindViewHolder(holder: StopViewHolder, position: Int) {
        val stop = stops[position]
        holder.stopName.text = stop.name
        holder.stopDistance.text = "Distance: ${stop.distance} km"
        holder.visaRequirement.text = "Visa Required: ${if (stop.visaRequired) "Yes" else "No"}"
    }

    // Return the number of stops
    override fun getItemCount(): Int {
        return stops.size
    }
}
