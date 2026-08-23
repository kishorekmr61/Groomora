package com.groomora.feature.offers

import kotlinx.serialization.Serializable

@Serializable
data class Offer(
    val id: String,
    val code: String,
    val title: String,
    val description: String,
    val discountValue: Double,
    val type: OfferType,
    val expiryDate: String,
    val minOrderValue: Double = 0.0,
    val maxDiscount: Double? = null
)

enum class OfferType {
    PERCENTAGE, FLAT, SERVICE_SPECIFIC, FIRST_BOOKING
}

data class OffersState(
    val isLoading: Boolean = false,
    val availableOffers: List<Offer> = emptyList(),
    val error: String? = null
)
