package com.example.wearnn.activities

import HealthStatsDisplay
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.example.wearnn.data.model.HealthStats
import com.example.wearnn.presentation.theme.WearNNTheme
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
            when (page) {

                0 -> Screen1Day(0.7f)
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
