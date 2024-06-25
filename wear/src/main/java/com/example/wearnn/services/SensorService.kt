// File: SensorService.kt

package com.example.wearnn.services

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import com.example.wearnn.data.database.AppDatabase
import com.example.wearnn.utils.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SensorService : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        Log.d("SENSOR", "Service Created")
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        database = AppDatabase.getDatabase(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("SENSOR", "Service Started")
        registerSensor()
        return START_STICKY
    }

    private fun registerSensor() {
        Log.d("SENSOR", "Registering Sensor")
        if (stepSensor == null) {
            Log.d("SENSOR", "Sensor is null")
        } else {
            Log.d("SENSOR", "Sensor is not null")
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val steps = event.values[0].toInt()
            Log.d("SENSOR", "Steps detected: $steps")
            updateSteps(steps)
        }
    }

    private fun updateSteps(steps: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            ViewModelProvider.healthViewModel.updateSteps(steps)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
