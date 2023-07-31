package com.example.meet_up.data.local

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun longToDate(value: Long?): Date? {
        return value?.let { Date(value) }
    }

    @TypeConverter
    fun dateToLong(date: Date?): Long? {
        return date?.time
    }
}