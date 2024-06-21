package com.example.wearnn.data.database

import com.example.wearnn.data.model.HealthData
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.wearnn.data.dao.HealthDataDao
import com.example.wearnn.utils.StatsNames
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

@Database(entities = [HealthData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun healthDataDao(): HealthDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Log.d("AppDatabase", "s4")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wearnn_database"

                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            Log.d("AppDatabase", "s5")
                            super.onCreate(db)
                            val dao = getDatabase(context).healthDataDao()
                            Log.d("AppDatabase", "s2")
                            // Using CoroutineScope to launch a coroutine
                            CoroutineScope(Dispatchers.IO).launch {
                                dao.insertAll(provideSimulationData())
                                Log.d("AppDatabase", "s1")
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun provideSimulationData(): List<HealthData> {
            val dataList = mutableListOf<HealthData>()
            val startDate = LocalDate.now().minusDays(7)  // Starting 8 days ago

            // Define the number of days for which you want to generate data
            val numberOfDays = 8

            for (i in 0 until numberOfDays) {
                // Move (Calories burned: 100 to 500)
                dataList.add(
                    HealthData(
                        progress = (100..500).random(),
                        goal = 500,
                        date = startDate.plusDays(i.toLong()),
                        type = StatsNames.move
                    )
                )

                // Stand (Hours: 1 to 16, not exceeding 24)
                dataList.add(
                    HealthData(
                        progress = (1..16).random(),
                        goal = 16,
                        date = startDate.plusDays(i.toLong()),
                        type = StatsNames.stand
                    )
                )

                // Exercise (Minutes: 60 to 360)
                dataList.add(
                    HealthData(
                        progress = (60..360).random(),
                        goal = 360,
                        date = startDate.plusDays(i.toLong()),
                        type = StatsNames.exercise
                    )
                )

                // Steps (1000 to 5000)
                dataList.add(
                    HealthData(
                        progress = (1000..5000).random(),
                        goal = 5000,
                        date = startDate.plusDays(i.toLong()),
                        type = StatsNames.steps
                    )
                )

                // Distance (0.2km to 10km)
                dataList.add(
                    HealthData(
                        progress = (200..10000).random(),  // Progress in meters for consistency
                        goal = 10000,
                        date = startDate.plusDays(i.toLong()),
                        type = StatsNames.distance
                    )
                )

                // Climbed (2m to 60m)
                dataList.add(
                    HealthData(
                        progress = (2..60).random(),
                        goal = 60,
                        date = startDate.plusDays(i.toLong()),
                        type = StatsNames.climbed
                    )
                )
            }

            return dataList
        }

    }
}
