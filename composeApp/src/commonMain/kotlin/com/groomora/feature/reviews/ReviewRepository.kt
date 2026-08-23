package com.groomora.feature.reviews

import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    fun getReviews(targetId: String, type: ReviewTargetType): Flow<List<Review>>
    suspend fun submitReview(targetId: String, type: ReviewTargetType, rating: Int, comment: String): Boolean
}
