package com.example.productcalculator.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.Date

@Entity(tableName = "history")
data class History(

    val productsId: List<Pair<Int, Int>>,
    val totalPrice: Double,
    val date: LocalDateTime,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
)
