package com.example.journeytracker.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.journeytracker.R
import com.example.journeytracker.model.Stop
import java.io.BufferedReader
import java.io.InputStreamReader

class JourneyViewModel(application: Application) : AndroidViewModel(application) {

    private val _stops = MutableLiveData<List<Stop>>()
    val stops: LiveData<List<Stop>> = _stops

    private val _currentStopIndex = MutableLiveData(0)
    val currentStopIndex: LiveData<Int> = _currentStopIndex

    private val _progress = MutableLiveData(0.0f)
    val progress: LiveData<Float> = _progress

    init {
        loadStopsFromFile()
    }

    private fun loadStopsFromFile() {
        val stopList = mutableListOf<Stop>()
        try {
            val inputStream = getApplication<Application>().resources.openRawResource(R.raw.stops)
            val reader = BufferedReader(InputStreamReader(inputStream))

            reader.useLines { lines ->
                lines.forEach { line ->
                    val parts = line.split(",")
                    if (parts.size == 3) {
                        val city = parts[0].trim()
                        val distance = parts[1].trim().toDoubleOrNull() ?: 0.0
                        val visaRequired = parts[2].trim().toBoolean()

                        stopList.add(Stop(city, distance, visaRequired))
                    }
                }
            }
            _stops.value = stopList
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun moveToNextStop() {
        _stops.value?.let {
            if (_currentStopIndex.value!! < it.size - 1) {
                _currentStopIndex.value = _currentStopIndex.value!! + 1
                _progress.value = _currentStopIndex.value!!.toFloat() / (it.size - 1)
            }
        }
    }
}
