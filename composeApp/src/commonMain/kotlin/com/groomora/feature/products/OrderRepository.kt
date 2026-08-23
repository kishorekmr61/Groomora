package com.groomora.feature.products

import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getOrders(): Flow<List<Order>>
    suspend fun placeOrder(items: List<OrderItem>, totalAmount: Double, address: String): Order?
    suspend fun cancelOrder(orderId: String): Boolean
}
