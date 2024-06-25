package com.example.wearnn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "health_data")
data class HealthData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: LocalDate,
    val type: String,
    val goal: Int,
    var progress: Int  // Now mutable, can be reassigned
)
