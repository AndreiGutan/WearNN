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
import java.util.concurrent.Executors

@Database(entities = [HealthData::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun healthDataDao(): HealthDataDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Log.d("AppDatabaseMobile", "s4")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "wearnn_database"

                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            Log.d("AppDatabaseMobile", "s5")
                            super.onCreate(db)
                            val dao = getDatabase(context).healthDataDao()
                            Log.d("AppDatabaseMobile", "s2")
                            // Using CoroutineScope to launch a coroutine
                            CoroutineScope(Dispatchers.IO).launch {
                                //dao.insertAll(provideSimulationData())
                                Log.d("AppDatabaseMobile", "s1")
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}