package com.groomora.feature.products

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockOrderRepository : OrderRepository {
    private val _orders = MutableStateFlow(
        listOf(
            Order(
                id = "ord_101",
                items = listOf(OrderItem("pr1", "Premium Beard Oil", 1, 850.0)),
                totalAmount = 850.0,
                status = OrderStatus.DELIVERED,
                date = "Oct 15, 2024",
                deliveryAddress = "123 Premium Ivory Lane, Style City",
                trackingNumber = "GRM987654321"
            ),
            Order(
                id = "ord_102",
                items = listOf(OrderItem("pr2", "Matte Clay Wax", 2, 450.0)),
                totalAmount = 900.0,
                status = OrderStatus.SHIPPED,
                date = "Oct 22, 2024",
                deliveryAddress = "123 Premium Ivory Lane, Style City",
                trackingNumber = "GRM123456789"
            )
        )
    )

    override fun getOrders(): Flow<List<Order>> = _orders.asStateFlow()

    override suspend fun placeOrder(items: List<OrderItem>, totalAmount: Double, address: String): Order? {
        delay(2000)
        val newOrder = Order(
            id = "ord_new_${(1000..9999).random()}",
            items = items,
            totalAmount = totalAmount,
            status = OrderStatus.PLACED,
            date = "Today",
            deliveryAddress = address
        )
        val current = _orders.value.toMutableList()
        current.add(0, newOrder)
        _orders.value = current
        return newOrder
    }

    override suspend fun cancelOrder(orderId: String): Boolean {
        delay(1000)
        val current = _orders.value.toMutableList()
        val index = current.indexOfFirst { it.id == orderId }
        if (index != -1) {
            current[index] = current[index].copy(status = OrderStatus.CANCELLED)
            _orders.value = current
            return true
        }
        return false
    }
}
