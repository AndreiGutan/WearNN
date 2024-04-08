package com.example.wearnn.data.model

data class HealthStats(
    val stepsCount: Int,
    val heartRate: Float, // Average or current
    val caloriesBurned: Int,
    val activeMinutes: Int,
    val distanceWalked: Float, // in kilometers or miles
    val sleepQuality: String, // e.g., poor, average, good
    // Add any other metrics that the watch can track and are relevant to insurance incentives
)
