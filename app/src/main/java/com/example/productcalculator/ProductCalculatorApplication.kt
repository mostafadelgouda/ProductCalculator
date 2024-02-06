package com.example.productcalculator

import android.app.Application
import androidx.room.Room
import com.example.productcalculator.database.HistoryDatabase
import com.example.productcalculator.database.ProductDatabase

class ProductCalculatorApplication : Application() {
    val productDb by lazy {
        Room.databaseBuilder(
            applicationContext,
            ProductDatabase::class.java,
            "product.db"
        ).build()
    }
    val historyDb by lazy {
        Room.databaseBuilder(
            applicationContext,
            HistoryDatabase::class.java,
            "history.db"
        ).build()
    }
}
