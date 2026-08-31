package com.groomora.core.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Splash : Screen

    @Serializable
    data object Home : Screen

    @Serializable
    data class Discovery(val categoryId: String? = null, val serviceIds: List<String> = emptyList()) : Screen

    @Serializable
    data class Services(val initialCategory: String? = null) : Screen


    @Serializable
    data class ShopDetails(val shopId: String) : Screen

    @Serializable
    data class ProfessionalProfile(val professionalId: String) : Screen

    @Serializable
    data class Booking(
        val serviceId: String? = null,
        val packageId: String? = null,
        val serviceIds: List<String> = emptyList(),
        val shopId: String? = null
    ) : Screen

    @Serializable
    data object Profile : Screen

    @Serializable
    data object EditProfile : Screen

    @Serializable
    data object Settings : Screen


    @Serializable
    data object Favorites : Screen

    @Serializable
    data object Offers : Screen

    @Serializable
    data object Loyalty : Screen

    @Serializable
    data object ProductCheckout : Screen

    @Serializable
    data class Payment(val planId: String, val amount: Int, val address: String? = null) : Screen

    @Serializable
    data object PaymentSuccess : Screen

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
    data object SignUp : Screen

    @Serializable
    data object AddressManagement : Screen


    @Serializable
    data object Onboarding : Screen

    @Serializable
    data object Support : Screen

    @Serializable
    data object Beauty : Screen

    @Serializable
    data object BookingHistory : Screen

    @Serializable
    data object GenderSelection : Screen

    @Serializable
    data class GenderServices(val gender: String) : Screen
}


