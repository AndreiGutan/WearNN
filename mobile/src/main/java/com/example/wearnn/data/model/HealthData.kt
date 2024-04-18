package com.example.wearnn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.time.LocalDate

@Entity(tableName = "health_data")
data class HealthData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val progress: Int,  // Assuming this is a sum of daily activity or similar
    val goal: Int,  // Daily goal
    val date: LocalDate,
    val type: String  // Type of the data "Move" "Stand" "Exercise" "Steps" "Distance" "Climbed"
)
