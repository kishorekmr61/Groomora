package com.groomora.feature.offers

import kotlinx.coroutines.flow.Flow

interface OffersRepository {
    fun getAvailableOffers(): Flow<List<Offer>>
    suspend fun getOfferByCode(code: String): Offer?
    suspend fun validateOffer(code: String, orderValue: Double): Boolean
}
