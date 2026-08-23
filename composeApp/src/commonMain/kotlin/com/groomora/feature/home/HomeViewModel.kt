package com.groomora.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.groomora.core.configuration.ConfigRepository
import com.groomora.core.location.LocationRepository
import com.groomora.core.location.LocationState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val configRepository: ConfigRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        onIntent(HomeIntent.LoadHomeData)
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadHomeData -> loadData()
            is HomeIntent.ChangeLocation -> { /* TODO: Manual location change */ }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            // Combine config and location flows
            combine(
                configRepository.config,
                locationRepository.getLocationUpdates()
            ) { config, locationState ->
                val location = if (locationState is LocationState.Success) locationState.location else null
                
                HomeState(
                    isLoading = false,
                    location = location,
                    categories = config.categories,
                    banners = listOf(
                        PromotionBanner(
                            id = "b1",
                            imageUrl = "",
                            title = "First Booking Offer",
                            description = "Get 20% off on your first salon visit.",
                            ctaLabel = "Book Now",
                            deepLink = "groomora://offers/first"
                        )
                    )
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }
}
