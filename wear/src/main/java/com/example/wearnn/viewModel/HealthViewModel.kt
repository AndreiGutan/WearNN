package com.example.wearnn.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wearnn.controller.ProgressController
import com.example.wearnn.data.dao.HealthDataDao
import com.example.wearnn.data.model.HealthData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class HealthViewModel(
    private val healthDataDao: HealthDataDao,
    private val progressController: ProgressController
) : ViewModel() {

    private val _weeklyData = MutableStateFlow<List<List<HealthData>>>(emptyList())
    val weeklyData: StateFlow<List<List<HealthData>>> = _weeklyData

    init {
        viewModelScope.launch {
            // Example of using both the DAO and the controller
            _weeklyData.value = progressController.getWeeklyData()
        }
    }

    fun addDailyData(data: HealthData) {
        viewModelScope.launch {
            healthDataDao.insertHealthData(data)
            // Update the in-memory data after adding new data
            _weeklyData.value = progressController.getWeeklyData()
        }
    }

    fun isDayCompleted(healthDataList: List<HealthData>): Boolean {
        if (healthDataList.isEmpty()) return false
        val individualCompletion = healthDataList.all { it.progress.toDouble() / it.goal >= 0.8 }
        val averageCompletion = healthDataList.sumOf { it.progress.toDouble() / it.goal } / healthDataList.size > 0.9
        return individualCompletion && averageCompletion
    }

    fun calculateCompletionRates() {
        viewModelScope.launch {
            val completionRates = progressController.calculateCompletionRates(_weeklyData.value)
            // This can be used to update a UI component or another state holder
        }
    }
}
