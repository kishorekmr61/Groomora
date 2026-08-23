package com.groomora.feature.offers

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockOffersRepository : OffersRepository {
    private val mockOffers = listOf(
        Offer(
            id = "o1",
            code = "WELCOME20",
            title = "Welcome Offer",
            description = "Get 20% off on your first booking.",
            discountValue = 20.0,
            type = OfferType.FIRST_BOOKING,
            expiryDate = "Dec 31, 2024",
            maxDiscount = 200.0
        ),
        Offer(
            id = "o2",
            code = "FESTIVE500",
            title = "Festive Special",
            description = "Flat ₹500 off on services above ₹2000.",
            discountValue = 500.0,
            type = OfferType.FLAT,
            expiryDate = "Nov 15, 2024",
            minOrderValue = 2000.0
        )
    )

    override fun getAvailableOffers(): Flow<List<Offer>> = flow {
        delay(500)
        emit(mockOffers)
    }

    override suspend fun getOfferByCode(code: String): Offer? {
        delay(300)
        return mockOffers.find { it.code == code }
    }

    override suspend fun validateOffer(code: String, orderValue: Double): Boolean {
        delay(300)
        val offer = mockOffers.find { it.code == code } ?: return false
        return orderValue >= offer.minOrderValue
    }
}
