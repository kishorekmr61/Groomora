package com.groomora.feature.offers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OffersViewModel(
    private val offersRepository: OffersRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OffersState())
    val state: StateFlow<OffersState> = _state.asStateFlow()

    init {
        loadOffers()
    }

    private fun loadOffers() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            offersRepository.getAvailableOffers().collect { offers ->
                _state.update { it.copy(isLoading = false, availableOffers = offers) }
            }
        }
    }
}
