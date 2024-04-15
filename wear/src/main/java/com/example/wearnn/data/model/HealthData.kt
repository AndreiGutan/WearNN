package com.example.wearnn.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.time.LocalDate

@Entity(tableName = "health_data")
data class HealthData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val progress: Int,
    val goal: Int,
    @ColumnInfo(name = "date") val date: LocalDate,
    @ColumnInfo(name = "type") val type: String  // Add this line
)
