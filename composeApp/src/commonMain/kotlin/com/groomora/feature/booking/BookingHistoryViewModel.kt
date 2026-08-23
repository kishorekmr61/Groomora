package com.groomora.feature.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BookingHistoryState(
    val isLoading: Boolean = false,
    val bookings: List<BookingRecord> = emptyList(),
    val reschedulingBookingId: String? = null,
    val cancellingBookingId: String? = null,
    val message: String? = null
)

sealed interface BookingHistoryIntent {
    data object LoadBookings : BookingHistoryIntent
    data class InitiateReschedule(val bookingId: String) : BookingHistoryIntent
    data class ConfirmReschedule(val bookingId: String, val newDate: String, val newTime: String) : BookingHistoryIntent
    data class DismissRescheduleDialog(val dummy: Unit = Unit) : BookingHistoryIntent
    data class InitiateCancel(val bookingId: String) : BookingHistoryIntent
    data class ConfirmCancel(val bookingId: String, val reason: String) : BookingHistoryIntent
    data class DismissCancelDialog(val dummy: Unit = Unit) : BookingHistoryIntent
}

class BookingHistoryViewModel(
    private val bookingRepository: BookingRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BookingHistoryState())
    val state: StateFlow<BookingHistoryState> = _state.asStateFlow()

    init {
        onIntent(BookingHistoryIntent.LoadBookings)
    }

    fun onIntent(intent: BookingHistoryIntent) {
        when (intent) {
            BookingHistoryIntent.LoadBookings -> loadBookings()
            is BookingHistoryIntent.InitiateReschedule -> {
                _state.update { it.copy(reschedulingBookingId = intent.bookingId) }
            }
            is BookingHistoryIntent.ConfirmReschedule -> reschedule(intent.bookingId, intent.newDate, intent.newTime)
            is BookingHistoryIntent.DismissRescheduleDialog -> {
                _state.update { it.copy(reschedulingBookingId = null) }
            }
            is BookingHistoryIntent.InitiateCancel -> {
                _state.update { it.copy(cancellingBookingId = intent.bookingId) }
            }
            is BookingHistoryIntent.ConfirmCancel -> cancel(intent.bookingId, intent.reason)
            is BookingHistoryIntent.DismissCancelDialog -> {
                _state.update { it.copy(cancellingBookingId = null) }
            }
        }
    }

    private fun loadBookings() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            bookingRepository.getUserBookings().collect { list ->
                _state.update { it.copy(isLoading = false, bookings = list) }
            }
        }
    }

    private fun reschedule(bookingId: String, newDate: String, newTime: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, reschedulingBookingId = null) }
            bookingRepository.rescheduleBooking(bookingId, newDate, newTime)
            _state.update { it.copy(isLoading = false, message = "Booking rescheduled successfully to $newDate at $newTime") }
        }
    }

    private fun cancel(bookingId: String, reason: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, cancellingBookingId = null) }
            bookingRepository.cancelBooking(bookingId, reason)
            _state.update { it.copy(isLoading = false, message = "Booking cancelled. Refund of 100% initiated to original payment method.") }
        }
    }
}
