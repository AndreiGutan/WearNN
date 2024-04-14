package com.example.wearnn.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.wearnn.data.model.HealthData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HealthDataDao {
    @Insert
    suspend fun insertHealthData(healthData: HealthData)

    @Query("SELECT * FROM HealthData WHERE date BETWEEN :startDate AND :endDate ORDER BY date ASC")
    fun loadHealthDataForWeek(startDate: LocalDate, endDate: LocalDate): Flow<List<HealthData>>

    @Query("DELETE FROM HealthData WHERE date < :threshold")
    suspend fun deleteOldData(threshold: LocalDate)
}
