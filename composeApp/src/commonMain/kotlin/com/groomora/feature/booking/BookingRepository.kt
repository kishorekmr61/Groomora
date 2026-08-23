package com.groomora.feature.booking

import com.groomora.feature.shop.Service
import com.groomora.feature.discovery.Professional
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    fun getService(serviceId: String): Flow<Service?>
    fun getAvailability(shopId: String, professionalId: String?): Flow<List<BookingAvailability>>
    suspend fun bookSlot(
        serviceId: String,
        professionalId: String?,
        date: String,
        time: String,
        isHomeService: Boolean
    ): Boolean
}
