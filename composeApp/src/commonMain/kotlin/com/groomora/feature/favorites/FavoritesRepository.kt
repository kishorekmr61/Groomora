package com.groomora.feature.favorites

import com.groomora.feature.discovery.Shop
import com.groomora.feature.discovery.Professional
import com.groomora.feature.shop.Service
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavoriteShops(): Flow<List<Shop>>
    fun getFavoriteProfessionals(): Flow<List<Professional>>
    fun getFavoriteServices(): Flow<List<Service>>
    suspend fun toggleShopFavorite(shop: Shop)
    suspend fun toggleProfessionalFavorite(professional: Professional)
    suspend fun toggleServiceFavorite(service: Service)
    fun isShopFavorite(shopId: String): Flow<Boolean>
}
