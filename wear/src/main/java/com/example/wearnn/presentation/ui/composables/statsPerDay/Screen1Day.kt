package com.example.wearnn.presentation.ui.composables.statsPerDay


import HealthViewModelPreferences
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.wearnn.utils.AppFonts
import com.example.wearnn.viewModel.HealthViewModel

@Composable
fun Screen1Day(viewModel: HealthViewModel) {
    // Collect from configuredDailyData instead of activityStats
    val dailyStats by viewModel.configuredDailyData.collectAsState()

    val strokeWidth = 50f // Define the stroke width
    val spaceBetweenArcs = 7f // Define space between arcs

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.size(300.dp)) {  // Adjust size as needed
            var outerRadius = (size.minDimension - strokeWidth * 1.2f) / 2
            dailyStats.forEach { stat ->
                val startAngle = -225f
                val sweepAngle = stat.angle  // Use the calculated angle

                // Draw the "empty" background arc
                stat.color?.let {
                    drawArc(
                        color = it.copy(alpha = 0.3f),
                        startAngle = startAngle,
                        sweepAngle = 270f,  // Full arc to show the goal
                        useCenter = false,
                        topLeft = center - Offset(outerRadius, outerRadius),
                        size = Size(outerRadius * 2, outerRadius * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
                // Draw the "filled" arc representing the current progress
                stat.color?.let {
                    if (sweepAngle != null) {
                        drawArc(
                            color = it,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,  // Dynamic based on progress
                            useCenter = false,
                            topLeft = center - Offset(outerRadius, outerRadius),
                            size = Size(outerRadius * 2, outerRadius * 2),
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                }
                outerRadius -= (strokeWidth + spaceBetweenArcs)  // Reduce the radius for the next arc
            }
        }

        // Positioning the text below the arcs
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 0.dp),
            verticalArrangement = Arrangement.spacedBy(-3.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            dailyStats.reversed().forEach { stat ->
                stat.color?.let {
                    Text(
                        text = "${stat.progress}",  // Display the progress value
                        color = it,  // Use the color directly from ActivityStat
                        fontFamily = AppFonts.bebasNeueFont,
                        style = TextStyle(fontSize = 26.sp)  // Adjust font size as necessary
                    )
                }
            }
        }
    }
}
