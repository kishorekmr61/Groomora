package com.groomora.feature.products

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: String,
    val name: String,
    val brand: String,
    val price: Double,
    val originalPrice: Double? = null,
    val imageUrl: String,
    val description: String,
    val category: String,
    val stockStatus: StockStatus = StockStatus.IN_STOCK
)

enum class StockStatus {
    IN_STOCK, LOW_STOCK, OUT_OF_STOCK
}

data class ProductState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val cartItems: Map<String, Int> = emptyMap(),
    val error: String? = null
)

sealed interface ProductIntent {
    data object LoadProducts : ProductIntent
    data class AddToCart(val productId: String) : ProductIntent
    data class RemoveFromCart(val productId: String) : ProductIntent
}
