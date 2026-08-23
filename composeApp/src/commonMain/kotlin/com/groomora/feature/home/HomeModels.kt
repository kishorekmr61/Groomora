package com.groomora.feature.home

import com.groomora.core.configuration.CategoryConfig
import com.groomora.core.location.UserLocation
import kotlinx.serialization.Serializable

@Serializable
data class PromotionBanner(
    val id: String,
    val imageUrl: String,
    val title: String,
    val description: String,
    val ctaLabel: String,
    val deepLink: String
)

data class HomeState(
    val isLoading: Boolean = false,
    val location: UserLocation? = null,
    val categories: List<CategoryConfig> = emptyList(),
    val banners: List<PromotionBanner> = emptyList(),
    val error: String? = null
)

sealed interface HomeIntent {
    data object LoadHomeData : HomeIntent
    data class ChangeLocation(val pincode: String) : HomeIntent
}
