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
import kotlinx.coroutines.flow.firstOrNull
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
                Log.d("AppDatabase", "Creating database instance")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wearnn_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val dao = getDatabase(context).healthDataDao()
                            CoroutineScope(Dispatchers.IO).launch {
                                initializeDataIfEmpty(dao)
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun initializeDataIfEmpty(dao: HealthDataDao) {
            val today = LocalDate.now()
            val types = listOf(
                StatsNames.move,
                StatsNames.stand,
                StatsNames.exercise,
                StatsNames.steps,
                StatsNames.distance,
                StatsNames.climbed
            )

            val initialData = types.map {
                HealthData(
                    date = today,
                    type = it,
                    goal = when (it) {
                        StatsNames.move -> 500
                        StatsNames.stand -> 16
                        StatsNames.exercise -> 360
                        StatsNames.steps -> 5000
                        StatsNames.distance -> 10000
                        StatsNames.climbed -> 60
                        else -> 0
                    },
                    progress = 0
                )
            }

            // Check if there's any data in the database
            if (dao.loadHealthDataForDay(today).firstOrNull().isNullOrEmpty()) {
                dao.insertAll(initialData)
            }
        }
    }
}
