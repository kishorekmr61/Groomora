package com.groomora.core.geo

import com.groomora.core.location.UserLocation

class GeoRulesEngine {
    fun calculateAvailability(
        location: UserLocation,
        shopLocation: UserLocation? = null
    ): ServiceAvailability {
        // Mock logic: Always available if pincode starts with '1'
        val isAvailable = location.address?.pincode?.startsWith("1") ?: true
        
        return ServiceAvailability(
            isAvailable = isAvailable,
            travelFee = if (isAvailable) 50.0 else 0.0,
            minOrderValue = 300.0,
            message = if (isAvailable) null else "Service not available in your area yet."
        )
    }

    fun getZoneForLocation(location: UserLocation): GeoZone {
        return GeoZone(
            id = "zone_1",
            name = "Premium Central",
            type = ZoneType.ZONE,
            radiusKm = 10.0
        )
    }
}
