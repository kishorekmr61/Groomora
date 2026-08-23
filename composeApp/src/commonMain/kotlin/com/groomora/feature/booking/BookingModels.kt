package com.groomora.feature.booking

import com.groomora.feature.shop.Service
import com.groomora.feature.shop.ServicePackage
import com.groomora.feature.discovery.Professional
import com.groomora.feature.offers.Offer
import kotlinx.serialization.Serializable

@Serializable
data class TimeSlot(
    val time: String,
    val isAvailable: Boolean = true
)

@Serializable
data class BookingAvailability(
    val date: String,
    val slots: List<TimeSlot>
)

@Serializable
data class AddOn(
    val id: String,
    val name: String,
    val price: Double,
    val duration: String
)

@Serializable
enum class PaymentMethodType {
    UPI,
    CREDIT_DEBIT_CARD,
    NET_BANKING,
    PAY_AT_VENUE
}

@Serializable
enum class BookingStatus {
    CONFIRMED,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    REFUNDED
}

@Serializable
enum class RefundStatus {
    NONE,
    INITIATED,
    PROCESSING,
    REFUNDED
}

@Serializable
data class BookingRecord(
    val id: String,
    val title: String,
    val shopName: String,
    val professionalName: String?,
    val date: String,
    val time: String,
    val isHomeService: Boolean,
    val totalAmount: Double,
    val paymentMethod: PaymentMethodType,
    val status: BookingStatus,
    val refundStatus: RefundStatus = RefundStatus.NONE,
    val cancellationReason: String? = null,
    val canReschedule: Boolean = true,
    val canCancel: Boolean = true,
    val canReview: Boolean = false,
    val canRebook: Boolean = true
)

data class BookingState(
    val isLoading: Boolean = false,
    val selectedService: Service? = null,
    val selectedPackage: ServicePackage? = null,
    val selectedProfessional: Professional? = null,
    val selectedDate: String? = null,
    val selectedTime: String? = null,
    val availability: List<BookingAvailability> = emptyList(),
    val availableAddOns: List<AddOn> = emptyList(),
    val selectedAddOns: Set<String> = emptySet(),
    val isHomeService: Boolean = false,
    val couponCodeInput: String = "",
    val appliedOffer: Offer? = null,
    val couponError: String? = null,
    val couponSuccessMessage: String? = null,
    val redeemLoyaltyPoints: Boolean = false,
    val userPointsBalance: Int = 1250,
    val selectedPaymentMethod: PaymentMethodType = PaymentMethodType.UPI,
    val priceBreakdown: PriceBreakdown = PriceBreakdown(),
    val error: String? = null,
    val isBookingConfirmed: Boolean = false,
    val lastConfirmedBookingId: String? = null
)

@Serializable
data class PriceBreakdown(
    val basePrice: Double = 0.0,
    val addOnsTotal: Double = 0.0,
    val travelFee: Double = 0.0,
    val discount: Double = 0.0,
    val loyaltyRedemption: Double = 0.0,
    val total: Double = 0.0
)

sealed interface BookingIntent {
    data class Initialize(val serviceId: String? = null, val packageId: String? = null) : BookingIntent
    data class SelectProfessional(val professional: Professional?) : BookingIntent
    data class SelectDate(val date: String) : BookingIntent
    data class SelectTime(val time: String) : BookingIntent
    data class ToggleHomeService(val isHome: Boolean) : BookingIntent
    data class ToggleAddOn(val addOnId: String) : BookingIntent
    data class UpdateCouponInput(val code: String) : BookingIntent
    data class ApplyOfferCode(val code: String) : BookingIntent
    data object RemoveOfferCode : BookingIntent
    data class SelectPaymentMethod(val method: PaymentMethodType) : BookingIntent
    data class ToggleLoyaltyRedemption(val redeem: Boolean) : BookingIntent
    data object ConfirmBooking : BookingIntent
}
