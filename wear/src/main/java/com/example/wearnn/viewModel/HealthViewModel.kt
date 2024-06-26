package com.example.wearnn.viewModel

import ActivityStat
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wearnn.data.dao.HealthDataDao
import com.example.wearnn.data.model.HealthData
import com.example.wearnn.utils.AppColors
import com.example.wearnn.utils.StatsNames
import com.example.wearnn.utils.Units
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HealthViewModel(private val healthDataDao: HealthDataDao) : ViewModel() {

    private val _dailyData = MutableStateFlow<List<HealthData>>(emptyList())
    val dailyData: StateFlow<List<HealthData>> = _dailyData
    private val _weeklyAverageStats = MutableStateFlow<List<ActivityStat>>(emptyList())
    val weeklyAverageStats: StateFlow<List<ActivityStat>> = _weeklyAverageStats
    private val _configuredDailyData = MutableStateFlow<List<ActivityStat>>(emptyList())
    val configuredDailyData: StateFlow<List<ActivityStat>> = _configuredDailyData
    private val _weeklyData = MutableStateFlow<List<List<HealthData>>>(emptyList())
    val weeklyData: StateFlow<List<List<HealthData>>> = _weeklyData

    init {
        Log.d("HealthViewModel", "Initialization")
        loadTodayData()
        loadWeeklyData()
        calculateWeeklyAverages()
        convertHealthDataToActivityStats()
    }

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private fun loadTodayData() {
        viewModelScope.launch {
            try {
                val today = LocalDate.now()
                healthDataDao.loadHealthDataForDay(today).collect { data ->
                    _dailyData.value = data.ifEmpty { initializeEmptyDataForDay(today) }
                    convertHealthDataToActivityStats()
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                Log.e("HealthViewModel", "Failed to load today's data: ${e.message}")
                _isLoading.value = false
            }
        }
    }

    private fun initializeEmptyDataForDay(date: LocalDate): List<HealthData> {
        val types = listOf(
            StatsNames.move,
            StatsNames.stand,
            StatsNames.exercise,
            StatsNames.steps,
            StatsNames.distance,
            StatsNames.climbed
        )
        return types.map { type ->
            HealthData(
                date = date,
                type = type,
                goal = when (type) {
                    StatsNames.move -> 500
                    StatsNames.stand -> 12
                    StatsNames.exercise -> 30
                    StatsNames.steps -> 5000
                    StatsNames.distance -> 10000
                    StatsNames.climbed -> 10
                    else -> 0
                },
                progress = 0
            )
        }
    }

    private fun calculateWeeklyAverages() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val startDate = today.minusDays(6)
            healthDataDao.loadHealthDataForWeek(startDate, today).collect { data ->
                val stepsAverage = data.filter { it.type == StatsNames.steps }.map { it.progress }.average()
                val distanceAverage = data.filter { it.type == StatsNames.distance }.map { it.progress }.average()
                val climbedAverage = data.filter { it.type == StatsNames.climbed }.map { it.progress }.average()
                val moveAverage = data.filter { it.type == StatsNames.move }.map { it.progress }.average()
                val standAverage = data.filter { it.type == StatsNames.stand }.map { it.progress }.average()
                val exerciseAverage = data.filter { it.type == StatsNames.exercise }.map { it.progress }.average()

                _weeklyAverageStats.value = listOf(
                    ActivityStat(title = StatsNames.avgsteps, unit = "steps", progress = stepsAverage.toInt()),
                    ActivityStat(title = StatsNames.avgdistance, unit = "km", progress = distanceAverage.toInt()),
                    ActivityStat(title = StatsNames.avgclimbed, unit = "m", progress = climbedAverage.toInt()),
                    ActivityStat(title = StatsNames.avgmove, unit = "cal", progress = moveAverage.toInt()),
                    ActivityStat(title = StatsNames.avgstand, unit = "hrs", progress = standAverage.toInt()),
                    ActivityStat(title = StatsNames.avgexercise, unit = "min", progress = exerciseAverage.toInt())
                )
            }
        }
    }

    private fun loadWeeklyData() {
        viewModelScope.launch {
            val today = LocalDate.now()
            val startDate = today.minusDays(6)
            healthDataDao.loadHealthDataForWeek(startDate, today).collect { weeklyDataList ->
                val groupedData = (0..6).map { daysAgo ->
                    val date = today.minusDays(daysAgo.toLong())
                    weeklyDataList.filter { it.date == date }.ifEmpty { initializeEmptyDataForDay(date) }
                }
                _weeklyData.value = groupedData
            }
        }
    }

    private fun convertHealthDataToActivityStats() {
        viewModelScope.launch {
            try {
                val validStats = _dailyData.value.filter { data ->
                    data.type in listOf(
                        StatsNames.move,
                        StatsNames.exercise,
                        StatsNames.stand,
                        StatsNames.steps,
                        StatsNames.distance,
                        StatsNames.climbed,
                        StatsNames.heartRate
                    )
                }
                val stats = validStats.map { healthData ->
                    mapToActivityStat(healthData)
                }
                _configuredDailyData.value = stats
            } catch (e: Exception) {
                Log.e("HealthViewModel", "Error converting health data: ${e.message}")
            }
        }
    }

    fun isDayCompleted(healthDataList: List<HealthData>): Boolean {
        if (healthDataList.isEmpty()) return false
        val individualCompletion = healthDataList.all { it.progress.toDouble() / it.goal >= 0.7 }
        val averageCompletion = healthDataList.sumOf { it.progress.toDouble() / it.goal } / healthDataList.size > 0.8
        return individualCompletion && averageCompletion
    }

    private fun mapToActivityStat(healthData: HealthData): ActivityStat {
        return ActivityStat(
            title = healthData.type,
            unit = when (healthData.type) {
                StatsNames.exercise -> Units.minutes
                StatsNames.move -> Units.calories
                StatsNames.steps -> Units.steps
                StatsNames.distance -> Units.kilometers
                StatsNames.climbed -> Units.meters
                StatsNames.stand -> Units.hours
                StatsNames.heartRate -> Units.bpm
                else -> Units.minutes
            },
            progress = healthData.progress,
            goal = healthData.goal,
            angle = calculateAngle(healthData.progress, healthData.goal),
            color = when (healthData.type) {
                StatsNames.move -> AppColors.caloriesRed
                StatsNames.exercise -> AppColors.activityYellow
                StatsNames.stand -> AppColors.standBlue
                StatsNames.heartRate -> AppColors.caloriesRed
                else -> Color.Gray
            }
        )
    }

    private fun calculateAngle(progress: Int, goal: Int): Float {
        return (270f * progress / goal).coerceIn(0f, 270f)
    }

    fun updateSteps(steps: Int) {
        viewModelScope.launch {
            val today = LocalDate.now()
            val type = StatsNames.steps
            val healthData = healthDataDao.getHealthDataByDateAndType(today, type) ?: HealthData(
                date = today,
                type = type,
                goal = 5000,
                progress = 0
            )
            healthData.progress = steps
            healthDataDao.insertOrUpdate(healthData)
            loadTodayData()
        }
    }

    fun addCalories(calories: Int) {
        viewModelScope.launch {
            val today = LocalDate.now()
            val type = StatsNames.move
            val healthData = healthDataDao.getHealthDataByDateAndType(today, type) ?: HealthData(
                date = today,
                type = type,
                goal = 500,
                progress = 0
            )
            healthData.progress += calories
            healthDataDao.insertOrUpdate(healthData)
            loadTodayData()
        }
    }

    fun updateStand(hours: Int) {
        viewModelScope.launch {
            val today = LocalDate.now()
            val type = StatsNames.stand
            val healthData = healthDataDao.getHealthDataByDateAndType(today, type) ?: HealthData(
                date = today,
                type = type,
                goal = 12,
                progress = 0
            )
            healthData.progress = hours
            healthDataDao.insertOrUpdate(healthData)
            loadTodayData()
        }
    }

    fun updateExercise(minutes: Int) {
        viewModelScope.launch {
            val today = LocalDate.now()
            val type = StatsNames.exercise
            val healthData = healthDataDao.getHealthDataByDateAndType(today, type) ?: HealthData(
                date = today,
                type = type,
                goal = 30,
                progress = 0
            )
            healthData.progress += minutes
            healthDataDao.insertOrUpdate(healthData)
            loadTodayData()
        }
    }

    fun addDistance(meters: Int) {
        viewModelScope.launch {
            val today = LocalDate.now()
            val type = StatsNames.distance
            val healthData = healthDataDao.getHealthDataByDateAndType(today, type) ?: HealthData(
                date = today,
                type = type,
                goal = 10000,
                progress = 0
            )
            healthData.progress += meters
            healthDataDao.insertOrUpdate(healthData)
            loadTodayData()
        }
    }

    fun updateClimbed(floors: Int) {
        viewModelScope.launch {
            val today = LocalDate.now()
            val type = StatsNames.climbed
            val healthData = healthDataDao.getHealthDataByDateAndType(today, type) ?: HealthData(
                date = today,
                type = type,
                goal = 10,
                progress = 0
            )
            healthData.progress = floors
            healthDataDao.insertOrUpdate(healthData)
            loadTodayData()
        }
    }
}
