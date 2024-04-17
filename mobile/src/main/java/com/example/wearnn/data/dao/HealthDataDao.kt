package com.example.wearnn.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.wearnn.data.model.HealthData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HealthDataDao {
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