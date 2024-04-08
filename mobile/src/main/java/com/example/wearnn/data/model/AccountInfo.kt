package com.example.wearnn.data.model

data class AccountInfo(
    val email: String,
    val password: String, // Stored securely and preferably hashed
    val membershipNumber: String,
    val insurancePlan: String, // e.g., basic, premium, family
    val rewardsPoints: Int, // Points earned through health activities
    val discountEligibility: Boolean,
    // Additional fields as needed for account management
)
