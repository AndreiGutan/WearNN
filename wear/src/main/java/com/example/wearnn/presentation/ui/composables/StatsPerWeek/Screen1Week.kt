import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Text
import com.example.wearnn.data.model.HealthData
import com.example.wearnn.viewModel.HealthViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun Screen1Week(healthViewModel: HealthViewModel) {
    val weeklyData = healthViewModel.weeklyData.collectAsState().value  // Assuming weeklyData is a List<List<HealthData>>

    val completedDays = weeklyData.count { dayData ->
        healthViewModel.isDayCompleted(dayData)  // Correctly pass List<HealthData>
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "This Week", fontSize = 18.sp, textAlign = TextAlign.Center)
        Text(text = "Completed $completedDays/7", fontSize = 14.sp, textAlign = TextAlign.Center)
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            weeklyData.zip(daysOfWeek).forEach { (data, day) ->
                MiniDayStats(healthData = data, day = day)  // Ensure data is a list
            }
        }
    }
}

@Composable
fun MiniDayStats(healthData: List<HealthData>, day: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = day, fontSize = 10.sp, textAlign = TextAlign.Center)
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(25.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                var outerRadius = (size.minDimension - 6f * 2f) / 2
                healthData.forEachIndexed { index, data ->
                    val startAngle = -225f
                    val sweepAngle = 270f * data.progress / data.goal
                    drawArc(
                        color = data.color.copy(alpha = 0.3f),
                        startAngle = startAngle,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = center - Offset(outerRadius, outerRadius),
                        size = Size(outerRadius * 2, outerRadius * 2),
                        style = Stroke(width = 6f)
                    )
                    drawArc(
                        color = data.color,
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
