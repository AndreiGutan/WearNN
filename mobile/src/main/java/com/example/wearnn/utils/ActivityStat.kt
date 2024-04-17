package com.example.wearnn.utils

import androidx.compose.ui.graphics.Color

// Modify ActivityStat to include an angle calculation based on progress and goal
data class ActivityStat(
    val title: String,
    val unit: String,
    val progress: Int,
    val goal: Int? = null,  // Make goal optional
    val color: Color? = null,  // Make color optional
    val angle: Float? = null  // Make angle optional
)
