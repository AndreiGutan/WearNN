package com.example.wearnn.data.model

// Place this inside a suitable package, for example, 'data/model'
data class HealthStats(
    val caloriesBurned: Int,
    val steps: Int,
    val exerciseMinutes: Int,
    val distanceKm: Int,
    val climbedMeters: Int,
    val standingHours: Int,

    val dailyStepGoal: Int = 10000 // Adding a daily goal for steps as an example
)
