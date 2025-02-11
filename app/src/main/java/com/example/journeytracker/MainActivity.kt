package com.example.journeytracker

import android.content.Context
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: JourneyViewModel by viewModels()
    private lateinit var adapter: StopsAdapter
    private lateinit var stops: List<Stop>
    private lateinit var progressBar: ProgressBar
    private lateinit var distanceCoveredText: TextView

    private val speedMph = 500  // Speed in miles per hour

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val stopsRecyclerView = findViewById<RecyclerView>(R.id.stopsRecyclerView)
        val toggleUnitButton = findViewById<MaterialButton>(R.id.toggleUnitButton)
        val nextStopButton = findViewById<MaterialButton>(R.id.nextStopButton)
        progressBar = findViewById(R.id.progressBar)
        distanceCoveredText = findViewById(R.id.distanceCoveredText)  // Reference TextView

        stopsRecyclerView.layoutManager = LinearLayoutManager(this)
        stops = readStopsFromFile(this)

        adapter = StopsAdapter(stops)
        stopsRecyclerView.adapter = adapter

        // Observe ViewModel changes
        lifecycleScope.launch {
            viewModel.currentStopIndex.collectLatest { index ->
                adapter.updateCurrentStop(index)

                // Calculate progress percentage
                val progress = ((index.toFloat() / (stops.size - 1)) * 100).toInt()
                progressBar.progress = progress

                // Calculate distance covered and time taken
                if (index > 0) {
                    val prevStop = stops[index - 1]
                    val currentStop = stops[index]
                    val distanceCovered = currentStop.distance - prevStop.distance
                    val timeTaken = distanceCovered / speedMph

                    // Update TextView
                    distanceCoveredText.text = "Distance covered: ${String.format("%.1f", distanceCovered)} miles | Time: ${String.format("%.2f", timeTaken)} hrs"
                } else {
                    distanceCoveredText.text = "Starting point reached!"
                }
            }
        }

        lifecycleScope.launch {
            viewModel.isMiles.collectLatest { isMiles ->
                adapter.updateDistanceUnit(isMiles)
                toggleUnitButton.text = if (isMiles) "Switch to KM" else "Switch to Miles"
            }
        }

        // Toggle Distance Unit
        toggleUnitButton.setOnClickListener {
            viewModel.toggleDistanceUnit()
        }

        // Move to Next Stop
        nextStopButton.setOnClickListener {
            viewModel.moveToNextStop(stops.size)
        }
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
