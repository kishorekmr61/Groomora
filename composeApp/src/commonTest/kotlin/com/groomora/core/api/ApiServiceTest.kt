package com.groomora.core.api

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ApiServiceTest {

    @Test
    fun testApiEndpointsConfiguration() {
        assertEquals("https://api.groomora.com", ApiEndpoints.BASE_URL)
        assertEquals("/api/v1/auth/send-otp", ApiEndpoints.Auth.SEND_OTP)
        assertEquals("/api/v1/bookings/create", ApiEndpoints.Booking.CREATE_BOOKING)
        assertEquals("/api/v1/offers/validate-coupon", ApiEndpoints.Offers.VALIDATE_COUPON)
        assertEquals("/api/v1/products", ApiEndpoints.Products.CATALOG)
    }

    @Test
    fun testAuthApiService() = runBlocking {
        val authApi = MockAuthApiService()

        // 1. Send OTP
        val otpRes = authApi.sendOtp(SendOtpRequest(phoneNumber = "9876543210"))
        assertTrue(otpRes.success)
        assertEquals("1234", otpRes.data)

        // 2. Verify OTP
        val verifyRes = authApi.verifyOtp(VerifyOtpRequest(phoneNumber = "9876543210", otp = "1234"))
        assertTrue(verifyRes.success)
        assertNotNull(verifyRes.data?.token)
        assertEquals("9876543210", verifyRes.data?.phoneNumber)
    }

    @Test
    fun testBookingApiService() = runBlocking {
        val bookingApi = MockBookingApiService()

        // 1. Time slots
        val slots = bookingApi.getAvailableTimeSlots("Oct 25, 2024")
        assertTrue(slots.success)
        assertTrue(slots.data!!.isNotEmpty())

        // 2. Create Booking
        val bookingRes = bookingApi.createBooking(
            CreateBookingRequest(
                serviceId = "ser1",
                date = "Oct 28, 2024",
                timeSlot = "11:00 AM"
            )
        )
        assertTrue(bookingRes.success)
        assertNotNull(bookingRes.data?.id)
        assertEquals("Oct 28, 2024", bookingRes.data?.date)

        // 3. Reschedule
        val reschedRes = bookingApi.rescheduleBooking(
            RescheduleBookingRequest(
                bookingId = bookingRes.data!!.id,
                newDate = "Oct 30, 2024",
                newTimeSlot = "03:00 PM"
            )
        )
        assertTrue(reschedRes.success)
        assertEquals("Oct 30, 2024", reschedRes.data?.date)

        // 4. Cancel
        val cancelRes = bookingApi.cancelBooking(
            CancelBookingRequest(
                bookingId = bookingRes.data!!.id,
                reason = "Change of schedule"
            )
        )
        assertTrue(cancelRes.success)
    }

    @Test
    fun testOffersApiServiceValidation() = runBlocking {
        val offersApi = MockOffersApiService()

        // Valid coupon
        val validRes = offersApi.validateCoupon(ValidateCouponRequest(couponCode = "FIRST50", cartAmount = 400.0))
        assertTrue(validRes.success)
        assertTrue(validRes.data!!.isValid)
        assertEquals(200.0, validRes.data?.discountAmount)
        assertEquals(200.0, validRes.data?.finalAmount)

        // Invalid coupon
        val invalidRes = offersApi.validateCoupon(ValidateCouponRequest(couponCode = "INVALID_CODE", cartAmount = 400.0))
        assertFalse(invalidRes.success)
        assertFalse(invalidRes.data!!.isValid)
        assertEquals(0.0, invalidRes.data?.discountAmount)
    }

    @Test
    fun testLoyaltyApiService() = runBlocking {
        val loyaltyApi = MockLoyaltyApiService()

        val summary = loyaltyApi.getLoyaltyProfile()
        assertTrue(summary.success)
        val initialBalance = summary.data!!.pointsBalance

        val redeemRes = loyaltyApi.redeemPoints(100)
        assertTrue(redeemRes.success)
        assertEquals(initialBalance - 100, redeemRes.data?.pointsBalance)

    }
}
