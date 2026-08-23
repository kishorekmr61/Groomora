package com.groomora.feature.products

import kotlinx.serialization.Serializable

@Serializable
data class Order(
    val id: String,
    val items: List<OrderItem>,
    val totalAmount: Double,
    val status: OrderStatus,
    val date: String,
    val deliveryAddress: String,
    val trackingNumber: String? = null
)

@Serializable
data class OrderItem(
    val productId: String,
    val productName: String,
    val quantity: Int,
    val price: Double
)

enum class OrderStatus {
    PLACED, PROCESSING, SHIPPED, DELIVERED, CANCELLED, REFUNDED
}

data class OrdersState(
    val isLoading: Boolean = false,
    val orders: List<Order> = emptyList(),
    val error: String? = null
)

sealed interface OrdersIntent {
    data object LoadOrders : OrdersIntent
    data class CancelOrder(val orderId: String) : OrdersIntent
}
