package com.groomora.feature.beauty

import kotlinx.serialization.Serializable

@Serializable
data class BeautyCategory(
    val id: String,
    val name: String,
    val iconName: String,
    val description: String
)

@Serializable
data class BeautyService(
    val id: String,
    val categoryId: String,
    val name: String,
    val description: String,
    val price: Double,
    val duration: String,
    val benefits: List<String> = emptyList(),
    val isPopular: Boolean = false
)

@Serializable
data class BeautyPackage(
    val id: String,
    val name: String,
    val description: String,
    val includedServices: List<String>,
    val price: Double,
    val originalPrice: Double,
    val savingsPercent: Int,
    val duration: String
)

data class BeautyState(
    val isLoading: Boolean = false,
    val categories: List<BeautyCategory> = emptyList(),
    val selectedCategoryId: String = "facial",
    val services: List<BeautyService> = emptyList(),
    val packages: List<BeautyPackage> = emptyList(),
    val error: String? = null
)

sealed interface BeautyIntent {
    data object LoadBeautyData : BeautyIntent
    data class SelectCategory(val categoryId: String) : BeautyIntent
}
