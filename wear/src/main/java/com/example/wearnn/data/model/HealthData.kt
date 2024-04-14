package com.example.wearnn.data.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDate

data class HealthData(
    val progress: Int,
    val goal: Int,
    val color: Color,
    val date: LocalDate  // Add a date field to store when the data was recorded
)
