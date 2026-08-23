package com.groomora.feature.homeservice

import com.groomora.feature.discovery.Professional
import kotlinx.serialization.Serializable

@Serializable
data class HomeServiceDetail(
    val estimatedArrivalTime: String,
    val hygieneProtocols: List<String>,
    val safetyMeasures: List<String>,
    val travelFee: Double,
    val serviceRadiusKm: Double
)

data class HomeServiceState(
    val isLoading: Boolean = false,
    val eligibleProfessionals: List<Professional> = emptyList(),
    val serviceDetails: HomeServiceDetail? = null,
    val error: String? = null
)

sealed interface HomeServiceIntent {
    data class LoadHomeServiceDetails(val addressId: String) : HomeServiceIntent
}
