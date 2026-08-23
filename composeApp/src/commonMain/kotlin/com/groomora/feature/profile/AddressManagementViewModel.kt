package com.groomora.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.groomora.core.location.Address
import com.groomora.core.location.LocationRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class AddressState(
    val isLoading: Boolean = false,
    val addresses: List<Address> = emptyList(),
    val error: String? = null
)

sealed interface AddressIntent {
    data object LoadAddresses : AddressIntent
    data class DeleteAddress(val addressId: String) : AddressIntent
    data class SetDefault(val addressId: String) : AddressIntent
    data class AddAddress(val address: Address) : AddressIntent
}

class AddressManagementViewModel(
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddressState())
    val state: StateFlow<AddressState> = _state.asStateFlow()

    init {
        loadAddresses()
    }

    fun onIntent(intent: AddressIntent) {
        when (intent) {
            AddressIntent.LoadAddresses -> loadAddresses()
            is AddressIntent.DeleteAddress -> { /* Mock delete */ }
            is AddressIntent.SetDefault -> {
                viewModelScope.launch {
                    locationRepository.setDefaultAddress(intent.addressId)
                }
            }
            is AddressIntent.AddAddress -> {
                viewModelScope.launch {
                    locationRepository.saveAddress(intent.address)
                }
            }
        }
    }

    private fun loadAddresses() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            locationRepository.getSavedAddresses().collect { addresses ->
                _state.update { it.copy(isLoading = false, addresses = addresses) }
            }
        }
    }
}
