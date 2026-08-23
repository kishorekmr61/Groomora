package com.groomora.feature.shop

import com.groomora.feature.discovery.Professional
import com.groomora.feature.discovery.Shop
import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val id: String,
    val name: String,
    val price: Double,
    val duration: String,
    val description: String? = null,
    val category: String
)

@Serializable
data class ServicePackage(
    val id: String,
    val name: String,
    val price: Double,
    val originalPrice: Double,
    val services: List<String>,
    val description: String
)

data class ShopDetailsState(
    val isLoading: Boolean = false,
    val shop: Shop? = null,
    val services: List<Service> = emptyList(),
    val packages: List<ServicePackage> = emptyList(),
    val professionals: List<Professional> = emptyList(),
    val error: String? = null
)

sealed interface ShopDetailsIntent {
    data class LoadDetails(val shopId: String) : ShopDetailsIntent
}
