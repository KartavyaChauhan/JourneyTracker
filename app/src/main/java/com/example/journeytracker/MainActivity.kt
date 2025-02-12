package com.example.journeytracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
    val listState = rememberLazyListState()

    // ✅ FIX: Scroll to current stop automatically
    LaunchedEffect(currentStopIndex) {
        listState.animateScrollToItem(currentStopIndex)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF1E88E5), Color(0xFF90CAF9))))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Journey Tracker",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

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
            Text("Time Taken: ${timeTaken} min")
            Spacer(modifier = Modifier.height(16.dp))

            // ✅ FIX: Pass listState to ensure smooth scrolling
            JourneyList(stops, distanceUnit, currentStopIndex, listState)
        }
    }
}

@Composable
fun JourneyList(
    stops: List<Triple<String, Double, String>>,
    distanceUnit: String,
    currentStopIndex: Int,
    listState: LazyListState
) {
    LazyColumn(state = listState) {
        itemsIndexed(stops) { index, stop ->
            val (city, distance, visaRequired) = stop  // ✅ Extract visa requirement
            val isCurrent = index == currentStopIndex
            JourneyItem(city, distance, distanceUnit, visaRequired, isCurrent)
        }
    }
}

@Composable
fun JourneyItem(city: String, distance: Double, distanceUnit: String, visaRequired: String, isCurrent: Boolean) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrent) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = city,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Distance: $distance $distanceUnit",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            // ✅ Add Visa Requirement Display
            Text(
                text = "Visa Required: $visaRequired",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewJourneyTracker() {
    JourneyTrackerApp(viewModel = JourneyViewModel()) // Pass ViewModel manually for preview
}
