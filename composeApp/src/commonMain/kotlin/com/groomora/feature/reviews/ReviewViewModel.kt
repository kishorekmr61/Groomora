package com.groomora.feature.reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ReviewViewModel(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ReviewState())
    val state: StateFlow<ReviewState> = _state.asStateFlow()

    fun onIntent(intent: ReviewIntent) {
        when (intent) {
            is ReviewIntent.LoadReviews -> loadReviews(intent.targetId, intent.type)
            is ReviewIntent.SubmitReview -> submitReview(intent.targetId, intent.type, intent.rating, intent.comment)
        }
    }

    private fun loadReviews(targetId: String, type: ReviewTargetType) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            reviewRepository.getReviews(targetId, type).collect { reviews ->
                val avg = if (reviews.isNotEmpty()) reviews.map { it.rating }.average() else 0.0
                _state.update { it.copy(
                    isLoading = false,
                    reviews = reviews,
                    averageRating = avg,
                    totalReviews = reviews.size
                ) }
            }
        }
    }

    private fun submitReview(targetId: String, type: ReviewTargetType, rating: Int, comment: String) {
        viewModelScope.launch {
            val success = reviewRepository.submitReview(targetId, type, rating, comment)
            if (success) {
                loadReviews(targetId, type)
            }
        }
    }
}
