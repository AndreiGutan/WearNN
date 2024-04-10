package com.example.wearnn.presentation.ui.composables.StatsPerDay

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun Screen1Day(
    progress: Float, // Current progress as a percentage (0f to 1f)
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(100.dp)) {
        val strokeWidth = 10.dp.toPx()
        val topLeft = Offset(strokeWidth, strokeWidth)
        val adjustedSize = Size(
            width = size.width - strokeWidth * 2,
            height = size.height - strokeWidth * 2
        )

        // Draw the background circle
        drawArc(
            color = Color.LightGray,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = adjustedSize, // Use the adjusted size
            style = Stroke(width = strokeWidth)
        )

        // Draw the progress arc
        drawArc(
            color = Color(0xFF_3F_51_B5), // A nice indigo color
            startAngle = -90f, // Start from the top (12 o'clock position)
            sweepAngle = 360f * progress,
            useCenter = false,
            topLeft = topLeft,
            size = adjustedSize, // Use the adjusted size
            style = Stroke(width = strokeWidth)
        )
    }
}

