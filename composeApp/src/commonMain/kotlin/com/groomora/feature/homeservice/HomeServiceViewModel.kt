package com.groomora.feature.homeservice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeServiceViewModel(
    private val homeServiceRepository: HomeServiceRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeServiceState())
    val state: StateFlow<HomeServiceState> = _state.asStateFlow()

    fun onIntent(intent: HomeServiceIntent) {
        when (intent) {
            is HomeServiceIntent.LoadHomeServiceDetails -> loadDetails(intent.addressId)
        }
    }

    private fun loadDetails(addressId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            homeServiceRepository.getHomeServiceDetails(addressId).collect { details ->
                _state.update { it.copy(isLoading = false, serviceDetails = details) }
            }
        }
    }
}
