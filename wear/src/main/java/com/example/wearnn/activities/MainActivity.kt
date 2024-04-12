package com.example.wearnn.activities

import ActivityStat
import HealthData
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
import com.example.wearnn.data.model.HealthStats
import com.example.wearnn.presentation.theme.WearNNTheme
import com.example.wearnn.presentation.ui.composables.StatsPerDay.Screen1Day
import com.example.wearnn.presentation.ui.composables.StatsPerDay.Screen2Day
import com.example.wearnn.presentation.ui.composables.StatsPerDay.Screen2Week
import com.example.wearnn.presentation.ui.composables.StatsPerDay.Screen3Day
import com.example.wearnn.presentation.ui.composables.StatsPerWeek.Screen3Week
import com.example.wearnn.utils.AppColors
import com.example.wearnn.utils.Goals
import com.example.wearnn.utils.PermissionUtils
import com.example.wearnn.utils.StatsNames
import com.example.wearnn.utils.Units

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

                0 -> Screen1Day(
                    healthData = listOf(
                        HealthData(
                            progress = 1700,
                            Goals.caloriesGoal,
                            color = AppColors.caloriesRed
                        ),
                        HealthData(
                            progress = 300,
                            Goals.moveGoal,
                            color = AppColors.activityYellow
                        ),
                        HealthData(progress = 7, Goals.standGoal, color = AppColors.standBlue)
                    )
                )

                1 -> Screen2Day(
                    activities = listOf(
                        ActivityStat(
                            StatsNames.exercise,
                            Units.calories,
                            progress = 1700,
                            Goals.caloriesGoal,
                            color = AppColors.caloriesRed
                        ),
                        ActivityStat(
                            StatsNames.move,
                            Units.minutes,
                            progress = 300,
                            Goals.moveGoal,
                            color = AppColors.activityYellow
                        ),
                        ActivityStat(
                            StatsNames.stand,
                            Units.hours,
                            progress = 7,
                            Goals.standGoal,
                            color = AppColors.standBlue
                        )
                    )
                )

                2 -> Screen3Day(
                    activities = listOf(
                        ActivityStat(
                            StatsNames.steps,
                            Units.steps,
                            progress = 1700,
                            Goals.caloriesGoal,
                            color = AppColors.caloriesRed
                        ),
                        ActivityStat(
                            StatsNames.distance,
                            Units.kilometers,
                            progress = 300,
                            Goals.moveGoal,
                            color = AppColors.activityYellow
                        ),
                        ActivityStat(
                            StatsNames.climbed,
                            Units.meters,
                            progress = 7,
                            Goals.standGoal,
                            color = AppColors.standBlue
                        )
                    )
                )
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

            val weeklyData = List(7) { day ->
                listOf(
                    HealthData(progress = day * 100 + 100, goal = 1000, color = AppColors.caloriesRed),
                    HealthData(progress = day * 50 + 50, goal = 500, color = AppColors.activityYellow),
                    HealthData(progress = day * 10 + 10, goal = 200, color = AppColors.standBlue)
                )
            }
            when (page) {
                0 -> Screen1Week(weeklyData)
                1 -> Screen2Week(
                    activities = listOf(
                        ActivityStat(
                            StatsNames.avgsteps,
                            Units.steps,
                            progress = 1700,
                            Goals.caloriesGoal,
                            color = AppColors.caloriesRed
                        ),
                        ActivityStat(
                            StatsNames.avgdistance,
                            Units.kilometers,
                            progress = 300,
                            Goals.moveGoal,
                            color = AppColors.activityYellow
                        ),
                        ActivityStat(
                            StatsNames.avgclimbed,
                            Units.meters,
                            progress = 7,
                            Goals.standGoal,
                            color = AppColors.standBlue
                        )
                    )
                )
                2 -> Screen3Week(sampleHealthStats)
            }
        }
    }

}
