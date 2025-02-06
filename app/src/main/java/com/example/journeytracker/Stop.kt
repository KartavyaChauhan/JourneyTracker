package com.example.journeytracker

data class Stop(
    val name: String,          // Name of the stop
    val distance: Double,      // Distance from the previous stop
    val visaRequired: Boolean, // Whether a visa is required
    val isReached: Boolean = false // Whether the stop has been reached
)
