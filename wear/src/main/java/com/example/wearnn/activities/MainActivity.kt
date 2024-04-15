package com.example.wearnn.activities


import HealthViewModelPreferences
import Screen1Week
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import com.example.wearnn.presentation.theme.WearNNTheme
import com.example.wearnn.presentation.ui.composables.statsPerDay.Screen1Day
import com.example.wearnn.presentation.ui.composables.statsPerDay.Screen2Day
import com.example.wearnn.presentation.ui.composables.statsPerDay.Screen2Week
import com.example.wearnn.presentation.ui.composables.statsPerDay.Screen3Day
import com.example.wearnn.viewModel.HealthViewModel

import java.time.LocalDate


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        //val database = AppDatabase.getDatabase(this)
        //val healthDataDao = database.healthDataDao()
        //val viewModelFactory = HealthViewModelFactory(healthDataDao)


// Retrieve default values from preferences in MainActivity
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val defaultProgress = sharedPreferences.getInt("default_progress", 0)
        val defaultGoal = sharedPreferences.getInt("default_goal", 0)
        val defaultDate = sharedPreferences.getString("default_date", LocalDate.now().toString()) ?: LocalDate.now().toString()
        val defaultType = sharedPreferences.getString("default_type", "default") ?: "default"

        setContent {
            WearNNTheme {
                val healthViewModel: HealthViewModel = ViewModelProvider(this)[HealthViewModel::class.java]
                PagerContent(healthViewModel)
            }
        }
    }

    @Composable
    fun PagerContent(
        healthViewModel: HealthViewModel
    ) {
        val pagerState = rememberPagerState(pageCount = { 2 })
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            when (page) {
                0 -> DailyContent(healthViewModel)
                1 -> WeeklyContent(healthViewModel)
            }
        }
    }

    @Composable
    fun DailyContent(healthViewModel: HealthViewModel) {
        val verticalPagerState = rememberPagerState(pageCount = { 3 })
        VerticalPager(state = verticalPagerState, modifier = Modifier.fillMaxSize()) { page ->
            when (page) {
                0 -> Screen1Day(healthViewModel)
                1 -> Screen2Day(healthViewModel)
                2 -> Screen3Day(healthViewModel)
            }
        }
    }

    @Composable
    fun WeeklyContent(healthViewModel: HealthViewModel) {
        val verticalPagerState = rememberPagerState(pageCount = { 2 })
        VerticalPager(state = verticalPagerState, modifier = Modifier.fillMaxSize()) { page ->
            when (page) {
                0 -> Screen1Week(healthViewModel)
                1 -> Screen2Week(healthViewModel)
            }
        }
    }


    // Function to set default values in preferences
    fun setDefaultValues(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()

        // Set default values if they don't exist
        if (!sharedPreferences.contains("default_progress")) {
            editor.putInt("default_progress", 0)
        }
        if (!sharedPreferences.contains("default_goal")) {
            editor.putInt("default_goal", 0)
        }
        if (!sharedPreferences.contains("default_date")) {
            editor.putString("default_date", LocalDate.now().toString()) // Save current date as default
        }
        if (!sharedPreferences.contains("default_type")) {
            editor.putString("default_type", "default") // Default type
        }

        editor.apply()
    }
}
