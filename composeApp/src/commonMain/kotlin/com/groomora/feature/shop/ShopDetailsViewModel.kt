package com.groomora.feature.shop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ShopDetailsViewModel(
    private val shopDetailsRepository: ShopDetailsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ShopDetailsState())
    val state: StateFlow<ShopDetailsState> = _state.asStateFlow()

    fun onIntent(intent: ShopDetailsIntent) {
        when (intent) {
            is ShopDetailsIntent.LoadDetails -> loadShopDetails(intent.shopId)
        }
    }

    private fun loadShopDetails(shopId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            combine(
                shopDetailsRepository.getShop(shopId),
                shopDetailsRepository.getServices(shopId),
                shopDetailsRepository.getPackages(shopId),
                shopDetailsRepository.getProfessionals(shopId)
            ) { shop, services, packages, professionals ->
                ShopDetailsState(
                    isLoading = false,
                    shop = shop,
                    services = services,
                    packages = packages,
                    professionals = professionals
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }
}
