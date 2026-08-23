package com.groomora.feature.discovery

import kotlinx.coroutines.flow.Flow

interface DiscoveryRepository {
    fun getShops(): Flow<List<Shop>>
    fun getShopsByCategory(categoryId: String): Flow<List<Shop>>
    fun searchShops(query: String): Flow<List<Shop>>
    suspend fun getShopDetails(shopId: String): Shop?
    suspend fun getProfessionals(shopId: String): List<Professional>
    suspend fun getProfessionalDetail(id: String): ProfessionalDetail?
}
