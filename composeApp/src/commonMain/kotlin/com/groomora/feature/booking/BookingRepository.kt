package com.groomora.feature.booking

import com.groomora.feature.shop.Service
import com.groomora.feature.shop.ServicePackage
import com.groomora.feature.discovery.Professional
import kotlinx.coroutines.flow.Flow

interface BookingRepository {
    fun getService(serviceId: String): Flow<Service?>
    fun getPackage(packageId: String): Flow<ServicePackage?>
    fun getAvailability(shopId: String, professionalId: String?): Flow<List<BookingAvailability>>
    suspend fun bookSlot(
        serviceId: String?,
        packageId: String?,
        professionalId: String?,
        date: String,
        time: String,
        isHomeService: Boolean,
        paymentMethod: PaymentMethodType,
        totalAmount: Double
    ): String? // returns booking ID if success

    fun getUserBookings(): Flow<List<BookingRecord>>
    suspend fun rescheduleBooking(bookingId: String, newDate: String, newTime: String): Boolean
    suspend fun cancelBooking(bookingId: String, reason: String): Boolean
}
