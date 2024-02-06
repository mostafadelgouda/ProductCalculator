package com.example.productcalculator.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.productcalculator.data.Product

@Database(
    entities = [Product::class],
    version = 1
)
abstract class ProductDatabase: RoomDatabase() {
    abstract val dao: ProductDao

}