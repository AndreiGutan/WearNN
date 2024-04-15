package com.example.wearnn.data.database

import com.example.wearnn.data.model.HealthData
import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.wearnn.data.dao.HealthDataDao
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
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wearnn_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val dao = getDatabase(context).healthDataDao()

                            // Using CoroutineScope to launch a coroutine
                            CoroutineScope(Dispatchers.IO).launch {
                                dao.insertAll(provideSimulationData())
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
            for (i in 0 until 8) {
                dataList.add(
                    HealthData(
                        progress = (1000..5000).random(),
                        goal = 4000,
                        date = startDate.plusDays(i.toLong()),
                        type = if (i % 2 == 0) "steps" else "heartRate"  // Alternating types
                    )
                )
            }
            return dataList
        }
    }
}
