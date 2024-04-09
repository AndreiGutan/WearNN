package com.example.wearnn.activities

import com.example.wearnn.data.model.HealthStats
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.wearnn.presentation.theme.WearNNTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.wearable.Wearable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.tooling.preview.devices.WearDevices
import com.example.wearnn.databinding.ThirdscrenBinding
import com.example.wearnn.utils.PermissionUtils
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.PutDataMapRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        PermissionUtils.checkAndRequestPermissions(this)

        // Simulated health statistics
        val sampleHealthStats = HealthStats(steps = 1200, standingMinutes = 90, caloriesBurned = 300, dailyStepGoal = 10000)

        setContent {
            MainScreen(sampleHealthStats) {
                sendSyncRequest()
            }
        }
    }

    private fun sendSyncRequest() {
        // Move the execution of the network call to a background thread using coroutines
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dataClient = Wearable.getDataClient(applicationContext)
                val putDataReq = PutDataMapRequest.create("/sync_request").run {
                    dataMap.putString("request", "sync_account")
                    dataMap.putLong("timestamp", System.currentTimeMillis())
                    asPutDataRequest().setUrgent()
                }
                val task = dataClient.putDataItem(putDataReq)
                Tasks.await(task)
                // If needed, switch back to the main thread to update UI or something else
                launch(Dispatchers.Main) {
                    // Update UI or something else on the main thread
                }
            } catch (e: Exception) {
                // Handle the exception, e.g., network error or interruption
                Log.e("sendSyncRequest", "Error sending sync request", e)
            }
        }
    }
}

@Composable
fun MainScreen(healthStats: HealthStats, onSyncRequest: () -> Unit) {
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
                // Traditional View system is used here for displaying health stats.
                AndroidView(
                    factory = { ctx ->
                        val inflater = LayoutInflater.from(ctx)
                        val binding = ThirdscrenBinding.inflate(inflater)
                        binding.tvTitle.text = "Today's Stats"
                        binding.tvSteps.text = "Steps: ${healthStats.steps}"
                        binding.tvStandingTime.text = "Standing Time: ${healthStats.standingMinutes} minutes"
                        binding.tvCaloriesBurned.text = "Calories Burned: ${healthStats.caloriesBurned}"
                        binding.root
                    },
                    modifier = Modifier.weight(1f) // Use weight to fill the space above the button
                )
                Spacer(modifier = Modifier.height(16.dp)) // Now correctly placed in Composable context
                Button(
                    onClick = onSyncRequest,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Sync Account")
                }
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF000000, device = WearDevices.SMALL_ROUND)
@Composable
fun DefaultPreview() {
    // Provide sample data for previews
    MainScreen(HealthStats(steps = 1234, standingMinutes = 150, caloriesBurned = 200, dailyStepGoal = 10000)) {
        // This is a dummy implementation for preview purposes
        // In real usage, this lambda would trigger the account sync process
        Log.d("Preview", "Sync request triggered in preview")
    }
}


