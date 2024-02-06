package com.example.productcalculator.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.PrimaryKey
import com.example.productcalculator.ProductCalculatorApplication
import com.example.productcalculator.data.History
import com.example.productcalculator.data.Product
import com.example.productcalculator.database.HistoryDao
import com.example.productcalculator.database.ProductDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.LocalDateTime

//data class HistoryState @RequiresApi(Build.VERSION_CODES.O) constructor(
//    val productsId: List<Int> = emptyList(),
//    val totalPrice: Double =0.0,
//    //val date: LocalDateTime = LocalDateTime.now(),
//    val id: Int = 0,
//)

@RequiresApi(Build.VERSION_CODES.O)
class ProductCalculatorViewModel(
    private val productDao: ProductDao,
    private val historyDao: HistoryDao,
): ViewModel() {

    private val _itemsIds = MutableStateFlow<List<Pair<Int, Int>>>(emptyList())
    val itemsIds: StateFlow<List<Pair<Int, Int>>> = _itemsIds.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    fun addProductId(itemId: Int) {
        viewModelScope.launch {
            val currentItemsIds = _itemsIds.value.toMutableList()
            var flag = 0
            var totalPrice = _totalPrice.value
            for(id in 0 until currentItemsIds.size){
                if(currentItemsIds[id].first == itemId){
                    currentItemsIds[id] = Pair(currentItemsIds[id].first, currentItemsIds[id].second + 1)
                    flag = 1
                    break
                }
            }
            if(flag == 0){
                currentItemsIds.add(Pair(itemId, 1))

            }
            totalPrice += (getProductWithId(itemId).firstOrNull()?.price ?: 0.0)
            _itemsIds.emit(currentItemsIds)
            _totalPrice.emit(totalPrice)
        }
    }
    fun removeProductWithId(itemId: Int) {
        viewModelScope.launch {
            val currentItemsIds = _itemsIds.value.toMutableList()
            var totalPrice = _totalPrice.value
            for(id in 0 until currentItemsIds.size){
                if(currentItemsIds[id].first == itemId){
                    if(currentItemsIds[id].second > 1)
                        currentItemsIds[id] = Pair(currentItemsIds[id].first, currentItemsIds[id].second - 1)
                    else
                        currentItemsIds.removeAt(id)
                    break
                }
            }

            totalPrice -= (getProductWithId(itemId).firstOrNull()?.price ?: 0.0)
            _itemsIds.emit(currentItemsIds)
            _totalPrice.emit(totalPrice)
        }
    }
    fun deleteLastProductId() {
        viewModelScope.launch {
            val currentItemsIds = _itemsIds.value.toMutableList()
            if(currentItemsIds.isNotEmpty()) {
                if(currentItemsIds.last().second > 1)
                    currentItemsIds[currentItemsIds.size - 1] = Pair(currentItemsIds.last().first, currentItemsIds.last().second - 1)
                currentItemsIds.removeLast()
            }
            _itemsIds.emit(currentItemsIds)
        }
    }
    fun clearProductsIds() {
        viewModelScope.launch {
            _itemsIds.emit(emptyList())
            _totalPrice.emit(0.0)
        }
    }
    fun getProducts(): Flow<List<Product>> = productDao.getProducts()
    //fun getProductsWithIds(ids: List<Int>): Flow<List<Product>> = productDao.getProductsByIds(ids)
    fun getProductWithId(id: Int): Flow<Product> = productDao.getProductById(id)
    fun getHistory(): Flow<List<History>> = historyDao.getHistory()
    fun addHistoryItem(historyItem: History){
        viewModelScope.launch {
            if(historyItem.productsId.isNotEmpty())
                historyDao.upsertHistoryItem(historyItem)
        }
    }
    fun deleteHistoryItem(historyItem: History){
        viewModelScope.launch {
            historyDao.deleteHistoryItem(historyItem)
        }
    }
    fun clearHistory(){
        viewModelScope.launch {
            //historyDao.clearHistory()
        }
    }
    fun addProduct(product: Product){
        viewModelScope.launch {
            productDao.upsertProduct(product)
        }
    }
    fun deleteProduct(product: Product){
        viewModelScope.launch {
            productDao.deleteProduct(product)
        }
    }


    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ProductCalculatorApplication)
                ProductCalculatorViewModel(application.productDb.dao, application.historyDb.dao)
            }
        }
    }
}