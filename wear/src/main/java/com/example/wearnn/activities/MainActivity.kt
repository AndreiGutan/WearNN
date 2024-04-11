package com.example.wearnn.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.wearnn.data.model.HealthStats
import com.example.wearnn.presentation.theme.WearNNTheme
import com.example.wearnn.presentation.ui.composables.StatsPerDay.HealthData
import com.example.wearnn.presentation.ui.composables.StatsPerDay.Screen1Day
import com.example.wearnn.presentation.ui.composables.StatsPerDay.Screen2Day
import com.example.wearnn.presentation.ui.composables.StatsPerDay.Screen3Day
import com.example.wearnn.presentation.ui.composables.StatsPerWeek.Screen1Week
import com.example.wearnn.presentation.ui.composables.StatsPerWeek.Screen2Week
import com.example.wearnn.presentation.ui.composables.StatsPerWeek.Screen3Week
import com.example.wearnn.utils.PermissionUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        PermissionUtils.checkAndRequestPermissions(this)

        setContent {
            WearNNTheme {
                // Replace with your function that manages swipeable screens
                MainContent()
            }
        }
    }
    @Composable
    fun MainContent() {
        val horizontalPagerState = rememberPagerState(pageCount = { 2 })

        HorizontalPager(state = horizontalPagerState) { page ->
            when (page) {
                0 -> FirstColumnScreens(modifier = Modifier.fillMaxSize())
                1 -> SecondColumnScreens(modifier = Modifier.fillMaxSize())
            }
        }
    }
    @Composable
    fun FirstColumnScreens(modifier: Modifier) {
        // This is for the first column
        val verticalPagerState = rememberPagerState(pageCount = { 3 })
        VerticalPager(state = verticalPagerState, modifier = modifier) { page ->
            val sampleHealthStats = HealthStats(
                steps = 1200,
                standingMinutes = 90,
                caloriesBurned = 300,
                dailyStepGoal = 10000
            )
            val customRed = Color(0xFFf82024) // Replace with your custom color value
            val customYellow = Color(0xFFf7d01b) // Replace with your custom color value
            val customBlue = Color(0xFF007af6) // Replace with your custom color value

            when (page) {

                0 -> Screen1Day(
                    healthData = listOf(
                        HealthData(progress = 1700, goal = 2000, color = customRed),
                        HealthData(progress = 300, goal = 1000, color = customYellow),
                        HealthData(progress = 7, goal = 100, color = customBlue)
                    )
                )

                1 -> Screen2Day(sampleHealthStats)
                2 -> Screen3Day(sampleHealthStats)
            }
        }
    }

    @Composable
    fun SecondColumnScreens(modifier: Modifier) {
        // This is for the second column
        val verticalPagerState = rememberPagerState(pageCount = { 3 })
        VerticalPager(state = verticalPagerState, modifier = modifier) { page ->
            val sampleHealthStats = HealthStats(
                steps = 1200,
                standingMinutes = 90,
                caloriesBurned = 300,
                dailyStepGoal = 10000
            )
            when (page) {
                0 -> Screen1Week(sampleHealthStats)
                1 -> Screen2Week(sampleHealthStats)
                2 -> Screen3Week(sampleHealthStats)
            }
        }
    }

}
