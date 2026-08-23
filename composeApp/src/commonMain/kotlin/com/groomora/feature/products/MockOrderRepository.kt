package com.groomora.feature.products

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockOrderRepository : OrderRepository {
    private val _orders = MutableStateFlow(
        listOf(
            Order(
                id = "ORD-8921",
                items = listOf(OrderItem("pr1", "Premium Beard Growth & Styling Oil", 1, 850.0)),
                totalAmount = 850.0,
                status = OrderStatus.DELIVERED,
                date = "Oct 15, 2024",
                deliveryAddress = "221B Baker Street, Flat 4A",
                paymentMethod = "UPI (Google Pay)",
                trackingNumber = "GRM987654321",
                canCancel = false
            ),
            Order(
                id = "ORD-9432",
                items = listOf(
                    OrderItem("pr2", "Matte Texture Clay Wax", 2, 450.0),
                    OrderItem("pr3", "Argan Oil Hair Treatment Serum", 1, 650.0)
                ),
                totalAmount = 1550.0,
                status = OrderStatus.SHIPPED,
                date = "Oct 22, 2024",
                deliveryAddress = "221B Baker Street, Flat 4A",
                paymentMethod = "Credit Card (HDFC)",
                trackingNumber = "GRM123456789",
                canCancel = false
            ),
            Order(
                id = "ORD-7612",
                items = listOf(OrderItem("pr4", "Activated Charcoal Face Wash", 1, 350.0)),
                totalAmount = 350.0,
                status = OrderStatus.REFUNDED,
                refundStatus = OrderRefundStatus.REFUNDED,
                cancellationReason = "Ordered duplicate item",
                date = "Sep 18, 2024",
                deliveryAddress = "221B Baker Street, Flat 4A",
                paymentMethod = "UPI",
                canCancel = false
            )
        )
    )

    override fun getOrders(): Flow<List<Order>> = _orders.asStateFlow()

    override suspend fun placeOrder(
        items: List<OrderItem>,
        totalAmount: Double,
        address: String,
        paymentMethod: String
    ): Order? {
        delay(800)
        val newOrder = Order(
            id = "ORD-${(1000..9999).random()}",
            items = items,
            totalAmount = totalAmount,
            status = OrderStatus.PLACED,
            date = "Today",
            deliveryAddress = address,
            paymentMethod = paymentMethod,
            trackingNumber = null, // Courier tracking number not assigned yet
            canCancel = false
        )
        _orders.value = listOf(newOrder) + _orders.value
        return newOrder
    }

    override suspend fun cancelOrder(orderId: String, reason: String): Boolean {
        delay(500)
        val current = _orders.value.toMutableList()
        val index = current.indexOfFirst { it.id == orderId }
        if (index != -1) {
            current[index] = current[index].copy(
                status = OrderStatus.CANCELLED,
                refundStatus = OrderRefundStatus.PROCESSING,
                cancellationReason = reason,
                canCancel = false
            )
            _orders.value = current
            return true
        }
        return false
    }
}
