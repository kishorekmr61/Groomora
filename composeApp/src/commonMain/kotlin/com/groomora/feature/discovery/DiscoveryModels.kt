package com.groomora.feature.discovery

import kotlinx.serialization.Serializable

@Serializable
data class Shop(
    val id: String,
    val name: String,
    val rating: Double,
    val reviewCount: Int,
    val address: String,
    val distance: String,
    val imageUrl: String,
    val isVerified: Boolean = false,
    val categories: List<String> = emptyList(),
    val tags: List<String> = emptyList()
)

@Serializable
data class Professional(
    val id: String,
    val name: String,
    val role: String,
    val rating: Double,
    val imageUrl: String,
    val specialization: List<String> = emptyList()
)

data class DiscoveryState(
    val isLoading: Boolean = false,
    val shops: List<Shop> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val error: String? = null
)

sealed interface DiscoveryIntent {
    data object LoadDiscoveryData : DiscoveryIntent
    data class Search(val query: String) : DiscoveryIntent
    data class FilterByCategory(val categoryId: String?) : DiscoveryIntent
}
