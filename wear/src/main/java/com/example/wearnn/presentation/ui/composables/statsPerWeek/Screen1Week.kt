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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.wearnn.data.model.HealthData
import com.example.wearnn.utils.AppColors
import com.example.wearnn.utils.StatsNames
import com.example.wearnn.viewModel.HealthViewModel

@Composable
fun Screen1Week(healthViewModel: HealthViewModel) {
    val weeklyData by healthViewModel.weeklyData.collectAsState()

    val averageMove = weeklyData.flatten().filter { it.type == StatsNames.move }.map { it.progress.toDouble() }.average()
    val averageExercise = weeklyData.flatten().filter { it.type == StatsNames.exercise }.map { it.progress.toDouble() }.average()
    val averageStand = weeklyData.flatten().filter { it.type == StatsNames.stand }.map { it.progress.toDouble() }.average()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This Week", fontSize = 18.sp, textAlign = TextAlign.Center)
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            weeklyData.zip(listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")).forEach { (data, day) ->
                MiniDayStats(healthData = data, day = day)
            }
        }
        // Display averages
        Text(text = "Averages:", fontSize = 16.sp, textAlign = TextAlign.Center)
        Text(text = "Move: ${String.format("%.2f", averageMove)}", fontSize = 14.sp)
        Text(text = "Exercise: ${String.format("%.2f", averageExercise)}", fontSize = 14.sp)
        Text(text = "Stand: ${String.format("%.2f", averageStand)}", fontSize = 14.sp)
    }
}

@Composable
fun MiniDayStats(healthData: List<HealthData>, day: String, modifier: Modifier = Modifier) {
    val colorMap = mapOf(
        StatsNames.move to AppColors.caloriesRed,
        StatsNames.exercise to AppColors.activityYellow,
        StatsNames.stand to AppColors.standBlue
    )

    Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = day, fontSize = 10.sp, textAlign = TextAlign.Center)
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(25.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                var outerRadius = (size.minDimension - 6f * 2f) / 2
                healthData.forEach { data ->
                    val startAngle = -225f
                    val sweepAngle = 270f * data.progress / data.goal
                    val color = colorMap[data.type] ?: Color.Gray  // Default to gray if type is not defined
                    drawArc(
                        color = color.copy(alpha = 0.3f),
                        startAngle = startAngle,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = center - Offset(outerRadius, outerRadius),
                        size = Size(outerRadius * 2, outerRadius * 2),
                        style = Stroke(width = 6f)
                    )
                    drawArc(
                        color = color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = false,
                        topLeft = center - Offset(outerRadius, outerRadius),
                        size = Size(outerRadius * 2, outerRadius * 2),
                        style = Stroke(width = 6f)
                    )
                    outerRadius -= (6f + 1f)
                }
            }
        }
    }
}