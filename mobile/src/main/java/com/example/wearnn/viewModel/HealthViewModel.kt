package com.example.wearnn.viewModel


import ActivityStat
import android.util.Log
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
                Log.d("HealthViewModelMobile", "Today Data About to load today's data")
                healthDataDao.loadHealthDataForDay(LocalDate.now()).collect { data ->
                    Log.d("HealthViewModelMobile", "Today Data Data loaded: $data")
                    _dailyData.value = data
                    Log.d("loadTodayDataMobile", "loadTodayData: ${_dailyData.value.joinToString { "HealthData(id=${it.id}, progress=${it.progress}, goal=${it.goal}, date=${it.date}, type=${it.type})" }}")

                    convertHealthDataToActivityStats()
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                Log.e("HealthViewModel", "Today Data Failed to load data: ${e.message}")
                _isLoading.value = false
            }
        }
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
            Log.d("HealthViewModelMobile", "convertHealthDataToActivityStats Converting health data to activity stats")
            val validStats = _dailyData.value.filter { data ->
                Log.d("HealthViewModelMobile", "valid stats get: $data")
                data.type in listOf(StatsNames.move, StatsNames.exercise, StatsNames.stand, StatsNames.steps, StatsNames.distance, StatsNames.climbed, StatsNames.heartRate)  // Added "heartRate"
            }
            Log.d("HealthViewModelMobile", "!!!!!!: $validStats")
            val stats = validStats.map { healthData ->
                Log.d("HealthViewModelMobile", "?? s ?$healthData")
                mapToActivityStat(healthData)
            }
            _configuredDailyData.value = stats
            Log.d("HealthViewModelMobile", "convertHealthDataToActivityStats Activity stats configured: ${_configuredDailyData.value}")
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
        Log.d("HealthViewModelMobile", "FFFFFFFFFFFFFFFFFFFF stat: ${healthData.type}")
        return when (healthData.type) {
            StatsNames.move, StatsNames.exercise, StatsNames.stand, StatsNames.steps, StatsNames.distance, StatsNames.climbed -> {
                Log.d("HealthViewModelMobile", "!!1")
                ActivityStat(
                    title = healthData.type,
                    unit = when (healthData.type) {
                        StatsNames.exercise -> Units.minutes
                        StatsNames.move -> Units.calories
                        StatsNames.steps -> Units.steps
                        StatsNames.distance -> Units.kilometers
                        StatsNames.climbed -> Units.meters
                        StatsNames.stand -> Units.hours
                        else -> Units.minutes // Default to minutes for exercise, stand
                    },
                    progress = healthData.progress,
                    goal = healthData.goal,
                    angle = calculateAngle(healthData.progress, healthData.goal),
                    color = when (healthData.type) {
                        StatsNames.move -> AppColors.caloriesRed
                        StatsNames.exercise -> AppColors.activityYellow
                        StatsNames.stand -> AppColors.standBlue
                        else -> Color.Gray // Default color
                    }
                )
            }
            else -> {
                Log.w("HealthViewModelMobile", "Encountered unknown health data type: ${healthData.type}")
                ActivityStat(
                    title = "Unknown",
                    unit = "",
                    progress = 0,
                    goal = 0,
                    angle = 0f,
                    color = Color.Gray
                )
            }
        }
    }

    private fun calculateAngle(progress: Int, goal: Int): Float {
        return (270f * progress / goal).coerceIn(0f, 270f)
    }

    fun countCompletedDays(weeklyData: List<List<HealthData>>): Int {
        return weeklyData.count { dailyData ->
            isDayCompleted(dailyData)
        }
    }

    fun isDayCompleted(healthDataList: List<HealthData>): Boolean {
        if (healthDataList.isEmpty()) return false
        val individualCompletion = healthDataList.all { it.progress.toDouble() / it.goal >= 0.7 }
        val averageCompletion = healthDataList.sumOf { it.progress.toDouble() / it.goal } / healthDataList.size > 0.8
        return individualCompletion && averageCompletion
    }
}
