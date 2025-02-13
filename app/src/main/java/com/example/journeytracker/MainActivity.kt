package com.example.journeytracker

import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.journeytracker.viewmodel.JourneyViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var stopsRecyclerView: RecyclerView
    private lateinit var toggleUnitButton: Button
    private lateinit var nextStopButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: StopsAdapter
    private var isMiles = false

    private val journeyViewModel: JourneyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        stopsRecyclerView = findViewById(R.id.stopsRecyclerView)
        toggleUnitButton = findViewById(R.id.toggleUnitButton)
        nextStopButton = findViewById(R.id.nextStopButton)
        progressBar = findViewById(R.id.progressBar)

        adapter = StopsAdapter(emptyList()) // Start with empty list
        stopsRecyclerView.layoutManager = LinearLayoutManager(this)
        stopsRecyclerView.adapter = adapter

        journeyViewModel.stops.observe(this, Observer { stops ->
            adapter.updateStops(stops)
        })

        journeyViewModel.currentStopIndex.observe(this, Observer { index ->
            adapter.updateCurrentStop(index)
        })

        journeyViewModel.progress.observe(this, Observer { progress ->
            progressBar.progress = (progress * 100).toInt()
        })

        toggleUnitButton.setOnClickListener {
            isMiles = !isMiles
            adapter.updateDistanceUnit(isMiles)
        }

        nextStopButton.setOnClickListener {
            journeyViewModel.moveToNextStop()
        }
    }
}
