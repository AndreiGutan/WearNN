package com.example.wearnn.controller

import com.example.wearnn.data.model.HealthData
import java.time.LocalDate

class ProgressController {
    private val dailyProgress: MutableList<HealthData> = mutableListOf()

    fun addDailyData(data: HealthData) {
        // Optionally prevent adding duplicate entries for the same day
        if (!dailyProgress.any { it.date == data.date }) {
            dailyProgress.add(data)
        }
    }

    fun getWeeklyData(): List<List<HealthData>> {
        // Group data by date and take the last 7 days
        return dailyProgress
            .groupBy { it.date }
            .entries
            .sortedByDescending { it.key }  // Make sure to sort to get the last 7 days correctly
            .take(7)
            .sortedBy { it.key }  // Sort again for chronological order
            .map { it.value }
    }

    fun calculateCompletionRates(weeklyData: List<List<HealthData>>): List<Boolean> {
        return weeklyData.map { dayData ->
            isDayCompleted(dayData)
        }
    }

    private fun isDayCompleted(healthDataList: List<HealthData>): Boolean {
        if (healthDataList.isEmpty()) return false
        val individualCompletion = healthDataList.all { it.progress.toDouble() / it.goal >= 0.8 }
        val averageCompletion = healthDataList.sumOf { it.progress.toDouble() / it.goal } / healthDataList.size > 0.9
        return individualCompletion && averageCompletion
    }
}
