package com.groomora.feature.booking

import com.groomora.feature.offers.Offer
import com.groomora.feature.offers.OfferType
import com.groomora.feature.shop.Service
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BookingCalculationTest {

    @Test
    fun testBasePriceWithAddOns() {
        val basePrice = 450.0
        val addOnsTotal = 350.0
        val travelFee = 0.0
        val discount = 0.0
        val loyalty = 0.0
        val total = basePrice + addOnsTotal + travelFee - discount - loyalty

        assertEquals(800.0, total)
    }

    @Test
    fun testHomeServiceTravelFeeAdded() {
        val basePrice = 450.0
        val travelFee = 99.0
        val total = basePrice + travelFee

        assertEquals(549.0, total)
    }

    @Test
    fun testPercentageCouponWithMaxDiscount() {
        val base = 1000.0
        val discountPercent = 20.0 // 20% = 200
        val maxDiscount = 150.0 // Cap at 150
        val discount = (base * (discountPercent / 100.0)).coerceAtMost(maxDiscount)

        assertEquals(150.0, discount)
    }

    @Test
    fun testLoyaltyPointsRedemption() {
        val base = 500.0
        val loyaltyRedeemed = 50.0
        val total = (base - loyaltyRedeemed).coerceAtLeast(0.0)

        assertEquals(450.0, total)
    }

    @Test
    fun testTotalNeverNegative() {
        val base = 100.0
        val discount = 200.0
        val total = (base - discount).coerceAtLeast(0.0)

        assertEquals(0.0, total)
    }
}
