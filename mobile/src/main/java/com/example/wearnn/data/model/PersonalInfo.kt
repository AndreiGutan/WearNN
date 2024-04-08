package com.example.wearnn.data.model

data class PersonalInfo(
    val firstName: String,
    val lastName: String,
    val dateOfBirth: String, // Consider using a date type
    val gender: String,
    val height: Float, // in centimeters or inches
    val weight: Float, // in kilograms or pounds
    val activityLevel: String, // e.g., sedentary, active, very active
    // Add any other health-related metrics if necessary
)
