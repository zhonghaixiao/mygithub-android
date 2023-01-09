package com.easyhi.manage.data

import androidx.room.TypeConverter
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): DateTime? {
        return value?.let { DateTime(value) }
    }

    @TypeConverter
    fun fromStr(value: String?): DateTime? {
        return value?.let {
            DateTime.parse(value, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"))
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: DateTime?): Long? {
        return date?.let {
            it.millis
        }
    }

    @TypeConverter
    fun dateToStr(date: DateTime?): String? {
        return date?.let {
            it.toString("yyyy-MM-dd HH:mm:ss")
        }
    }

}