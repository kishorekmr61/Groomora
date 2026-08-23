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
        Service("ser1", "Luxury Hair Spa & Scalp Therapy", 599.0, "60 min", "Deep conditioning & scalp massage", "hair"),
        Service("ser2", "Master Haircut & Style Consultation", 299.0, "30 min", "Master cut, wash & blow dry", "hair"),
        Service("ser3", "Royal Beard Trim & Hot Towel Shape", 199.0, "25 min", "Razor sharp beard detailing", "beard"),
        Service("ser4", "Face Clean Up & Charcoal Detan", 499.0, "40 min", "Instant brightening & pore cleanse", "skin"),
        Service("ser5", "Full Body Swedish Massage", 1499.0, "60 min", "Aromatherapy relaxation with soothing warm essential oils", "spa"),
        Service("ser6", "Gel Nail Art & Polish Extension", 699.0, "45 min", "Long-lasting UV gel finish with custom nail art", "nails"),
        Service("ser7", "Hydrating Diamond Facial", 899.0, "50 min", "Cellular glow treatment with natural diamond dust mask", "skin"),
        Service("ser8", "Bridal Glow Pre-Wedding Ritual", 2499.0, "120 min", "Full body polish, organic facial & hair conditioning", "bridal"),
        Service("ser9", "Doorstep At-Home Haircut & Grooming", 399.0, "45 min", "Certified barber visits your home with sanitized toolkit", "home")
    )

    private val mockPackages = listOf(
        // Bridal Packages
        ServicePackage(
            id = "br1",
            name = "Royal Bridal Radiance",
            description = "Complete bridal makeover including HD makeup, luxury hairstyling, and saree/lehenga draping.",
            price = 15000.0,
            originalPrice = 20000.0,
            services = listOf("HD Makeup", "Luxury Hairstyling", "Saree Draping", "Skin Prep")
        ),
        ServicePackage(
            id = "br2",
            name = "Engagement Glow",
            description = "Sophisticated look for your engagement ceremony. Includes makeup and hairstyling.",
            price = 8000.0,
            originalPrice = 11000.0,
            services = listOf("Engagement Makeup", "Hairstyling", "Draping")
        ),
        ServicePackage(
            id = "br3",
            name = "Bridal Party Package",
            description = "Package for bridesmaids and family. Minimalist yet elegant makeup and hair.",
            price = 3500.0,
            originalPrice = 5000.0,
            services = listOf("Party Makeup", "Simple Hairstyling")
        ),
        // Home Service Best-Seller Packages
        ServicePackage(
            id = "pkg_home_glow",
            name = "Complete Glow Ritual",
            description = "Brightening Vitamin C Facial, Waxing, Threading & Scalp Massage",
            price = 1299.0,
            originalPrice = 1899.0,
            services = listOf("Vitamin C Facial", "Honey Waxing", "Threading", "Scalp Massage")
        ),
        ServicePackage(
            id = "pkg_home_spa",
            name = "Aromatherapy Stress Relief",
            description = "Lavender Essential Oil Full Body Massage, Foot Reflexology & Hot Towel Therapy",
            price = 1499.0,
            originalPrice = 2199.0,
            services = listOf("Essential Oil Massage", "Foot Reflexology", "Hot Towel Therapy")
        ),
        ServicePackage(
            id = "pkg_home_mens",
            name = "Men's Executive Grooming",
            description = "Signature Haircut, Precision Beard Trim, Charcoal Detan & Head Massage",
            price = 799.0,
            originalPrice = 1199.0,
            services = listOf("Signature Haircut", "Beard Trim & Shave", "Charcoal Detan", "Head Massage")
        ),
        ServicePackage(
            id = "pkg_home_manipedi",
            name = "Rose Deluxe Mani-Pedi Duo",
            description = "Organic Rose Petal Foot Soak, Scrub, Cuticle Care & Gel Polish",
            price = 699.0,
            originalPrice = 999.0,
            services = listOf("Rose Petal Soak", "Callus Removal", "Cuticle Oil", "Gel Polish")
        ),
        // Shop Details Packages
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
        delay(150)
        val found = mockServices.find { it.id == serviceId } ?: Service(
            id = serviceId,
            name = "Custom Salon Service",
            price = 499.0,
            duration = "45 min",
            description = "Professional grooming treatment",
            category = "hair"
        )
        emit(found)
    }

    override fun getPackage(packageId: String): Flow<ServicePackage?> = flow {
        delay(150)
        val found = mockPackages.find { it.id == packageId } ?: ServicePackage(
            id = packageId,
            name = when {
                packageId.contains("bridal", ignoreCase = true) || packageId.startsWith("br") -> "Royal Bridal Package"
                packageId.contains("home", ignoreCase = true) || packageId.startsWith("hp") -> "Luxury Doorstep Spa Package"
                else -> "Grooming Deluxe Package"
            },
            description = "All-inclusive premium grooming package",
            price = when {
                packageId.startsWith("br") -> 8000.0
                packageId.startsWith("hp") || packageId.startsWith("pkg_home") -> 1299.0
                else -> 999.0
            },
            originalPrice = 1499.0,
            services = listOf("Premium Styling", "Detan Cleanup", "Relaxing Massage")
        )
        emit(found)
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
