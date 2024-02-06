package com.example.productcalculator.ui

import com.example.productcalculator.data.Product

sealed interface ProductEvent {
    object SaveProduct: ProductEvent
    data class SetProductName(val productName: String): ProductEvent
    data class SetProductPrice(val productPrice: Double): ProductEvent
    object HideDialog: ProductEvent
    object ShowDialog: ProductEvent
    data class DeleteProduct(val product: Product): ProductEvent
}