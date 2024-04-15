import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wearnn.utils.AppColors
import com.example.wearnn.utils.Goals
import com.example.wearnn.utils.StatsNames
import com.example.wearnn.utils.Units
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HealthViewModelPreferences(
    private val context: Context
) : ViewModel() {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "health_preferences",
        Context.MODE_PRIVATE
    )

    private val _dailyData = MutableStateFlow<List<HealthDataPreferences>>(emptyList())
    val dailyData: StateFlow<List<HealthDataPreferences>> = _dailyData
    private val _weeklyAverageStats = MutableStateFlow<List<ActivityStat>>(emptyList())
    val weeklyAverageStats: StateFlow<List<ActivityStat>> = _weeklyAverageStats
    private val _configuredDailyData = MutableStateFlow<List<ActivityStat>>(emptyList())
    val configuredDailyData: StateFlow<List<ActivityStat>> = _configuredDailyData
    private val _weeklyData = MutableStateFlow<List<List<HealthDataPreferences>>>(emptyList())
    val weeklyData: StateFlow<List<List<HealthDataPreferences>>> = _weeklyData

    init {
        loadTodayData()
        loadWeeklyData()
        calculateWeeklyAverages()
        convertHealthDataToActivityStats()
    }

    private fun calculateWeeklyAverages() {
        viewModelScope.launch {
            // Calculate weekly averages from preferences or provide default values
            val stepsAverage = sharedPreferences.getFloat(StatsNames.steps, Goals.moveGoal.toFloat())
            val distanceAverage = sharedPreferences.getFloat(StatsNames.distance, Goals.moveGoal.toFloat())
            val climbedAverage = sharedPreferences.getFloat(StatsNames.climbed, Goals.moveGoal.toFloat())

            _weeklyAverageStats.value = listOf(
                ActivityStat(title = "Avg Steps", unit = "steps", progress = stepsAverage.toInt()),
                ActivityStat(title = "Avg Distance", unit = "km", progress = distanceAverage.toInt()),
                ActivityStat(title = "Avg Climbed", unit = "m", progress = climbedAverage.toInt())
            )
        }
    }

    private fun loadTodayData() {
        viewModelScope.launch {
            // Load daily data from preferences or provide default values
            val progress = sharedPreferences.getInt(StatsNames.move, Goals.moveGoal)
            val goal = sharedPreferences.getInt(StatsNames.move, Goals.moveGoal)
            val date = LocalDate.now()
            val type = "default"

            val todayData = listOf(HealthDataPreferences(progress, goal, date, type))

            _dailyData.value = todayData
            convertHealthDataToActivityStats()
        }
    }


    private fun loadWeeklyData() {
        viewModelScope.launch {
            // Load weekly data from preferences or provide default values
            val startDate = LocalDate.now().minusDays(6)
            val endDate = LocalDate.now()

            val weeklyDataList = mutableListOf<HealthDataPreferences>()
            var currentDate = startDate
            while (currentDate <= endDate) {
                val progress = sharedPreferences.getInt(StatsNames.move, Goals.moveGoal)
                val goal = sharedPreferences.getInt(StatsNames.move, Goals.moveGoal)
                val type = "default"

                weeklyDataList.add(HealthDataPreferences(progress, goal, currentDate, type))
                currentDate = currentDate.plusDays(1)
            }
            _weeklyData.value = listOf(weeklyDataList)
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

    private fun mapToActivityStat(healthData: HealthDataPreferences): ActivityStat {
        // Map HealthData to ActivityStat
        return ActivityStat(
            title = StatsNames.move,
            unit = Units.calories,
            progress = healthData.progress,
            goal = healthData.goal,
            angle = calculateAngle(healthData.progress, healthData.goal),
            color = AppColors.caloriesRed
        )
    }

    private fun calculateAngle(progress: Int, goal: Int): Float {
        // Calculate angle based on progress and goal
        return (270f * progress / goal).coerceIn(0f, 270f)
    }
}
