package com.example.wearnn.activities

import HealthStatsDisplay
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.wearnn.data.model.HealthStats
import com.example.wearnn.presentation.theme.WearNNTheme
import com.example.wearnn.utils.PermissionUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        PermissionUtils.checkAndRequestPermissions(this)

        // Simulated health statistics


        setContent {
            MyApp()
        }

    }
    // Here you define your screen content Composables
    @Composable
    fun MyApp() {
        WearNNTheme {
            // This is where we set up our navController
            val navController = rememberSwipeDismissableNavController()
            Log.d("WearService", "WearNNTheme:")
            SwipeDismissableNavHost(navController, startDestination = Screen.MainScreen.route) {
                composable(Screen.MainScreen.route) {
                    Log.d("WearService", "MainScreenContents:")
                    MainScreenContent()
                }
                composable(Screen.DetailScreen1.route) {
                    Log.d("WearService", "DetailScreen1Content:")
                    DetailScreen1Content()
                }
                composable(Screen.DetailScreen2.route) {
                    Log.d("WearService", "DetailScreen2Content:")
                    DetailScreen2Content()
                }
                // ... define other screens
            }
        }
    }
    @Composable
    fun MainScreenContent() {
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

            val sampleHealthStats = HealthStats(
                steps = 1200,
                standingMinutes = 90,
                caloriesBurned = 300,
                dailyStepGoal = 10000
            )
            // Main screen content, such as HealthStatsDisplay
            HealthStatsDisplay(sampleHealthStats)
        }
    }

    @Composable
    fun DetailScreen1Content() {
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

            val sampleHealthStats = HealthStats(
                steps = 1200,
                standingMinutes = 90,
                caloriesBurned = 300,
                dailyStepGoal = 10000
            )
            // Main screen content, such as HealthStatsDisplay
            HealthStatsDisplay(sampleHealthStats)
        }
    }

    @Composable
    fun DetailScreen2Content() {
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

            val sampleHealthStats = HealthStats(
                steps = 1200,
                standingMinutes = 90,
                caloriesBurned = 300,
                dailyStepGoal = 10000
            )
            // Main screen content, such as HealthStatsDisplay
            HealthStatsDisplay(sampleHealthStats)
        }
    }
}
