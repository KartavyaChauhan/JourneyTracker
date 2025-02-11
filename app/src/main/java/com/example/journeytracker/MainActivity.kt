package com.example.journeytracker
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JourneyTrackerApp()
        }
    }
}

@Composable
fun JourneyTrackerApp() {
    var distanceUnit by remember { mutableStateOf("km") }
    var progress by remember { mutableStateOf(0.2f) }

    Column(modifier = Modifier.padding(16.dp)) {
        // ✅ Fixed: Use headlineSmall for Material 3, h5 for Material 2
        Text("JourneyTracker", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            distanceUnit = if (distanceUnit == "km") "miles" else "km"
        }) {
            Text("SWITCH KM/MILES")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (progress < 1.0f) progress += 0.2f
        }) {
            Text("MARK NEXT STOP")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // ✅ Fixed: Ensure correct Jetpack Compose import
        LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(16.dp))

        JourneyList(distanceUnit)
    }
}

@Composable
fun JourneyList(distanceUnit: String) {
    val stops = listOf(
        "New York" to 0.0,
        "London" to 5600.0,
        "Dubai" to 3400.0,
        "Singapore" to 7500.0
    )

    LazyColumn {
        items(stops) { (city, distance) ->
            Text("$city\nDistance: ${if (distanceUnit == "km") distance else distance * 0.621} $distanceUnit")
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewJourneyTracker() {
    JourneyTrackerApp()
}
