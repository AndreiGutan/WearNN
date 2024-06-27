package com.example.wearnn.viewModel

import android.app.Application
import android.content.Context
import android.os.Vibrator
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.*

class ChallengeViewModel(application: Application) : AndroidViewModel(application) {
    var startChallenge by mutableStateOf(false)
    var resumeChallenge by mutableStateOf(true)
    var caloriesBurned by mutableStateOf(0)
    private val goalCalories = 10
    private val vibrator = application.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun start() {
        startChallenge = true
    }

    fun addCalories(calories: Int) {
        if (startChallenge) {
            caloriesBurned += calories
            if (caloriesBurned >= goalCalories) {
                vibrate()
                CoroutineScope(Dispatchers.Main).launch {
                    delay(4000)  // Delay to allow the vibration to be felt
                    reset()
                }
            }
        }
    }

    private fun vibrate() {
        vibrator.vibrate(3000) // Vibrate for 500 milliseconds
    }

    fun reset() {
        startChallenge = false
        resumeChallenge = true
        caloriesBurned = 0
    }
}

class ChallengeViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChallengeViewModel::class.java)) {
            return ChallengeViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
