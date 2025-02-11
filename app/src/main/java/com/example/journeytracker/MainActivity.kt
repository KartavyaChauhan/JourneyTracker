package com.example.journeytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun JourneyTrackerApp(viewModel: JourneyViewModel) {
    val stops = viewModel.getStops()
    val currentStopIndex by viewModel.currentStopIndex.collectAsState()
    val progress by viewModel.progress.collectAsState()
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

        JourneyList(stops, currentStopIndex, distanceUnit)
    }
}

@Composable
fun JourneyList(stops: List<Pair<String, Double>>, currentStopIndex: Int, distanceUnit: String) {
    LazyColumn {
        itemsIndexed(stops) { index, (city, distance) ->
            val isCurrent = index == currentStopIndex
            Text(
                text = "$city\nDistance: ${if (distanceUnit == "km") distance else distance * 0.621} $distanceUnit",
                color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
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
