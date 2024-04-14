package com.example.wearnn.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class LocalDateConverter {
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): Long? {
        return date?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    @TypeConverter
    fun toLocalDate(millisSinceEpoch: Long?): LocalDate? {
        return millisSinceEpoch?.let {
            LocalDate.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
        }
    }
}
