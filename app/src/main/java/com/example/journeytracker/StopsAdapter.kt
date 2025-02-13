package com.example.journeytracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.journeytracker.model.Stop

class StopsAdapter(private var stops: List<Stop>) : RecyclerView.Adapter<StopsAdapter.StopViewHolder>() {

    private var currentStopIndex = 0
    private var isMiles = false

    class StopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stopName: TextView = itemView.findViewById(R.id.stopName)
        val stopDistance: TextView = itemView.findViewById(R.id.stopDistance)
        val visaRequirement: TextView = itemView.findViewById(R.id.visaRequired)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stop, parent, false)
        return StopViewHolder(view)
    }

    override fun onBindViewHolder(holder: StopViewHolder, position: Int) {
        val stop = stops[position]
        val distance = if (isMiles) stop.distance * 0.621371 else stop.distance
        val distanceUnit = if (isMiles) "miles" else "km"

        holder.stopName.text = stop.name
        holder.stopDistance.text = "Distance: %.1f $distanceUnit".format(distance)
        holder.visaRequirement.text = "Visa Required: ${if (stop.visaRequired) "Yes" else "No"}"

        holder.itemView.alpha = if (position == currentStopIndex) 1.0f else 0.5f
    }

    override fun getItemCount(): Int = stops.size

    fun updateStops(newStops: List<Stop>) {
        stops = newStops
        notifyDataSetChanged()
    }

    fun updateCurrentStop(index: Int) {
        currentStopIndex = index
        notifyDataSetChanged()
    }

    fun updateDistanceUnit(miles: Boolean) {
        isMiles = miles
        notifyDataSetChanged()
    }
}
