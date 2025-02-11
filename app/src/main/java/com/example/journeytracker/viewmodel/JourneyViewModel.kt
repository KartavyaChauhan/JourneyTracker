package com.example.journeytracker.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JourneyViewModel : ViewModel() {
    private val stops = listOf(
        "London" to 5600.0,
        "Paris" to 6200.0,
        "Rome" to 7000.0,
        "Berlin" to 7500.0,  // added European country
        "Amsterdam" to 8200.0 // added European country
    )

    private val _currentStopIndex = MutableStateFlow(0)
    val currentStopIndex: StateFlow<Int> = _currentStopIndex

    private val _progress = MutableStateFlow(0.0f)
    val progress: StateFlow<Float> = _progress

    private val _distanceCovered = MutableStateFlow(0.0)
    val distanceCovered: StateFlow<Double> = _distanceCovered

    private val _timeTaken = MutableStateFlow(0)
    val timeTaken: StateFlow<Int> = _timeTaken

    private val speed = 900 // Speed in km/h

    fun getStops(): List<Pair<String, Double>> = stops

    fun moveToNextStop() {
        if (_currentStopIndex.value < stops.size - 1) {
            val previousStopDistance = stops[_currentStopIndex.value].second
            _currentStopIndex.value++
            val newStopDistance = stops[_currentStopIndex.value].second

            _distanceCovered.value = newStopDistance - previousStopDistance
            _timeTaken.value = ((newStopDistance - previousStopDistance) / speed * 60).toInt() // Time in minutes

            _progress.value = (_currentStopIndex.value.toFloat() / (stops.size - 1))
        }
    }
}
