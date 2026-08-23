package com.groomora.feature.loyalty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoyaltyViewModel(
    private val loyaltyRepository: LoyaltyRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoyaltyState())
    val state: StateFlow<LoyaltyState> = _state.asStateFlow()

    init {
        loadLoyaltyData()
    }

    private fun loadLoyaltyData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            combine(
                loyaltyRepository.getLoyaltyProfile(),
                loyaltyRepository.getTransactions(),
                loyaltyRepository.getMembershipPlans()
            ) { profile, transactions, plans ->
                LoyaltyState(
                    isLoading = false,
                    profile = profile,
                    transactions = transactions,
                    membershipPlans = plans
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun redeemPoints(points: Int) {
        viewModelScope.launch {
            val success = loyaltyRepository.redeemPoints(points)
            if (success) loadLoyaltyData()
        }
    }
}
