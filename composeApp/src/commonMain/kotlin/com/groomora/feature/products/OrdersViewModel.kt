package com.groomora.feature.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OrdersState())
    val state: StateFlow<OrdersState> = _state.asStateFlow()

    init {
        onIntent(OrdersIntent.LoadOrders)
    }

    fun onIntent(intent: OrdersIntent) {
        when (intent) {
            OrdersIntent.LoadOrders -> loadOrders()
            is OrdersIntent.CancelOrder -> cancelOrder(intent.orderId, intent.reason)
        }
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            orderRepository.getOrders().collect { orders ->
                _state.update { it.copy(isLoading = false, orders = orders) }
            }
        }
    }

    private fun cancelOrder(orderId: String, reason: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val success = orderRepository.cancelOrder(orderId, reason)
            if (success) {
                _state.update { it.copy(message = "Order $orderId cancelled. Refund processing initiated.") }
                loadOrders()
            }
        }
    }
}
