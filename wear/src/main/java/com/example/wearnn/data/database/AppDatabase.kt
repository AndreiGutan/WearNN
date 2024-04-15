package com.example.wearnn.data.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wearnn.data.dao.HealthDataDao
import com.example.wearnn.data.model.HealthData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.time.LocalDate

@Database(entities = [HealthData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun healthDataDao(): HealthDataDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "wearnn_database"
            )
                .fallbackToDestructiveMigration()
                .build()

            // Insert default values here
            runBlocking { insertDefaultValues(db) }

            return db
        }

        private suspend fun insertDefaultValues(database: AppDatabase) {
            // Insert default values for HealthData table
            val defaultHealthData = HealthData(
                progress = 0,
                goal = 0,
                date = LocalDate.now(), // Set the date as today's date or any default date
                type = "default" // Set the default type if applicable
            )
            database.healthDataDao().insertHealthData(defaultHealthData)
            // Insert more default values as needed for other tables
        }
    }
}
