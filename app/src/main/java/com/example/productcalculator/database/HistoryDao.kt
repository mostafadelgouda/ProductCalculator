package com.example.productcalculator.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.productcalculator.data.History
import com.example.productcalculator.data.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Upsert
    suspend fun upsertHistoryItem(history: History)

    @Delete
    suspend fun deleteHistoryItem(history: History)

    @Query("DELETE FROM history")
    suspend fun clearHistory(): Int

    @Query("SELECT * FROM history ORDER BY date")
    fun getHistory(): Flow<List<History>>
}