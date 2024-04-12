import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
@Composable
fun MiniDayStats(healthData: List<HealthData>, modifier: Modifier = Modifier) {
    val strokeWidth = 6f  // Reduced stroke width for thinner arcs
    val spaceBetweenArcs = 1f  // Minimal space between arcs for better visual separation

    Box(contentAlignment = Alignment.Center, modifier = modifier.size(25.dp)) {  // Adjusted size for slightly bigger arcs
        Canvas(modifier = Modifier.fillMaxSize()) {
            var outerRadius = (size.minDimension - strokeWidth * 2f) / 2  // Adjust outer radius for new size
            healthData.forEachIndexed { index, data ->
                val startAngle = -225f
                val sweepAngle = 270f * data.progress / data.goal

                // Draw the background arc
                drawArc(
                    color = data.color.copy(alpha = 0.3f),
                    startAngle = startAngle,
                    sweepAngle = 270f,
                    useCenter = false,
                    topLeft = center - Offset(outerRadius, outerRadius),
                    size = Size(outerRadius * 2, outerRadius * 2),
                    style = Stroke(width = strokeWidth)
                )
                // Draw the progress arc
                drawArc(
                    color = data.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = center - Offset(outerRadius, outerRadius),
                    size = Size(outerRadius * 2, outerRadius * 2),
                    style = Stroke(width = strokeWidth)
                )
                outerRadius -= (strokeWidth + spaceBetweenArcs)
            }
        }
    }
}

@Composable
fun Screen1Week(weeklyData: List<List<HealthData>>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,  // Center content vertically
        horizontalAlignment = Alignment.CenterHorizontally  // Center content horizontally
    ) {
        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()).padding(all = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly  // Evenly space items
        ) {
            weeklyData.forEach { dayData ->
                MiniDayStats(healthData = dayData, modifier = Modifier.padding(horizontal = 2.dp))  // Adjust horizontal padding if needed
            }
        }
    }
}
