package com.groomora.feature.favorites

import com.groomora.feature.discovery.Shop
import com.groomora.feature.discovery.Professional
import com.groomora.feature.shop.Service
import kotlinx.serialization.Serializable

data class FavoritesState(
    val isLoading: Boolean = false,
    val favoriteShops: List<Shop> = emptyList(),
    val favoriteProfessionals: List<Professional> = emptyList(),
    val favoriteServices: List<Service> = emptyList(),
    val error: String? = null
)

sealed interface FavoritesIntent {
    data object LoadFavorites : FavoritesIntent
    data class ToggleShopFavorite(val shopId: String) : FavoritesIntent
    data class ToggleProfessionalFavorite(val professionalId: String) : FavoritesIntent
}
