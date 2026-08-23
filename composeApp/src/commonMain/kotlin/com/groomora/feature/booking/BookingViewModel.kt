package com.groomora.feature.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.groomora.core.location.LocationRepository
import com.groomora.feature.offers.Offer
import com.groomora.feature.offers.OfferType
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
            is BookingIntent.Initialize -> initializeBooking(
                serviceId = intent.serviceId,
                packageId = intent.packageId,
                serviceIds = intent.serviceIds,
                shopId = intent.shopId
            )
            is BookingIntent.ToggleService -> {
                val current = _state.value.selectedServices.toMutableList()
                if (current.any { it.id == intent.service.id }) {
                    current.removeAll { it.id == intent.service.id }
                } else {
                    current.add(intent.service)
                }
                _state.update {
                    val newState = it.copy(
                        selectedServices = current,
                        selectedService = current.firstOrNull()
                    )
                    newState.copy(priceBreakdown = calculatePrice(newState))
                }
            }
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

    private fun initializeBooking(
        serviceId: String?,
        packageId: String?,
        serviceIds: List<String> = emptyList(),
        shopId: String? = null
    ) {
        val targetShopId = shopId ?: "s1"
        val shopName = when (targetShopId) {
            "s1" -> "The Golden Scissor"
            "s2" -> "Radiance Beauty & Spa Lounge"
            "s3" -> "Vogue Studio & Barbershop"
            "s4" -> "Urban Glow Wellness"
            else -> "King's Barber Studio"
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null,
                    shopId = targetShopId,
                    shopName = shopName
                )
            }

            if (serviceIds.isNotEmpty()) {
                val loadedList = mutableListOf<com.groomora.feature.shop.Service>()
                for (id in serviceIds) {
                    bookingRepository.getService(id).firstOrNull()?.let { loadedList.add(it) }
                }
                _state.update {
                    val newState = it.copy(
                        isLoading = false,
                        shopId = targetShopId,
                        shopName = shopName,
                        selectedService = loadedList.firstOrNull(),
                        selectedServices = loadedList,
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
                loadAvailability(targetShopId)
            } else if (serviceId != null) {
                bookingRepository.getService(serviceId).collect { service ->
                    _state.update {
                        val initialList = if (service != null) listOf(service) else emptyList()
                        val newState = it.copy(
                            isLoading = false,
                            shopId = targetShopId,
                            shopName = shopName,
                            selectedService = service,
                            selectedServices = initialList,
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
                    loadAvailability(targetShopId)
                }
            } else if (packageId != null) {
                bookingRepository.getPackage(packageId).collect { pkg ->
                    _state.update {
                        val newState = it.copy(
                            isLoading = false,
                            shopId = targetShopId,
                            shopName = shopName,
                            selectedPackage = pkg,
                            selectedService = null,
                            selectedServices = emptyList(),
                            availableAddOns = listOf(
                                AddOn("a1", "Extra Scalp Massage", 200.0, "15 min"),
                                AddOn("a4", "Aromatherapy Mist", 180.0, "10 min")
                            ),
                            userPointsBalance = 1250
                        )
                        newState.copy(priceBreakdown = calculatePrice(newState))
                    }
                    loadAvailability(targetShopId)
                }
            } else {
                // Fallback default service
                bookingRepository.getService("ser1").collect { service ->
                    _state.update {
                        val initialList = if (service != null) listOf(service) else emptyList()
                        val newState = it.copy(
                            isLoading = false,
                            shopId = targetShopId,
                            shopName = shopName,
                            selectedService = service,
                            selectedServices = initialList,
                            availableAddOns = listOf(
                                AddOn("a1", "Extra Scalp Massage", 200.0, "15 min"),
                                AddOn("a2", "Premium Hair Serum Treatment", 150.0, "5 min")
                            ),
                            userPointsBalance = 1250
                        )
                        newState.copy(priceBreakdown = calculatePrice(newState))
                    }
                    loadAvailability(targetShopId)
                }
            }
        }
    }

    private fun applyOffer(code: String) {
        val trimmed = code.trim().uppercase()
        if (trimmed.isBlank()) {
            _state.update { it.copy(couponError = "Please enter a valid coupon code") }
            return
        }
        viewModelScope.launch {
            val repositoryOffer = offersRepository.getOfferByCode(trimmed)
            val offer = repositoryOffer ?: Offer(
                id = "coupon_${trimmed.lowercase()}",
                code = trimmed,
                title = "$trimmed (2% Discount)",
                description = "2% instant discount applied on total amount",
                discountValue = 2.0,
                type = OfferType.PERCENTAGE,
                expiryDate = "2026-12-31"
            )


            _state.update {
                val newState = it.copy(
                    appliedOffer = offer,
                    couponError = null,
                    couponSuccessMessage = "Coupon '${offer.code}' applied! 2% discount saved."
                )
                newState.copy(priceBreakdown = calculatePrice(newState))
            }
        }
    }

    private fun calculatePrice(state: BookingState): PriceBreakdown {
        val base = when {
            state.selectedServices.isNotEmpty() -> state.selectedServices.sumOf { it.price }
            state.selectedPackage != null -> state.selectedPackage.price
            state.selectedService != null -> state.selectedService.price
            else -> 0.0
        }
        val addOns = state.availableAddOns
            .filter { state.selectedAddOns.contains(it.id) }
            .sumOf { it.price }
        val travel = if (state.isHomeService) 99.0 else 0.0

        var discount = 0.0
        state.appliedOffer?.let { offer ->
            discount = if (offer.type == OfferType.FLAT) {
                offer.discountValue
            } else {
                (base + addOns + travel) * (offer.discountValue / 100.0)
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

    private fun loadAvailability(targetShopId: String? = null) {
        val shopId = targetShopId ?: _state.value.shopId ?: "s1"
        val profId = _state.value.selectedProfessional?.id

        viewModelScope.launch {
            bookingRepository.getAvailability(shopId, profId).collect { availability ->
                _state.update { it.copy(availability = availability) }
            }
        }
    }

    private fun confirmBooking() {
        val currentState = _state.value
        val hasItem = currentState.selectedServices.isNotEmpty() || currentState.selectedService != null || currentState.selectedPackage != null
        if (!hasItem) return

        val finalDate = currentState.selectedDate ?: "21 May 2025"
        val finalTime = currentState.selectedTime ?: "10:30 AM"

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val bookingId = bookingRepository.bookSlot(
                serviceId = currentState.selectedServices.firstOrNull()?.id ?: currentState.selectedService?.id,
                packageId = currentState.selectedPackage?.id,
                professionalId = currentState.selectedProfessional?.id,
                date = finalDate,
                time = finalTime,
                isHomeService = currentState.isHomeService,
                paymentMethod = currentState.selectedPaymentMethod,
                totalAmount = currentState.priceBreakdown.total
            ) ?: "BK-${(1000..9999).random()}"

            _state.update {
                it.copy(
                    isLoading = false,
                    selectedDate = finalDate,
                    selectedTime = finalTime,
                    isBookingConfirmed = true,
                    lastConfirmedBookingId = bookingId
                )
            }
        }
    }
}
