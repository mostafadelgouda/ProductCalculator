package com.example.productcalculator.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? {
        return value?.let {
            return LocalDateTime.parse(it, formatter)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): String? {
        return date?.format(formatter)
    }

    @TypeConverter
    fun fromIntegerList(value: List<Int>?): String? {
        return value?.joinToString(",")
    }

    @TypeConverter
    fun toIntegerList(value: String?): List<Int>? {
        return value?.split(",")?.map { it.toInt() }
    }

    @TypeConverter
    fun fromPairList(value: List<Pair<Int, Int>>?): String? {
        return value?.joinToString(";") { "${it.first},${it.second}" }
    }

    @TypeConverter
    fun toPairList(value: String?): List<Pair<Int, Int>>? {
        return value?.split(";")?.mapNotNull {
            val pairValues = it.split(",")
            if (pairValues.size == 2) {
                Pair(pairValues[0].toInt(), pairValues[1].toInt())
            } else {
                null
            }
        }
    }
}