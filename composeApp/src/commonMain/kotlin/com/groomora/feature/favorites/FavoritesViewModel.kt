package com.groomora.feature.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesState())
    val state: StateFlow<FavoritesState> = _state.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            combine(
                favoritesRepository.getFavoriteShops(),
                favoritesRepository.getFavoriteProfessionals(),
                favoritesRepository.getFavoriteServices()
            ) { shops, professionals, services ->
                FavoritesState(
                    isLoading = false,
                    favoriteShops = shops,
                    favoriteProfessionals = professionals,
                    favoriteServices = services
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun onIntent(intent: FavoritesIntent) {
        when (intent) {
            FavoritesIntent.LoadFavorites -> loadFavorites()
            is FavoritesIntent.ToggleShopFavorite -> {
                // In a real app we'd pass the whole object, but for mock we just handle it
            }
            is FavoritesIntent.ToggleProfessionalFavorite -> {
            }
        }
    }
}
