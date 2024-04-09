package com.example.wearnn.activities

import HealthStatsDisplay
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import com.example.wearnn.data.model.HealthStats
import com.example.wearnn.presentation.theme.WearNNTheme
import com.example.wearnn.utils.PermissionUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        PermissionUtils.checkAndRequestPermissions(this)

        // Simulated health statistics
        val sampleHealthStats = HealthStats(
            steps = 1200,
            standingMinutes = 90,
            caloriesBurned = 300,
            dailyStepGoal = 10000
        )

        setContent {
            WearNNTheme {
                Scaffold(
                    timeText = {
                        // If you want to display the time
                        Text(
                            text = java.text.SimpleDateFormat(
                                "HH:mm", java.util.Locale.getDefault()
                            ).format(System.currentTimeMillis()),
                            style = MaterialTheme.typography.title3
                        )
                    }
                ) {
                    // Your composable content goes here
                    HealthStatsDisplay(healthStats = sampleHealthStats)
                }
            }
        }
    }
}
