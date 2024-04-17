package com.example.wearnn.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wearnn.data.dao.HealthDataDao
import com.example.wearnn.viewModel.HealthViewModel

class HealthViewModelFactory(private val healthDataDao: HealthDataDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HealthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HealthViewModel(healthDataDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
