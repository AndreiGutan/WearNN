package com.example.wearnn.data.model

// Place this inside a suitable package, for example, 'data/model'
data class HealthStats(
    val steps: Int,
    val standingMinutes: Int,
    val caloriesBurned: Int,
    val dailyStepGoal: Int = 10000 // Adding a daily goal for steps as an example
)
