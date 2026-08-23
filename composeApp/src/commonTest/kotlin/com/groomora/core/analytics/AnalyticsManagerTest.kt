package com.groomora.core.analytics

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AnalyticsManagerTest {

    private val analyticsManager = DefaultAnalyticsManager()

    @Test
    fun testEventLoggingAndPiiStripping() {
        analyticsManager.logEvent(
            "user_signup",
            mapOf(
                "method" to "phone_otp",
                "password" to "secret123",
                "auth_token" to "xyz_token_secret",
                "card_number" to "4111222233334444",
                "city" to "Bangalore"
            )
        )

        val lastEvent = analyticsManager.eventHistory.value.lastOrNull()
        assertTrue(lastEvent != null)
        assertEquals("user_signup", lastEvent.name)
        assertFalse(lastEvent.params.containsKey("password"))
        assertFalse(lastEvent.params.containsKey("auth_token"))
        assertFalse(lastEvent.params.containsKey("card_number"))
        assertEquals("Bangalore", lastEvent.params["city"])
    }

    @Test
    fun testFunnelStepTracking() {
        analyticsManager.logFunnelStep("booking_flow", 1, "service_selected")
        analyticsManager.logFunnelStep("booking_flow", 2, "slot_selected")
        analyticsManager.logFunnelStep("booking_flow", 3, "payment_confirmed")

        val events = analyticsManager.eventHistory.value.filter { it.name == "funnel_step" }
        assertEquals(3, events.size)
        assertEquals("booking_flow", events[0].params["funnel_name"])
        assertEquals("1", events[0].params["step_number"])
        assertEquals("service_selected", events[0].params["step_name"])
    }
}
