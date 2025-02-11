package com.example.journeytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.journeytracker.viewmodel.JourneyViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: JourneyViewModel by viewModels()
            JourneyTrackerApp(viewModel)
        }
    }
}

@Composable
fun JourneyTrackerApp(viewModel: JourneyViewModel = remember { JourneyViewModel() }) {
    val currentStopIndex by viewModel.currentStopIndex.collectAsState()
    val progress by viewModel.progress.collectAsState()
    val distanceCovered by viewModel.distanceCovered.collectAsState()
    val timeTaken by viewModel.timeTaken.collectAsState()
    val stops = viewModel.getStops()

    var distanceUnit by remember { mutableStateOf("km") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Journey Tracker", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { distanceUnit = if (distanceUnit == "km") "miles" else "km" }) {
            Text("SWITCH KM/MILES")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.moveToNextStop() }) {
            Text("MARK NEXT STOP")
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))

        Text("Distance Covered: ${if (distanceUnit == "km") distanceCovered else distanceCovered * 0.621} $distanceUnit")
        Text("Time Taken: ${timeTaken} min")  // Updated to show time in minutes
        Spacer(modifier = Modifier.height(16.dp))

        JourneyList(stops, distanceUnit, currentStopIndex)
    }
}

@Composable
fun JourneyList(stops: List<Pair<String, Double>>, distanceUnit: String, currentStopIndex: Int) {
    LazyColumn {
        items(stops) { (city, distance) ->
            val formattedDistance = if (distanceUnit == "km") distance else distance * 0.621
            Text("$city\nDistance: $formattedDistance $distanceUnit",
                color = if (stops.indexOf(city to distance) == currentStopIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewJourneyTracker() {
    JourneyTrackerApp(viewModel = JourneyViewModel()) // Pass ViewModel manually for preview
}
