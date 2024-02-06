package com.example.productcalculator.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.productcalculator.data.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Upsert
    suspend fun upsertProduct(product: Product)
    @Delete
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM product ORDER BY name")
    fun getProducts(): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE id IN (:ids)")
    fun getProductsByIds(ids: List<Int>): Flow<List<Product>>
    @Query("SELECT * FROM product WHERE id = :id")
    fun getProductById(id: Int): Flow<Product>

}