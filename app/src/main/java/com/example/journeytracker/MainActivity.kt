package com.example.journeytracker

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView
        val stopsRecyclerView = findViewById<RecyclerView>(R.id.stopsRecyclerView)
        stopsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Read stops from file
        val stops = readStopsFromFile(this)

        // Set adapter
        val adapter = StopsAdapter(stops)
        stopsRecyclerView.adapter = adapter
    }
}

// Function to Read Stops from stops.txt (Inside res/raw/)
fun readStopsFromFile(context: Context): List<Stop> {
    val stops = mutableListOf<Stop>()
    val inputStream = context.resources.openRawResource(R.raw.stops)
    inputStream.bufferedReader().useLines { lines ->
        lines.forEach { line ->
            val parts = line.split(",")
            if (parts.size == 3) {
                val name = parts[0]
                val distance = parts[1].toDoubleOrNull() ?: 0.0
                val visaRequired = parts[2].toBooleanStrictOrNull() ?: false
                stops.add(Stop(name, distance, visaRequired))
            }
        }
    }
    return stops
}
