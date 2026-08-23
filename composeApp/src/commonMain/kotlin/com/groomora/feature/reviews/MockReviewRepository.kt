package com.groomora.feature.reviews

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockReviewRepository : ReviewRepository {
    private val mockReviews = listOf(
        Review(
            id = "rev1",
            userId = "u1",
            userName = "Amit Sharma",
            rating = 5,
            comment = "Excellent service! The haircut was exactly what I wanted.",
            date = "Oct 20, 2024",
            isVerified = true
        ),
        Review(
            id = "rev2",
            userId = "u2",
            userName = "Priya K.",
            rating = 4,
            comment = "Very professional staff. A bit of a wait but worth it.",
            date = "Oct 18, 2024",
            reply = "Thank you Priya! We are working on reducing wait times.",
            isVerified = true
        ),
        Review(
            id = "rev3",
            userId = "u3",
            userName = "Rahul M.",
            rating = 5,
            comment = "Best beard trim in the city. Highly recommended!",
            date = "Oct 15, 2024",
            isVerified = true
        )
    )

    override fun getReviews(targetId: String, type: ReviewTargetType): Flow<List<Review>> = flow {
        delay(600)
        emit(mockReviews)
    }

    override suspend fun submitReview(
        targetId: String,
        type: ReviewTargetType,
        rating: Int,
        comment: String
    ): Boolean {
        delay(1000)
        return true
    }
}
