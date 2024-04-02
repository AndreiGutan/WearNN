package com.example.wearnn.presentation

import com.example.wearnn.data.model.HealthStats
import android.os.Bundle
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.view.LayoutInflater
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
import com.example.wearnn.presentation.theme.WearNNTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.wearnn.databinding.FirstscreenBinding
import com.example.wearnn.databinding.ThirdscrenBinding
import com.example.wearnn.presentation.ui.composables.ProgressArc
import com.example.wearnn.utils.PermissionUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.FitnessOptions
import com.google.android.gms.fitness.data.DataType


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        PermissionUtils.checkAndRequestPermissions(this)

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

        PermissionUtils.requestGoogleFitPermissions(this) // Request Google Fit permissions
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Handle permissions results. Consider moving detailed handling to PermissionUtils if it grows complex.
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
                AndroidView(
                    factory = { ctx ->

                        val inflater = LayoutInflater.from(ctx)
                        val binding = ThirdscrenBinding.inflate(inflater)

                        binding.tvTitle.text = "Today's Stats"
                        binding.tvSteps.text = "Steps: ${healthStats.steps}"
                        binding.tvStandingTime.text = "Standing Time: ${healthStats.standingMinutes} minutes"
                        binding.tvCaloriesBurned.text = "Calories Burned: ${healthStats.caloriesBurned}"

                        binding.root

                    })
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

private fun accessGoogleFitData() {
    // Implementation for accessing Google Fit data goes here
}
