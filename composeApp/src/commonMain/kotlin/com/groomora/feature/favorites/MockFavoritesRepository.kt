package com.groomora.feature.favorites

import com.groomora.feature.discovery.Shop
import com.groomora.feature.discovery.Professional
import com.groomora.feature.shop.Service
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class MockFavoritesRepository : FavoritesRepository {
    private val _favoriteShops = MutableStateFlow<List<Shop>>(emptyList())
    private val _favoriteProfessionals = MutableStateFlow<List<Professional>>(emptyList())
    private val _favoriteServices = MutableStateFlow<List<Service>>(emptyList())

    override fun getFavoriteShops(): Flow<List<Shop>> = _favoriteShops.asStateFlow()

    override fun getFavoriteProfessionals(): Flow<List<Professional>> = _favoriteProfessionals.asStateFlow()

    override fun getFavoriteServices(): Flow<List<Service>> = _favoriteServices.asStateFlow()

    override suspend fun toggleShopFavorite(shop: Shop) {
        delay(200)
        val current = _favoriteShops.value.toMutableList()
        if (current.any { it.id == shop.id }) {
            current.removeAll { it.id == shop.id }
        } else {
            current.add(shop)
        }
        _favoriteShops.value = current
    }

    override suspend fun toggleProfessionalFavorite(professional: Professional) {
        delay(200)
        val current = _favoriteProfessionals.value.toMutableList()
        if (current.any { it.id == professional.id }) {
            current.removeAll { it.id == professional.id }
        } else {
            current.add(professional)
        }
        _favoriteProfessionals.value = current
    }

    override suspend fun toggleServiceFavorite(service: Service) {
        delay(200)
        val current = _favoriteServices.value.toMutableList()
        if (current.any { it.id == service.id }) {
            current.removeAll { it.id == service.id }
        } else {
            current.add(service)
        }
        _favoriteServices.value = current
    }

    override fun isShopFavorite(shopId: String): Flow<Boolean> = _favoriteShops.map { list ->
        list.any { it.id == shopId }
    }
}
