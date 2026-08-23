package com.groomora.feature.homeservice

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockHomeServiceRepository : HomeServiceRepository {
    override fun getHomeServiceDetails(addressId: String): Flow<HomeServiceDetail?> = flow {
        delay(600)
        emit(
            HomeServiceDetail(
                estimatedArrivalTime = "45-60 mins",
                hygieneProtocols = listOf(
                    "Sanitized tools and kits",
                    "Mandatory masks and gloves",
                    "Single-use disposables"
                ),
                safetyMeasures = listOf(
                    "Background verified professionals",
                    "Daily temperature checks",
                    "Contactless service option"
                ),
                travelFee = 99.0,
                serviceRadiusKm = 15.0
            )
        )
    }
}
