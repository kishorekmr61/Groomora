package com.groomora.feature.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductState())
    val state: StateFlow<ProductState> = _state.asStateFlow()

    init {
        onIntent(ProductIntent.LoadProducts)
    }

    fun onIntent(intent: ProductIntent) {
        when (intent) {
            ProductIntent.LoadProducts -> loadProducts()
            is ProductIntent.AddToCart -> {
                val currentCart = _state.value.cartItems.toMutableMap()
                val count = currentCart[intent.productId] ?: 0
                currentCart[intent.productId] = count + 1
                _state.update { it.copy(cartItems = currentCart) }
            }
            is ProductIntent.RemoveFromCart -> {
                val currentCart = _state.value.cartItems.toMutableMap()
                val count = currentCart[intent.productId] ?: 0
                if (count > 1) {
                    currentCart[intent.productId] = count - 1
                } else {
                    currentCart.remove(intent.productId)
                }
                _state.update { it.copy(cartItems = currentCart) }
            }
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            productRepository.getProducts().collect { products ->
                _state.update { it.copy(isLoading = false, products = products) }
            }
        }
    }
}
