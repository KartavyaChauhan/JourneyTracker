package com.example.journeytracker.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JourneyViewModel : ViewModel() {
    private val stops = listOf(
        "New York" to 0.0,
        "London" to 5600.0,
        "Dubai" to 3400.0,
        "Singapore" to 7500.0
    )

    private val _currentStopIndex = MutableStateFlow(0)
    val currentStopIndex: StateFlow<Int> = _currentStopIndex

    private val _progress = MutableStateFlow(0.0f)
    val progress: StateFlow<Float> = _progress

    fun getStops(): List<Pair<String, Double>> = stops

    fun moveToNextStop() {
        if (_currentStopIndex.value < stops.size - 1) {
            _currentStopIndex.value++
            _progress.value = (_currentStopIndex.value.toFloat() / (stops.size - 1))
        }
    }
}
