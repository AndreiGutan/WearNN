import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.example.wearnn.R
import com.example.wearnn.data.model.HealthData
import com.example.wearnn.utils.AppColors
import com.example.wearnn.utils.AppFonts
import com.example.wearnn.utils.StatsNames
import com.example.wearnn.viewModel.HealthViewModel

@Composable
fun Screen1Week(healthViewModel: HealthViewModel) {
    val weeklyData by healthViewModel.weeklyData.collectAsState()

    val filteredWeeklyData = weeklyData.map { dailyData ->
        dailyData.filter { it.type in listOf(StatsNames.move, StatsNames.exercise, StatsNames.stand) }
    }

    val averageMove = filteredWeeklyData.flatten().filter { it.type == StatsNames.move }.map { it.progress.toDouble() }.average()
    val averageExercise = filteredWeeklyData.flatten().filter { it.type == StatsNames.exercise }.map { it.progress.toDouble() }.average()
    val averageStand = filteredWeeklyData.flatten().filter { it.type == StatsNames.stand }.map { it.progress.toDouble() }.average()

    // Counting completed days
    val completedDays = filteredWeeklyData.count { dayData ->
        healthViewModel.isDayCompleted(dayData)
    }

    Column(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "This Week",
            fontFamily = AppFonts.bebasNeueFont,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = "Days Completed $completedDays/7",
            fontFamily = AppFonts.bebasNeueFont,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            filteredWeeklyData.zip(listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")).forEach { (data, day) ->
                MiniDayStats(healthData = data, day = day)
            }
        }
        Column(modifier = Modifier.padding(top = 0.dp)) {
            AverageStats("Move", averageMove, R.drawable.ic_move, "kcal")
            AverageStats("Exercise", averageExercise, R.drawable.ic_exercise, "mins")
            AverageStats("Stand", averageStand, R.drawable.ic_stand, "hrs")
        }
    }
}

@Composable
fun AverageStats(label: String, average: Double, iconResId: Int, unit: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = "$label icon",
            tint = Color.Unspecified, // Use this to not apply any tint
            modifier = Modifier.size(28.dp).padding(end = 5.dp)
        )
        Text(
            text = "Avg: ${String.format("%.1f", average)} $unit",
            fontFamily = AppFonts.bebasNeueFont,
            fontSize = 14.sp
        )
    }
}

@Composable
fun MiniDayStats(healthData: List<HealthData>, day: String, modifier: Modifier = Modifier) {
    val colorMap = mapOf(
        StatsNames.move to AppColors.caloriesRed,
        StatsNames.exercise to AppColors.activityYellow,
        StatsNames.stand to AppColors.standBlue
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day,
            fontFamily = AppFonts.bebasNeueFont,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(29.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeWidth = 6f
                var outerRadius = (size.minDimension - strokeWidth * 2) / 2
                healthData.forEach { data ->
                    val startAngle = -225f
                    val sweepAngle = 270f * data.progress / data.goal
                    val color = colorMap[data.type] ?: Color.Gray

                    // Background arc
                    drawArc(
                        color = color.copy(alpha = 0.3f),
                        startAngle = startAngle,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = center - Offset(outerRadius, outerRadius),
                        size = Size(outerRadius * 2, outerRadius * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    // Progress arc
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = center - Offset(outerRadius, outerRadius),
                        size = Size(outerRadius * 2, outerRadius * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    outerRadius -= (strokeWidth + 1f)  // Adjusted for more spacing
                }
            }
        }
    }
}


