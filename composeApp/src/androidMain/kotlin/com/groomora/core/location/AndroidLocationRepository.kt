package com.groomora.core.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*

class AndroidLocationRepository(private val context: Context) : LocationRepository {
    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    
    private val _locationState = MutableStateFlow<LocationState>(LocationState.Loading)
    
    override fun getLocationUpdates(): Flow<LocationState> = _locationState.asStateFlow()

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LocationState {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            val state = LocationState.PermissionDenied
            _locationState.value = state
            return state
        }

        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        val isGpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
        val isNetworkEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true

        if (!isGpsEnabled && !isNetworkEnabled) {
            val state = LocationState.Error("Location services are disabled")
            _locationState.value = state
            return state
        }

        try {
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await()

            if (location != null) {
                val address = reverseGeocode(location.latitude, location.longitude)
                val userLocation = UserLocation(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    address = address
                )
                val state = LocationState.Success(userLocation)
                _locationState.value = state
                return state
            }
        } catch (e: Exception) {
            return LocationState.Error(e.message ?: "Failed to get location")
        }
        return LocationState.Error("Location is null")
    }

    private fun reverseGeocode(lat: Double, lng: Double): Address? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                // For modern devices, we should technically use the listener version, 
                // but this is a suspend function context anyway. 
                // We'll keep it simple and use the synchronous one for now as it's still available.
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                processAddress(addresses)
            } else {
                @Suppress("DEPRECATION")
                val addresses = geocoder.getFromLocation(lat, lng, 1)
                processAddress(addresses)
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun processAddress(addresses: List<android.location.Address>?): Address? {
        if (!addresses.isNullOrEmpty()) {
            val addr = addresses[0]
            return Address(
                id = "current",
                label = "Current Location",
                fullAddress = addr.getAddressLine(0) ?: "",
                city = addr.locality ?: addr.subAdminArea ?: "Unknown",
                state = addr.adminArea ?: "",
                country = addr.countryName ?: "",
                pincode = addr.postalCode ?: ""
            )
        }
        return null
    }

    override suspend fun saveAddress(address: Address) { /* Mock implementation */ }
    override fun getSavedAddresses(): Flow<List<Address>> = flowOf(emptyList())
    override suspend fun setDefaultAddress(addressId: String) { /* Mock implementation */ }
}
