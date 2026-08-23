package com.groomora.core.geo

import com.groomora.core.location.Address
import com.groomora.core.location.UserLocation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GeoRulesEngineTest {

    private val engine = GeoRulesEngine()

    @Test
    fun testEligibleLocationPincode() {
        val loc = UserLocation(
            latitude = 12.9716,
            longitude = 77.5946,
            address = Address(
                id = "addr1",
                label = "Home",
                fullAddress = "MG Road, Bangalore",
                city = "Bangalore",
                state = "Karnataka",
                country = "India",
                pincode = "110001"
            )
        )
        val availability = engine.calculateAvailability(loc)
        assertTrue(availability.isAvailable)
        assertEquals(50.0, availability.travelFee)
    }

    @Test
    fun testIneligibleLocationPincode() {
        val loc = UserLocation(
            latitude = 12.9716,
            longitude = 77.5946,
            address = Address(
                id = "addr2",
                label = "Office",
                fullAddress = "Remote Road, Remote City",
                city = "Remote City",
                state = "State",
                country = "India",
                pincode = "990001"
            )
        )
        val availability = engine.calculateAvailability(loc)
        assertFalse(availability.isAvailable)
        assertEquals(0.0, availability.travelFee)
    }
}
