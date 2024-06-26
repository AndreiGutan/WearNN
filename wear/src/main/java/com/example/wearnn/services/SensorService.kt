package com.example.wearnn.services

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import com.example.wearnn.utils.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.Instant

class SensorService : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var heartRateSensor: Sensor? = null

    private var steps: Int = 0
    private var previousSteps: Int = 0
    private var heartRate: Int = 0
    private var inTargetZone = false
    private var targetZoneStartTime: Instant? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("SENSOR", "Service Created")
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SENSOR", "Service Started")
        registerSensors()
        return START_STICKY
    }

    private fun registerSensors() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
        heartRateSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_STEP_COUNTER -> {
                    steps = it.values[0].toInt()
                    val newSteps = steps - previousSteps
                    previousSteps = steps

                    if (newSteps > 0) {
                        Log.d("SENSOR", "Steps detected: $newSteps")
                        ViewModelProvider.healthViewModel?.updateSteps(steps)
                        updateDistance(newSteps)
                        updateCaloriesBurned(newSteps)
                    }
                }
                Sensor.TYPE_HEART_RATE -> {
                    heartRate = it.values[0].toInt()
                    Log.d("SENSOR", "Heart rate detected: $heartRate")
                    updateExercise()
                }
                else -> {}
            }
        }
    }

    private fun updateCaloriesBurned(newSteps: Int) {
        val caloriesPerStep = 0.05
        val heartRateFactor = when {
            heartRate <= 70 -> 1.0
            heartRate in 71..80 -> 1.0
            heartRate in 81..90 -> 1.0
            heartRate in 91..100 -> 1.0
            heartRate > 100 -> 1.0
            else -> 1.0
        }

        val caloriesBurned = newSteps * caloriesPerStep * heartRateFactor
        ViewModelProvider.healthViewModel?.addCalories(caloriesBurned.toInt())
    }

    private fun updateExercise() {
        val targetZoneMin = 66
        val targetZoneMax = 162

        if (heartRate in targetZoneMin..targetZoneMax) {
            if (!inTargetZone) {
                inTargetZone = true
                targetZoneStartTime = Instant.now()
            }
        } else {
            if (inTargetZone) {
                inTargetZone = false
                val durationInZone = Duration.between(targetZoneStartTime, Instant.now()).toMinutes().toInt()
                targetZoneStartTime = null
                ViewModelProvider.healthViewModel?.updateExercise(durationInZone)
            }
        }
    }

    private fun updateDistance(newSteps: Int) {
        val userHeight = 1.75  // in meters
        val strideLength = userHeight * 0.78  // assuming walking
        val distance = (newSteps * strideLength).toInt()  // distance in meters
        ViewModelProvider.healthViewModel?.addDistance(distance)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
