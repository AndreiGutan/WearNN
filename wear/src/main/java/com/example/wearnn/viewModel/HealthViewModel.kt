package com.example.wearnn.viewModel

import ActivityStat
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wearnn.data.dao.HealthDataDao
import com.example.wearnn.data.model.HealthData
import com.example.wearnn.utils.AppColors
import com.example.wearnn.utils.Goals
import com.example.wearnn.utils.StatsNames
import com.example.wearnn.utils.Units
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate

class HealthViewModel(
    private val healthDataDao: HealthDataDao
) : ViewModel() {
    private val _dailyData = MutableStateFlow<List<HealthData>>(emptyList())
    val dailyData: StateFlow<List<HealthData>> = _dailyData
    private val _weeklyAverageStats = MutableStateFlow<List<ActivityStat>>(emptyList())
    val weeklyAverageStats: StateFlow<List<ActivityStat>> = _weeklyAverageStats
    private val _configuredDailyData = MutableStateFlow<List<ActivityStat>>(emptyList())
    val configuredDailyData: StateFlow<List<ActivityStat>> = _configuredDailyData
    private val _weeklyData = MutableStateFlow<List<List<HealthData>>>(emptyList())
    val weeklyData: StateFlow<List<List<HealthData>>> = _weeklyData

    init {
        loadTodayData()
        loadWeeklyData()
        calculateWeeklyAverages()
        convertHealthDataToActivityStats()
    }
    private fun calculateWeeklyAverages() {
        viewModelScope.launch {
            val weekData = healthDataDao.loadHealthDataForWeek(LocalDate.now().minusDays(6), LocalDate.now()).collect { data ->
                val stepsAverage = data.filter { it.type == StatsNames.steps }.map { it.progress }.average()
                val distanceAverage = data.filter { it.type == StatsNames.distance }.map { it.progress }.average()
                val climbedAverage = data.filter { it.type == StatsNames.climbed }.map { it.progress }.average()

                _weeklyAverageStats.value = listOf(
                    ActivityStat(title = "Avg Steps", unit = "steps", progress = stepsAverage.toInt()),
                    ActivityStat(title = "Avg Distance", unit = "km", progress = distanceAverage.toInt()),
                    ActivityStat(title = "Avg Climbed", unit = "m", progress = climbedAverage.toInt())
                )
            }
        }
    }
    private fun loadTodayData() {
        viewModelScope.launch {
            healthDataDao.loadHealthDataForDay(LocalDate.now()).collect {
                _dailyData.value = it
                convertHealthDataToActivityStats()
            }
        }
    }
    private fun loadWeeklyData() {
        viewModelScope.launch {
            val startDate = LocalDate.now().minusDays(6)
            val endDate = LocalDate.now()
            healthDataDao.loadHealthDataForWeek(startDate, endDate).collect { weeklyDataList ->
                val groupedData = weeklyDataList.groupBy { it.date }.values.map { it.toList() }
                _weeklyData.value = groupedData
            }
        }
    }
    private fun convertHealthDataToActivityStats() {
        viewModelScope.launch {
            val stats = _dailyData.value.map { healthData ->
                mapToActivityStat(healthData)
            }
            _configuredDailyData.value = stats
        }
    }

    // Example function to calculate average progress of a particular statistic
    fun calculateAverageProgress() {
        viewModelScope.launch {
            dailyData.collect { dataList ->
                val averageProgress = dataList.map { it.progress }.average()
                // Do something with averageProgress, like storing it in a StateFlow or processing further
            }
        }
    }
    private fun mapToActivityStat(healthData: HealthData): ActivityStat {
        return when (healthData.type) {
            StatsNames.move -> ActivityStat(
                title = StatsNames.move,
                unit = Units.calories,
                progress = healthData.progress,
                goal = Goals.caloriesGoal,
                angle = calculateAngle(healthData.progress, Goals.caloriesGoal),
                color = AppColors.caloriesRed
            )
            StatsNames.exercise -> ActivityStat(
                title = StatsNames.exercise,
                unit = Units.minutes,
                progress = healthData.progress,
                goal = Goals.exerciseGoal,
                angle = calculateAngle(healthData.progress, Goals.exerciseGoal),
                color = AppColors.activityYellow
            )
            StatsNames.stand -> ActivityStat(
                title = StatsNames.stand,
                unit = Units.hours,
                progress = healthData.progress,
                goal = Goals.standGoal,
                angle = calculateAngle(healthData.progress, Goals.standGoal),
                color = AppColors.standBlue
            )
            StatsNames.steps -> ActivityStat(
                title = StatsNames.steps,
                unit = Units.steps,
                progress = healthData.progress
            )
            StatsNames.distance -> ActivityStat(
                title = StatsNames.distance,
                unit = Units.kilometers,
                progress = healthData.progress
            )
            StatsNames.climbed -> ActivityStat(
                title = StatsNames.climbed,
                unit = Units.meters,
                progress = healthData.progress
            )
            else -> throw IllegalArgumentException("Unknown health data type")
        }
    }

    private fun calculateAngle(progress: Int, goal: Int): Float {
        return (270f * progress / goal).coerceIn(0f, 270f)
    }


    fun isDayCompleted(healthDataList: List<HealthData>): Boolean {
        if (healthDataList.isEmpty()) return false
        val individualCompletion = healthDataList.all { it.progress.toDouble() / it.goal >= 0.8 }
        val averageCompletion = healthDataList.sumOf { it.progress.toDouble() / it.goal } / healthDataList.size > 0.9
        return individualCompletion && averageCompletion
    }
}
