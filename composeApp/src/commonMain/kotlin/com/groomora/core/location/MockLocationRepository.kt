package com.groomora.core.location

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.asStateFlow

class MockLocationRepository : LocationRepository {
    private val _savedAddresses = MutableStateFlow<List<Address>>(
        listOf(
            Address(
                id = "1",
                label = "Home",
                fullAddress = "123 Premium Ivory Lane, Style City",
                city = "Style City",
                state = "Fashion State",
                country = "Groomland",
                pincode = "123456",
                isDefault = true
            )
        )
    )

    private val mockLocation = UserLocation(
        latitude = 12.9716,
        longitude = 77.5946,
        address = _savedAddresses.value.first(),
        isMock = true
    )

    override fun getLocationUpdates(): Flow<LocationState> = flow {
        emit(LocationState.Loading)
        delay(1000)
        emit(LocationState.Success(mockLocation))
    }

    override suspend fun getCurrentLocation(): LocationState {
        delay(500)
        return LocationState.Success(mockLocation)
    }

    override suspend fun saveAddress(address: Address) {
        val current = _savedAddresses.value.toMutableList()
        current.add(address.copy(id = (current.size + 1).toString()))
        _savedAddresses.value = current
    }

    override fun getSavedAddresses(): Flow<List<Address>> = _savedAddresses.asStateFlow()

    override suspend fun setDefaultAddress(addressId: String) {
        val updated = _savedAddresses.value.map {
            it.copy(isDefault = it.id == addressId)
        }
        _savedAddresses.value = updated
    }
}
