package com.groomora.core.api

import kotlinx.serialization.Serializable

/**
 * Standard generic API response envelope returned by all Groomora backend endpoints.
 */
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val errorCode: String? = null,
    val timestamp: Long = 0L
)

/**
 * Generic Paginated Response envelope.
 */
@Serializable
data class PaginatedResponse<T>(
    val items: List<T> = emptyList(),
    val page: Int = 1,
    val totalPages: Int = 1,
    val totalItems: Int = 0,
    val hasMore: Boolean = false
)

// ==========================================
// REQUEST DATA TRANSFER OBJECTS (DTOs)
// ==========================================

@Serializable
data class SendOtpRequest(
    val phoneNumber: String
)

@Serializable
data class VerifyOtpRequest(
    val phoneNumber: String,
    val otp: String
)

@Serializable
data class LoginPasswordRequest(
    val phoneNumber: String,
    val password: String
)

@Serializable
data class SignUpRequest(
    val name: String,
    val phoneNumber: String,
    val email: String,
    val gender: String,
    val password: String,
    val referralCode: String? = null
)

@Serializable
data class AuthResponseData(

    val token: String,
    val userId: String,
    val name: String,
    val phoneNumber: String,
    val email: String? = null,
    val profileImageUrl: String? = null
)

@Serializable
data class CreateBookingRequest(
    val serviceId: String? = null,
    val packageId: String? = null,
    val professionalId: String? = null,
    val date: String,
    val timeSlot: String,
    val couponCode: String? = null,
    val paymentMethod: String = "ONLINE"
)

@Serializable
data class RescheduleBookingRequest(
    val bookingId: String,
    val newDate: String,
    val newTimeSlot: String
)

@Serializable
data class CancelBookingRequest(
    val bookingId: String,
    val reason: String
)

@Serializable
data class ValidateCouponRequest(
    val couponCode: String,
    val cartAmount: Double
)

@Serializable
data class ValidateCouponResponseData(
    val isValid: Boolean,
    val code: String,
    val discountAmount: Double,
    val finalAmount: Double,
    val message: String
)

@Serializable
data class CreateOrderRequest(
    val items: List<OrderItemRequest>,
    val deliveryAddressId: String,
    val paymentMethod: String = "CARD"
)

@Serializable
data class OrderItemRequest(
    val productId: String,
    val quantity: Int
)

@Serializable
data class SubmitReviewRequest(
    val targetId: String,
    val rating: Int,
    val comment: String,
    val type: String = "SHOP"
)

