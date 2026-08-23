package com.groomora.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data class Discovery(val categoryId: String? = null) : Screen

    @Serializable
    data class ShopDetails(val shopId: String) : Screen

    @Serializable
    data class ProfessionalProfile(val professionalId: String) : Screen

    @Serializable
    data class Booking(val serviceId: String? = null, val packageId: String? = null) : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data object Favorites : Screen

    @Serializable
    data object Offers : Screen

    @Serializable
    data object Loyalty : Screen

    @Serializable
    data object Products : Screen

    @Serializable
    data class ProductDetails(val productId: String) : Screen

    @Serializable
    data object Cart : Screen

    @Serializable
    data object Bridal : Screen

    @Serializable
    data object Orders : Screen

    @Serializable
    data object Notifications : Screen

    @Serializable
    data object HomeService : Screen

    @Serializable
    data class Reviews(val targetId: String, val type: String) : Screen

    @Serializable
    data object Login : Screen

    @Serializable
    data object AddressManagement : Screen

    @Serializable
    data object Onboarding : Screen
}
