package com.example.journeytracker

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class JourneyViewModel : ViewModel() {

    private val _currentStopIndex = MutableStateFlow(0)
    val currentStopIndex: StateFlow<Int> = _currentStopIndex

    private val _isMiles = MutableStateFlow(false)
    val isMiles: StateFlow<Boolean> = _isMiles

    fun moveToNextStop(totalStops: Int) {
        if (_currentStopIndex.value < totalStops - 1) {
            _currentStopIndex.value++
        }
    }

    fun toggleDistanceUnit() {
        _isMiles.value = !_isMiles.value
    }
}
