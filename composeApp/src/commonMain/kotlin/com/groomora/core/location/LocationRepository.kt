package com.groomora.core.location

import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun getLocationUpdates(): Flow<LocationState>
    suspend fun getCurrentLocation(): LocationState
    suspend fun saveAddress(address: Address)
    fun getSavedAddresses(): Flow<List<Address>>
    suspend fun setDefaultAddress(addressId: String)
}

expect fun createLocationRepository(): LocationRepository
