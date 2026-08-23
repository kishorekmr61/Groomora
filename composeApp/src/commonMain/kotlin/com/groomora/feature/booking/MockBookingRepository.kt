package com.groomora.feature.booking

import com.groomora.feature.shop.Service
import com.groomora.feature.shop.ServicePackage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class MockBookingRepository : BookingRepository {
    private val mockServices = listOf(
        Service("ser1", "Classic Haircut", 450.0, "45 min", "A precision cut tailored to your style.", "hair"),
        Service("ser2", "Beard Trim & Shape", 250.0, "30 min", "Professional grooming for your beard.", "beard"),
        Service("ser3", "Express Facial & Glow", 699.0, "40 min", "Deep cleansing and brightening facial.", "skin"),
        Service("ser4", "Bridal Glow Deluxe", 2999.0, "120 min", "Full pre-bridal session.", "bridal")
    )

    private val mockPackages = listOf(
        ServicePackage(
            id = "pkg1",
            name = "Groom's Classic Grooming Combo",
            description = "Haircut, Beard Styling, Scalp Massage & Charcoal Mask",
            price = 999.0,
            originalPrice = 1450.0,
            services = listOf("Haircut", "Beard Styling", "Scalp Massage")
        ),
        ServicePackage(
            id = "pkg2",
            name = "Royal Bridal Package",
            description = "HD Makeup, Hair Styling, Saree Draping & Pre-Bridal Glow",
            price = 7999.0,
            originalPrice = 11500.0,
            services = listOf("HD Makeup", "Hair Styling", "Pre-Bridal Glow")
        )
    )


    private val _userBookings = MutableStateFlow<List<BookingRecord>>(
        listOf(
            BookingRecord(
                id = "BK-1082",
                title = "Classic Haircut & Beard Trim",
                shopName = "Vogue Studio & Barbershop",
                professionalName = "Alex Rivera (Master Barber)",
                date = "Oct 26, 2024",
                time = "10:00 AM",
                isHomeService = false,
                totalAmount = 650.0,
                paymentMethod = PaymentMethodType.UPI,
                status = BookingStatus.CONFIRMED,
                canReschedule = true,
                canCancel = true,
                canReview = false,
                canRebook = true
            ),
            BookingRecord(
                id = "BK-0941",
                title = "Express Facial & Glow",
                shopName = "Radiance Beauty & Spa Lounge",
                professionalName = "Elena Vance (Skincare Specialist)",
                date = "Oct 12, 2024",
                time = "04:00 PM",
                isHomeService = true,
                totalAmount = 798.0,
                paymentMethod = PaymentMethodType.CREDIT_DEBIT_CARD,
                status = BookingStatus.COMPLETED,
                canReschedule = false,
                canCancel = false,
                canReview = true,
                canRebook = true
            ),
            BookingRecord(
                id = "BK-0819",
                title = "Groom's Classic Grooming Combo",
                shopName = "Vogue Studio & Barbershop",
                professionalName = "Alex Rivera",
                date = "Sep 28, 2024",
                time = "02:00 PM",
                isHomeService = false,
                totalAmount = 999.0,
                paymentMethod = PaymentMethodType.UPI,
                status = BookingStatus.REFUNDED,
                refundStatus = RefundStatus.REFUNDED,
                cancellationReason = "Schedule conflict",
                canReschedule = false,
                canCancel = false,
                canReview = false,
                canRebook = true
            )
        )
    )

    override fun getService(serviceId: String): Flow<Service?> = flow {
        delay(200)
        emit(mockServices.find { it.id == serviceId })
    }

    override fun getPackage(packageId: String): Flow<ServicePackage?> = flow {
        delay(200)
        emit(mockPackages.find { it.id == packageId })
    }

    override fun getAvailability(shopId: String, professionalId: String?): Flow<List<BookingAvailability>> = flow {
        delay(300)
        emit(
            listOf(
                BookingAvailability(
                    date = "Today, Oct 24",
                    slots = listOf(
                        TimeSlot("09:00 AM"),
                        TimeSlot("10:30 AM"),
                        TimeSlot("11:30 AM", isAvailable = false),
                        TimeSlot("02:00 PM"),
                        TimeSlot("03:30 PM"),
                        TimeSlot("05:00 PM")
                    )
                ),
                BookingAvailability(
                    date = "Tomorrow, Oct 25",
                    slots = listOf(
                        TimeSlot("10:00 AM"),
                        TimeSlot("11:00 AM"),
                        TimeSlot("01:00 PM"),
                        TimeSlot("03:00 PM"),
                        TimeSlot("04:30 PM")
                    )
                ),
                BookingAvailability(
                    date = "Sat, Oct 26",
                    slots = listOf(
                        TimeSlot("09:30 AM"),
                        TimeSlot("11:30 AM"),
                        TimeSlot("02:30 PM"),
                        TimeSlot("04:00 PM"),
                        TimeSlot("06:00 PM")
                    )
                )
            )
        )
    }

    override suspend fun bookSlot(
        serviceId: String?,
        packageId: String?,
        professionalId: String?,
        date: String,
        time: String,
        isHomeService: Boolean,
        paymentMethod: PaymentMethodType,
        totalAmount: Double
    ): String {
        delay(800)
        val newId = "BK-" + (1000..9999).random()
        val title = serviceId?.let { sId -> mockServices.find { it.id == sId }?.name }
            ?: packageId?.let { pId -> mockPackages.find { it.id == pId }?.name }
            ?: "Custom Service"
        
        val newBooking = BookingRecord(
            id = newId,
            title = title,
            shopName = "Vogue Studio & Barbershop",
            professionalName = if (professionalId != null) "Selected Professional" else "Any Available Stylist",
            date = date,
            time = time,
            isHomeService = isHomeService,
            totalAmount = totalAmount,
            paymentMethod = paymentMethod,
            status = BookingStatus.CONFIRMED,
            canReschedule = true,
            canCancel = true,
            canReview = false,
            canRebook = true
        )
        _userBookings.value = listOf(newBooking) + _userBookings.value
        return newId
    }

    override fun getUserBookings(): Flow<List<BookingRecord>> = _userBookings.asStateFlow()

    override suspend fun rescheduleBooking(bookingId: String, newDate: String, newTime: String): Boolean {
        delay(500)
        _userBookings.value = _userBookings.value.map { booking ->
            if (booking.id == bookingId) {
                booking.copy(date = newDate, time = newTime, status = BookingStatus.CONFIRMED)
            } else {
                booking
            }
        }
        return true
    }

    override suspend fun cancelBooking(bookingId: String, reason: String): Boolean {
        delay(500)
        _userBookings.value = _userBookings.value.map { booking ->
            if (booking.id == bookingId) {
                booking.copy(
                    status = BookingStatus.CANCELLED,
                    refundStatus = RefundStatus.PROCESSING,
                    cancellationReason = reason,
                    canReschedule = false,
                    canCancel = false
                )
            } else {
                booking
            }
        }
        return true
    }
}
