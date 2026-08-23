package com.groomora.feature.booking

import com.groomora.feature.shop.Service
import com.groomora.feature.discovery.Professional
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockBookingRepository : BookingRepository {
    private val mockServices = listOf(
        Service("ser1", "Classic Haircut", 450.0, "45 min", "A precision cut tailored to your style.", "hair"),
        Service("ser2", "Beard Trim & Shape", 250.0, "30 min", "Professional grooming for your beard.", "beard")
    )

    override fun getService(serviceId: String): Flow<Service?> = flow {
        delay(300)
        emit(mockServices.find { it.id == serviceId })
    }

    override fun getAvailability(shopId: String, professionalId: String?): Flow<List<BookingAvailability>> = flow {
        delay(600)
        emit(
            listOf(
                BookingAvailability(
                    date = "Oct 24, 2024",
                    slots = listOf(
                        TimeSlot("09:00 AM"),
                        TimeSlot("10:00 AM"),
                        TimeSlot("11:00 AM", isAvailable = false),
                        TimeSlot("02:00 PM"),
                        TimeSlot("04:00 PM")
                    )
                ),
                BookingAvailability(
                    date = "Oct 25, 2024",
                    slots = listOf(
                        TimeSlot("10:00 AM"),
                        TimeSlot("12:00 PM"),
                        TimeSlot("03:00 PM")
                    )
                )
            )
        )
    }

    override suspend fun bookSlot(
        serviceId: String,
        professionalId: String?,
        date: String,
        time: String,
        isHomeService: Boolean
    ): Boolean {
        delay(1500) // Simulate network booking
        return true
    }
}
