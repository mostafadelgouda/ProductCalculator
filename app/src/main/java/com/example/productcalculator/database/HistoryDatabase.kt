package com.example.productcalculator.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.productcalculator.data.History
import com.example.productcalculator.data.Product


@Database(
    entities = [History::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class HistoryDatabase : RoomDatabase() {
    abstract val dao: HistoryDao
}