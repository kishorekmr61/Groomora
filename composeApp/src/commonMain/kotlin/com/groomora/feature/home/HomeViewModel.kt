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

    val locationState: StateFlow<LocationState> = locationRepository.getLocationUpdates()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), LocationState.Loading)

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
                            imageUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=800&q=80",
                            title = "Look Good\nFeel Better",
                            description = "Book your perfect style today • FLAT 20% OFF",
                            ctaLabel = "Book Now",
                            deepLink = "groomora://services"
                        ),
                        PromotionBanner(
                            id = "b2",
                            imageUrl = "https://images.unsplash.com/photo-1583939003579-730e3918a45a?w=800&q=80",
                            title = "Luxury Bridal\n& Glow Rituals",
                            description = "Exclusive bridal packages for your special day",
                            ctaLabel = "Explore Bridal",
                            deepLink = "groomora://bridal"
                        ),
                        PromotionBanner(
                            id = "b3",
                            imageUrl = "https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=800&q=80",
                            title = "Relaxing Spa\n& Home Care",
                            description = "Certified salon experts at your doorstep • ₹150 OFF",
                            ctaLabel = "Book At Home",
                            deepLink = "groomora://homeservice"
                        )
                    )
                )
            }.collect { newState ->
                _state.value = newState
            }

        }
    }
}
