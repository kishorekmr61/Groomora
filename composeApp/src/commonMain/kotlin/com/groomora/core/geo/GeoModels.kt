package com.groomora.core.geo

import kotlinx.serialization.Serializable

@Serializable
data class GeoZone(
    val id: String,
    val name: String,
    val type: ZoneType,
    val parentId: String? = null,
    val centerLat: Double? = null,
    val centerLng: Double? = null,
    val radiusKm: Double? = null
)

enum class ZoneType {
    COUNTRY, STATE, CITY, ZONE, PINCODE
}

@Serializable
data class ServiceAvailability(
    val isAvailable: Boolean,
    val travelFee: Double = 0.0,
    val minOrderValue: Double = 0.0,
    val message: String? = null
)
