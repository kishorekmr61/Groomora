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
        viewModelScope.launch {
            productRepository.cartItems.collect { cart ->
                _state.update { it.copy(cartItems = cart) }
            }
        }
    }

    fun onIntent(intent: ProductIntent) {
        when (intent) {
            ProductIntent.LoadProducts -> loadProducts()
            is ProductIntent.AddToCart -> {
                productRepository.addToCart(intent.productId)
            }
            is ProductIntent.RemoveFromCart -> {
                productRepository.removeFromCart(intent.productId)
            }
            ProductIntent.ClearCart -> {
                productRepository.clearCart()
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
