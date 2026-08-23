package com.groomora.feature.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.groomora.core.location.LocationRepository
import com.groomora.feature.offers.OffersRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BookingViewModel(
    private val bookingRepository: BookingRepository,
    private val locationRepository: LocationRepository,
    private val offersRepository: OffersRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BookingState())
    val state: StateFlow<BookingState> = _state.asStateFlow()

    fun onIntent(intent: BookingIntent) {
        when (intent) {
            is BookingIntent.Initialize -> initializeBooking(intent.serviceId, intent.packageId)
            is BookingIntent.SelectProfessional -> {
                _state.update { it.copy(selectedProfessional = intent.professional) }
                loadAvailability()
            }
            is BookingIntent.SelectDate -> {
                _state.update { it.copy(selectedDate = intent.date, selectedTime = null) }
            }
            is BookingIntent.SelectTime -> {
                _state.update { it.copy(selectedTime = intent.time) }
            }
            is BookingIntent.ToggleHomeService -> {
                _state.update {
                    val newState = it.copy(isHomeService = intent.isHome)
                    newState.copy(priceBreakdown = calculatePrice(newState))
                }
            }
            is BookingIntent.ToggleAddOn -> {
                val current = _state.value.selectedAddOns.toMutableSet()
                if (current.contains(intent.addOnId)) current.remove(intent.addOnId)
                else current.add(intent.addOnId)
                _state.update {
                    val newState = it.copy(selectedAddOns = current)
                    newState.copy(priceBreakdown = calculatePrice(newState))
                }
            }
            is BookingIntent.UpdateCouponInput -> {
                _state.update { it.copy(couponCodeInput = intent.code, couponError = null) }
            }
            is BookingIntent.ApplyOfferCode -> applyOffer(intent.code)
            is BookingIntent.RemoveOfferCode -> {
                _state.update {
                    val newState = it.copy(appliedOffer = null, couponSuccessMessage = null, couponError = null)
                    newState.copy(priceBreakdown = calculatePrice(newState))
                }
            }
            is BookingIntent.SelectPaymentMethod -> {
                _state.update { it.copy(selectedPaymentMethod = intent.method) }
            }
            is BookingIntent.ToggleLoyaltyRedemption -> {
                _state.update {
                    val newState = it.copy(redeemLoyaltyPoints = intent.redeem)
                    newState.copy(priceBreakdown = calculatePrice(newState))
                }
            }
            BookingIntent.ConfirmBooking -> confirmBooking()
        }
    }

    private fun initializeBooking(serviceId: String?, packageId: String?) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            if (serviceId != null) {
                bookingRepository.getService(serviceId).collect { service ->
                    _state.update {
                        val newState = it.copy(
                            isLoading = false,
                            selectedService = service,
                            selectedPackage = null,
                            availableAddOns = listOf(
                                AddOn("a1", "Extra Scalp Massage", 200.0, "15 min"),
                                AddOn("a2", "Premium Hair Serum Treatment", 150.0, "5 min"),
                                AddOn("a3", "Charcoal Nose Strip", 100.0, "10 min")
                            ),
                            userPointsBalance = 1250
                        )
                        newState.copy(priceBreakdown = calculatePrice(newState))
                    }
                    loadAvailability()
                }
            } else if (packageId != null) {
                bookingRepository.getPackage(packageId).collect { pkg ->
                    _state.update {
                        val newState = it.copy(
                            isLoading = false,
                            selectedPackage = pkg,
                            selectedService = null,
                            availableAddOns = listOf(
                                AddOn("a1", "Extra Scalp Massage", 200.0, "15 min"),
                                AddOn("a4", "Aromatherapy Mist", 180.0, "10 min")
                            ),
                            userPointsBalance = 1250
                        )
                        newState.copy(priceBreakdown = calculatePrice(newState))
                    }
                    loadAvailability()
                }
            } else {
                // Fallback default service
                bookingRepository.getService("ser1").collect { service ->
                    _state.update {
                        val newState = it.copy(
                            isLoading = false,
                            selectedService = service,
                            availableAddOns = listOf(
                                AddOn("a1", "Extra Scalp Massage", 200.0, "15 min"),
                                AddOn("a2", "Premium Hair Serum Treatment", 150.0, "5 min")
                            ),
                            userPointsBalance = 1250
                        )
                        newState.copy(priceBreakdown = calculatePrice(newState))
                    }
                    loadAvailability()
                }
            }
        }
    }

    private fun applyOffer(code: String) {
        if (code.isBlank()) {
            _state.update { it.copy(couponError = "Please enter a valid coupon code") }
            return
        }
        viewModelScope.launch {
            val offer = offersRepository.getOfferByCode(code.trim().uppercase())
            if (offer != null) {
                _state.update {
                    val newState = it.copy(
                        appliedOffer = offer,
                        couponError = null,
                        couponSuccessMessage = "Coupon '${offer.code}' applied successfully!"
                    )
                    newState.copy(priceBreakdown = calculatePrice(newState))
                }
            } else {
                _state.update { it.copy(couponError = "Coupon '$code' is invalid or expired", couponSuccessMessage = null) }
            }
        }
    }

    private fun calculatePrice(state: BookingState): PriceBreakdown {
        val base = state.selectedService?.price ?: state.selectedPackage?.price ?: 0.0
        val addOns = state.availableAddOns
            .filter { state.selectedAddOns.contains(it.id) }
            .sumOf { it.price }
        val travel = if (state.isHomeService) 99.0 else 0.0

        var discount = 0.0
        state.appliedOffer?.let { offer ->
            discount = if (offer.type == com.groomora.feature.offers.OfferType.FLAT) {
                offer.discountValue
            } else {
                (base + addOns) * (offer.discountValue / 100.0)
            }
            offer.maxDiscount?.let { max -> if (discount > max) discount = max }
        }

        val loyalty = if (state.redeemLoyaltyPoints) 50.0 else 0.0

        val total = (base + addOns + travel - discount - loyalty).coerceAtLeast(0.0)

        return PriceBreakdown(
            basePrice = base,
            addOnsTotal = addOns,
            travelFee = travel,
            discount = discount,
            loyaltyRedemption = loyalty,
            total = total
        )
    }

    private fun loadAvailability() {
        val shopId = "s1"
        val profId = _state.value.selectedProfessional?.id

        viewModelScope.launch {
            bookingRepository.getAvailability(shopId, profId).collect { availability ->
                _state.update { it.copy(availability = availability) }
            }
        }
    }

    private fun confirmBooking() {
        val currentState = _state.value
        val hasItem = currentState.selectedService != null || currentState.selectedPackage != null
        if (!hasItem || currentState.selectedDate == null || currentState.selectedTime == null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val bookingId = bookingRepository.bookSlot(
                serviceId = currentState.selectedService?.id,
                packageId = currentState.selectedPackage?.id,
                professionalId = currentState.selectedProfessional?.id,
                date = currentState.selectedDate,
                time = currentState.selectedTime,
                isHomeService = currentState.isHomeService,
                paymentMethod = currentState.selectedPaymentMethod,
                totalAmount = currentState.priceBreakdown.total
            )
            _state.update {
                it.copy(
                    isLoading = false,
                    isBookingConfirmed = bookingId != null,
                    lastConfirmedBookingId = bookingId
                )
            }
        }
    }
}
