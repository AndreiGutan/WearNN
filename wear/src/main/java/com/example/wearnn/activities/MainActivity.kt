package com.example.wearnn.activities

import HealthViewModelFactory
import Screen1Week
import android.os.Bundle
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
import com.example.wearnn.data.AppDatabase
import com.example.wearnn.presentation.theme.WearNNTheme
import com.example.wearnn.presentation.ui.composables.statsPerDay.Screen1Day
import com.example.wearnn.presentation.ui.composables.statsPerDay.Screen2Day
import com.example.wearnn.presentation.ui.composables.statsPerDay.Screen2Week
import com.example.wearnn.presentation.ui.composables.statsPerDay.Screen3Day
import com.example.wearnn.utils.PermissionUtils
import com.example.wearnn.viewModel.HealthViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        val database = AppDatabase.getDatabase(this)
        val healthDataDao = database.healthDataDao()
        val viewModelFactory = HealthViewModelFactory(healthDataDao)

        setContent {
            WearNNTheme {
                val healthViewModel: HealthViewModel = ViewModelProvider(this, viewModelFactory)[HealthViewModel::class.java]
                PagerContent(healthViewModel)
            }
        }
    }

    @Composable
    fun PagerContent(healthViewModel: HealthViewModel) {
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
}
