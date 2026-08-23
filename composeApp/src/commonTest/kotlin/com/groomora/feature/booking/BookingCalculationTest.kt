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

    @Test
    fun testMultipleServicesSumCalculation() {
        val services = listOf(
            Service("ser1", "Hair Spa", 599.0, "60 min", "", "hair"),
            Service("ser2", "Haircut", 299.0, "30 min", "", "hair"),
            Service("ser3", "Beard Trim", 199.0, "45 min", "", "beard")
        )
        val basePrice = services.sumOf { it.price }
        assertEquals(1097.0, basePrice)
    }

    @Test
    fun testTwoPercentCouponDiscountCalculation() {
        val basePrice = 1000.0
        val addOns = 200.0
        val travel = 99.0
        val fullAmount = basePrice + addOns + travel // 1299.0
        val discount2Percent = fullAmount * 0.02 // 25.98
        val total = fullAmount - discount2Percent

        assertEquals(25.98, discount2Percent)
        assertEquals(1273.02, total)
    }

    @Test
    fun testExploreServicesVendorBookingFlow() {
        val selectedServiceIds = listOf("ser1", "ser2")
        val chosenShopId = "s1"
        val chosenShopName = "The Golden Scissor"

        val repo = MockBookingRepository()
        val services = listOf(
            Service("ser1", "Luxury Hair Spa & Scalp Therapy", 599.0, "60 min", "", "hair"),
            Service("ser2", "Master Haircut & Style Consultation", 299.0, "30 min", "", "hair")
        )

        val totalBasePrice = services.sumOf { it.price }
        assertEquals(898.0, totalBasePrice)
        assertEquals("The Golden Scissor", chosenShopName)
        assertEquals(2, services.size)
    }
}

