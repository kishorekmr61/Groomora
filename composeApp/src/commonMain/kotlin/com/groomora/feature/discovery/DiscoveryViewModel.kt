package com.groomora.feature.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DiscoveryViewModel(
    private val discoveryRepository: DiscoveryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DiscoveryState())
    val state: StateFlow<DiscoveryState> = _state.asStateFlow()

    init {
        onIntent(DiscoveryIntent.LoadDiscoveryData)
    }

    fun onIntent(intent: DiscoveryIntent) {
        when (intent) {
            DiscoveryIntent.LoadDiscoveryData -> loadShops()
            is DiscoveryIntent.Search -> {
                _state.update { it.copy(searchQuery = intent.query) }
                searchShops(intent.query)
            }
            is DiscoveryIntent.FilterByCategory -> {
                _state.update { it.copy(selectedCategory = intent.categoryId) }
                if (intent.categoryId == null) {
                    loadShops()
                } else {
                    loadShopsByCategory(intent.categoryId)
                }
            }
        }
    }

    private fun loadShops() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            discoveryRepository.getShops().collect { shops ->
                _state.update { it.copy(isLoading = false, shops = shops) }
            }
        }
    }

    private fun loadShopsByCategory(categoryId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            discoveryRepository.getShopsByCategory(categoryId).collect { shops ->
                _state.update { it.copy(isLoading = false, shops = shops) }
            }
        }
    }

    private fun searchShops(query: String) {
        viewModelScope.launch {
            discoveryRepository.searchShops(query).collect { shops ->
                _state.update { it.copy(shops = shops) }
            }
        }
    }
}
