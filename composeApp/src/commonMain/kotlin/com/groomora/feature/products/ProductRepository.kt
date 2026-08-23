package com.groomora.feature.products

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    fun getProductsByCategory(category: String): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun getProductDetails(productId: String): Product?
    val cartItems: StateFlow<Map<String, Int>>
    fun addToCart(productId: String)
    fun removeFromCart(productId: String)
    fun clearCart()
}
