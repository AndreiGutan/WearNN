package com.example.wearnn.presentation.ui.composables.challenges

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.wearnn.R


@Composable
fun AnimatedHeart(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "animationParams"
    )

    val svgImage: ImageVector = ImageVector.vectorResource(id = R.drawable.ic_move)

    Box(
        modifier = modifier
            .size(100.dp) // Set a fixed size for the Box
            .padding(8.dp) // Adjust padding as needed
            .graphicsLayer(scaleX = scale, scaleY = scale),
        contentAlignment = Alignment.Center // Center the content in the Box
    ) {
        Image(
            painter = rememberVectorPainter(image = svgImage),
            contentDescription = "Animated Heart",
            modifier = Modifier.size(100.dp) // Set a fixed size for the Image
        )
    }
}