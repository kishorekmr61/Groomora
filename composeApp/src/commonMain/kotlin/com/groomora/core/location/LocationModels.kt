package com.groomora.core.location

import kotlinx.serialization.Serializable

@Serializable
data class UserLocation(
    val latitude: Double,
    val longitude: Double,
    val address: Address? = null,
    val isMock: Boolean = false
)

@Serializable
data class Address(
    val id: String? = null,
    val label: String? = null, // e.g., "Home", "Office"
    val fullAddress: String,
    val city: String,
    val state: String,
    val country: String,
    val pincode: String,
    val isDefault: Boolean = false
)

sealed interface LocationState {
    data object Loading : LocationState
    data class Success(val location: UserLocation) : LocationState
    data class Error(val message: String) : LocationState
    data object PermissionDenied : LocationState
}
