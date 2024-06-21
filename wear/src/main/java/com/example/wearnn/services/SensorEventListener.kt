import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import com.example.wearnn.data.database.AppDatabase
import com.example.wearnn.data.model.HealthData
import com.example.wearnn.utils.StatsNames
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class SensorService : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        database = AppDatabase.getDatabase(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        registerSensor()
        return START_STICKY
    }

    private fun registerSensor() {
        stepSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_STEP_COUNTER) {
            val steps = event.values[0]
            updateStepCountInDatabase(steps)
        }
    }

    private fun updateStepCountInDatabase(steps: Float) {
        CoroutineScope(Dispatchers.IO).launch {
            val today = LocalDate.now()
            val healthDao = database.healthDataDao()
            val todayHealthData = healthDao.getHealthDataByDateAndType(today, StatsNames.steps) ?: HealthData(
                date = today,
                type = StatsNames.steps,
                goal = 5000,  // Define a typical goal or fetch from settings
                progress = 0
            )

            todayHealthData.progress = steps.toInt()
            healthDao.insertOrUpdate(todayHealthData)
        }
    }



    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle sensor accuracy changes if needed
    }

    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
