package com.example.wearnn.presentation

import HealthStats
import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.wearnn.R
import com.example.wearnn.presentation.theme.WearNNTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.wearnn.presentation.ui.composables.ProgressArc


class MainActivity : ComponentActivity() {
    private val REQUEST_PERMISSIONS_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        checkAndRequestPermissions()

        // Simulate fetching health statistics
        val sampleHealthStats = HealthStats(
            steps = 1200,
            standingMinutes = 90,
            caloriesBurned = 300,
            dailyStepGoal = 10000 // Example goal
        )

        setContent {
            MainScreen(sampleHealthStats)
        }
    }

    private fun checkAndRequestPermissions() {
        val requiredPermissions = arrayOf(
            Manifest.permission.ACTIVITY_RECOGNITION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BODY_SENSORS
        )

        val allPermissionsGranted = requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, requiredPermissions, REQUEST_PERMISSIONS_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_CODE && grantResults.any { it != PackageManager.PERMISSION_GRANTED }) {
            // Handle case where user does not grant permissions. You might want to show a message or disable features.
        }
    }
}

@Composable
fun MainScreen(healthStats: HealthStats) {
    val progress = healthStats.steps.toFloat() / healthStats.dailyStepGoal.toFloat() // Calculate progress

    WearNNTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                ProgressArc(
                    progress = progress,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Steps: ${healthStats.steps} / ${healthStats.dailyStepGoal}", style = MaterialTheme.typography.body1)
                Text(text = "Standing Time: ${healthStats.standingMinutes} minutes", style = MaterialTheme.typography.body1)
                Text(text = "Calories Burned: ${healthStats.caloriesBurned}", style = MaterialTheme.typography.body1)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, device = WearDevices.SMALL_ROUND)
@Composable
fun DefaultPreview() {
    // Provide sample data for previews
    MainScreen(HealthStats(steps = 1234, standingMinutes = 150, caloriesBurned = 200, dailyStepGoal = 10000))
}
