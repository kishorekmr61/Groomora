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
    val paymentMethod: String = "UPI / Online",
    val trackingNumber: String? = null,
    val refundStatus: OrderRefundStatus = OrderRefundStatus.NONE,
    val cancellationReason: String? = null,
    val canCancel: Boolean = true
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

enum class OrderRefundStatus {
    NONE, INITIATED, PROCESSING, REFUNDED
}

data class OrdersState(
    val isLoading: Boolean = false,
    val orders: List<Order> = emptyList(),
    val cancellingOrderId: String? = null,
    val message: String? = null,
    val error: String? = null
)

sealed interface OrdersIntent {
    data object LoadOrders : OrdersIntent
    data class CancelOrder(val orderId: String, val reason: String) : OrdersIntent
}
