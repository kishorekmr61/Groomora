package com.groomora.feature.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.groomora.core.location.LocationRepository
import com.groomora.feature.discovery.Professional
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
            is BookingIntent.Initialize -> initializeBooking(intent.serviceId)
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
            is BookingIntent.ApplyOfferCode -> applyOffer(intent.code)
            is BookingIntent.ToggleLoyaltyRedemption -> {
                _state.update { 
                    val newState = it.copy(redeemLoyaltyPoints = intent.redeem)
                    newState.copy(priceBreakdown = calculatePrice(newState))
                }
            }
            BookingIntent.ConfirmBooking -> confirmBooking()
        }
    }

    private fun initializeBooking(serviceId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            // Get user points and default address
            val addresses = locationRepository.getSavedAddresses().firstOrNull() ?: emptyList()
            val defaultAddress = addresses.find { it.isDefault } ?: addresses.firstOrNull()
            
            bookingRepository.getService(serviceId).collect { service ->
                _state.update { 
                    val newState = it.copy(
                        isLoading = false, 
                        selectedService = service,
                        availableAddOns = listOf(
                            AddOn("a1", "Extra Scalp Massage", 200.0, "15 min"),
                            AddOn("a2", "Premium Hair Serum", 150.0, "5 min")
                        ),
                        userPointsBalance = 1250 // Mock
                    )
                    newState.copy(priceBreakdown = calculatePrice(newState))
                }
                loadAvailability()
            }
        }
    }

    private fun applyOffer(code: String) {
        viewModelScope.launch {
            val offer = offersRepository.getOfferByCode(code)
            if (offer != null) {
                _state.update { 
                    val newState = it.copy(appliedOffer = offer, error = null)
                    newState.copy(priceBreakdown = calculatePrice(newState))
                }
            } else {
                _state.update { it.copy(error = "Invalid coupon code") }
            }
        }
    }

    private fun calculatePrice(state: BookingState): PriceBreakdown {
        val base = state.selectedService?.price ?: 0.0
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
        
        return PriceBreakdown(
            basePrice = base,
            addOnsTotal = addOns,
            travelFee = travel,
            discount = discount,
            loyaltyRedemption = loyalty,
            total = base + addOns + travel - discount - loyalty
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
        if (currentState.selectedService == null || currentState.selectedDate == null || currentState.selectedTime == null) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val success = bookingRepository.bookSlot(
                serviceId = currentState.selectedService.id,
                professionalId = currentState.selectedProfessional?.id,
                date = currentState.selectedDate,
                time = currentState.selectedTime,
                isHomeService = currentState.isHomeService
            )
            _state.update { it.copy(isLoading = false, isBookingConfirmed = success) }
        }
    }
}
