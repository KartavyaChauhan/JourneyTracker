package com.example.journeytracker.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JourneyViewModel : ViewModel() {
    private val stops = listOf(
        "London" to 5600.0,
        "Paris" to 6200.0,
        "Rome" to 7000.0,
        "Berlin" to 7500.0,
        "Amsterdam" to 8200.0,
        "Madrid" to 8800.0,
        "Lisbon" to 9400.0,
        "Vienna" to 10000.0,
        "Prague" to 10600.0,
        "Warsaw" to 11200.0,
        "Budapest" to 11800.0,
        "Athens" to 12400.0,
        "Istanbul" to 13000.0
    )

    private val visaRequirements = mapOf(
        "London" to "Visa Required",
        "Paris" to "Schengen Visa Required",
        "Rome" to "Schengen Visa Required",
        "Berlin" to "Schengen Visa Required",
        "Amsterdam" to "Schengen Visa Required",
        "Madrid" to "Schengen Visa Required",
        "Lisbon" to "Schengen Visa Required",
        "Vienna" to "Schengen Visa Required",
        "Prague" to "Schengen Visa Required",
        "Warsaw" to "Schengen Visa Required",
        "Budapest" to "Schengen Visa Required",
        "Athens" to "Schengen Visa Required",
        "Istanbul" to "E-Visa Required"
    )

    private val _currentStopIndex = MutableStateFlow(0)
    val currentStopIndex: StateFlow<Int> = _currentStopIndex

    private val _progress = MutableStateFlow(0.0f)
    val progress: StateFlow<Float> = _progress

    private val _distanceCovered = MutableStateFlow(0.0)
    val distanceCovered: StateFlow<Double> = _distanceCovered

    private val _timeTaken = MutableStateFlow(0)
    val timeTaken: StateFlow<Int> = _timeTaken

    private val _currentVisaRequirement = MutableStateFlow(visaRequirements[stops[0].first] ?: "Unknown")
    val currentVisaRequirement: StateFlow<String> = _currentVisaRequirement

    private val speed = 900 // Speed in km/h
    private val totalDistance = stops.last().second

    fun getStops(): List<Triple<String, Double, String>> =
        stops.map { Triple(it.first, it.second, visaRequirements[it.first] ?: "Unknown") }
    fun resetJourney() {
        _currentStopIndex.value = 0
        _progress.value = 0f
        _distanceCovered.value = 0.0
        _timeTaken.value = 0
    }

    fun moveToNextStop() {
        if (_currentStopIndex.value < stops.size - 1) {
            val previousStopDistance = stops[_currentStopIndex.value].second
            _currentStopIndex.value++
            val newStopDistance = stops[_currentStopIndex.value].second

            _distanceCovered.value += newStopDistance - previousStopDistance
            _timeTaken.value = ((_distanceCovered.value) / speed * 60).toInt()

            // Ensure progress reaches 1.0 at the last stop
            _progress.value = if (_currentStopIndex.value == stops.size - 1) {
                1.0f
            } else {
                (_distanceCovered.value / totalDistance).toFloat()
            }

            _currentVisaRequirement.value = visaRequirements[stops[_currentStopIndex.value].first] ?: "Unknown"
        }
    }
}
