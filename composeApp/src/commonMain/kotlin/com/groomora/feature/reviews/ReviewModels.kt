package com.groomora.feature.reviews

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val id: String,
    val userId: String,
    val userName: String,
    val userImageUrl: String? = null,
    val rating: Int,
    val comment: String,
    val date: String,
    val reply: String? = null,
    val isVerified: Boolean = true
)

data class ReviewState(
    val isLoading: Boolean = false,
    val reviews: List<Review> = emptyList(),
    val averageRating: Double = 0.0,
    val totalReviews: Int = 0,
    val error: String? = null
)

sealed interface ReviewIntent {
    data class LoadReviews(val targetId: String, val type: ReviewTargetType) : ReviewIntent
    data class SubmitReview(val targetId: String, val type: ReviewTargetType, val rating: Int, val comment: String) : ReviewIntent
}

enum class ReviewTargetType {
    SHOP, PROFESSIONAL, PRODUCT
}
