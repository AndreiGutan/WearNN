package com.example.wearnn.presentation.ui.composables.statsPerDay

import ActivityStat
import android.util.Log
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
import com.example.wearnn.utils.StatsNames
import com.example.wearnn.viewModel.HealthViewModel

@Composable
fun Screen1Day(viewModel: HealthViewModel) {
    val isLoading by viewModel.isLoading.collectAsState()
    val allStats by viewModel.configuredDailyData.collectAsState()

    // Custom order: Move (outer), Exercise (middle), Stand (inner)
    val dailyStats = allStats.filter { it.title in listOf(StatsNames.move, StatsNames.exercise, StatsNames.stand) }
    val orderedStats = listOfNotNull(
        dailyStats.find { it.title == StatsNames.move },
        dailyStats.find { it.title == StatsNames.exercise },
        dailyStats.find { it.title == StatsNames.stand }
    )  // Ensure no null values are included

    Log.d("Screen1Day", "Loading: $isLoading, Filtered stats count: ${dailyStats.size}")

    if (isLoading) {
        Text("Loading...", style = TextStyle(fontSize = 18.sp))
    } else {
        if (dailyStats.isEmpty()) {
            Text("No data available", style = TextStyle(fontSize = 18.sp))
        } else {
            DrawStats(orderedStats)
        }
    }
}

@Composable
private fun DrawStats(dailyStats: List<ActivityStat>) {
    val strokeWidth = 50f
    val spaceBetweenArcs = 7f

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.size(300.dp)) {
            var outerRadius = (size.minDimension - strokeWidth * 1.2f) / 2
            dailyStats.forEach { stat ->
                val startAngle = -225f
                val sweepAngle = stat.angle

                stat.color?.let { color ->
                    drawArc(
                        color = color.copy(alpha = 0.3f),
                        startAngle = startAngle,
                        sweepAngle = 270f,
                        useCenter = false,
                        topLeft = center - Offset(outerRadius, outerRadius),
                        size = Size(outerRadius * 2, outerRadius * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    if (sweepAngle != null) {
                        drawArc(
                            color = color,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            topLeft = center - Offset(outerRadius, outerRadius),
                            size = Size(outerRadius * 2, outerRadius * 2),
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                }
                outerRadius -= (strokeWidth + spaceBetweenArcs)
            }
        }
        DisplayTextBelowArcs(dailyStats, Modifier.align(Alignment.BottomCenter))
    }
}


@Composable
private fun DisplayTextBelowArcs(dailyStats: List<ActivityStat>, modifier: Modifier) {
    Column(
        modifier = modifier.padding(bottom = 0.dp),
        verticalArrangement = Arrangement.spacedBy((-3).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        dailyStats.reversed().forEach { stat ->
            stat.color?.let { color ->
                Text(
                    text = "${stat.progress}",
                    color = color,
                    fontFamily = AppFonts.bebasNeueFont,
                    style = TextStyle(fontSize = 26.sp)
                )
            }
        }
    }
}
