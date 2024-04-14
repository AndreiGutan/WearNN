package com.example.wearnn.presentation.ui.composables.StatsPerDay

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.example.wearnn.data.model.HealthData
import com.example.wearnn.utils.AppFonts
import com.example.wearnn.viewModel.HealthViewModel
import kotlinx.coroutines.flow.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue

@Composable
fun Screen1Day(viewModel: HealthViewModel) {
    // Assuming we can fetch today's data from the ViewModel
    val todayData by viewModel.todayData.observeAsState(initial = emptyList())  // `todayData` should be prepared in ViewModel

    val strokeWidth = 50f // define the stroke width
    val spaceBetweenArcs = 7f // define space between arcs

    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.size(300.dp)) {  // Adjust size as needed
            var outerRadius = (size.minDimension - strokeWidth * 1.2f) / 2
            todayData.forEach { data ->
                val startAngle = -225f
                val sweepAngle = 270f * data.progress / data.goal  // Calculate the percentage of the goal achieved

                // Draw the "empty" background arc
                drawArc(
                    color = data.color.copy(alpha = 0.3f),  // Make the color paler
                    startAngle = startAngle,
                    sweepAngle = 270f,
                    useCenter = false,
                    topLeft = center - Offset(outerRadius, outerRadius),
                    size = Size(outerRadius * 2, outerRadius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                // Draw the "filled" arc representing the current progress
                drawArc(
                    color = data.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = center - Offset(outerRadius, outerRadius),
                    size = Size(outerRadius * 2, outerRadius * 2),
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                outerRadius -= (strokeWidth + spaceBetweenArcs)  // Reduce the radius for the next arc
            }
        }

        // Positioning the text below the arcs
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 0.dp), // Adjust the padding as needed to move the texts further down
            verticalArrangement = Arrangement.spacedBy(-3.dp), // This adds space between texts
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Reverse the order of todayData if needed by using `reversed()`
            todayData.reversed().forEach { data ->
                Text(
                    text = "${data.progress}",
                    color = data.color,
                    fontFamily = AppFonts.bebasNeueFont,
                    style = TextStyle(fontSize = 26.sp) // Specify other text styles if needed
                )
            }
        }
    }
}
