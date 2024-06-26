package com.example.wearnn.data.dao

import androidx.room.*
import com.example.wearnn.data.model.HealthData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HealthDataDao {
    @Query("SELECT * FROM health_data WHERE date = :date AND type = :type LIMIT 1")
    suspend fun getHealthDataByDateAndType(date: LocalDate, type: String): HealthData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(healthData: HealthData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHealthData(data: HealthData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<HealthData>)

    @Query("SELECT * FROM health_data WHERE date BETWEEN :startDate AND :endDate")
    fun loadHealthDataForWeek(startDate: LocalDate, endDate: LocalDate): Flow<List<HealthData>>

    @Query("SELECT * FROM health_data WHERE date = :date")
    fun loadHealthDataForDay(date: LocalDate): Flow<List<HealthData>>

    @Query("SELECT * FROM health_data WHERE type = :type AND date BETWEEN :startDate AND :endDate")
    fun loadHealthDataByType(type: String, startDate: LocalDate, endDate: LocalDate): Flow<List<HealthData>>
}
