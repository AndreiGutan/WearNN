package com.example.wearnn.activities

import HealthViewModelFactory
import Screen1Week
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import com.example.wearnn.presentation.ui.composables.statsPerWeek.Screen2Week
import com.example.wearnn.presentation.ui.composables.statsPerDay.Screen3Day
import com.example.wearnn.viewModel.HealthViewModel
import com.example.wearnn.data.database.AppDatabase
import com.example.wearnn.utils.PermissionUtils


class MainActivity : ComponentActivity() {
    private lateinit var healthViewModel: HealthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setupViewModel()
        setupContent()
        PermissionUtils.checkAndRequestPermissions(this)
    }

    private fun setupViewModel() {
        val database = AppDatabase.getDatabase(this)
        val healthDataDao = database.healthDataDao()
        val viewModelFactory = HealthViewModelFactory(healthDataDao)
        healthViewModel = ViewModelProvider(this, viewModelFactory)[HealthViewModel::class.java]
    }

    private fun setupContent() {
        setContent {
            WearNNTheme {
                PagerContent(healthViewModel)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionUtils.REQUEST_PERMISSIONS_CODE) {
            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (!allGranted) {
                // Implement your logic for a fallback or a user dialog explaining why you need the permissions
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

