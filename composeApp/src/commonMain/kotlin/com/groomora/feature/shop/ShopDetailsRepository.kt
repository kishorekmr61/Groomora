package com.groomora.feature.shop

import com.groomora.feature.discovery.Professional
import com.groomora.feature.discovery.Shop
import kotlinx.coroutines.flow.Flow

interface ShopDetailsRepository {
    fun getShop(shopId: String): Flow<Shop?>
    fun getServices(shopId: String): Flow<List<Service>>
    fun getPackages(shopId: String): Flow<List<ServicePackage>>
    fun getProfessionals(shopId: String): Flow<List<Professional>>
}
