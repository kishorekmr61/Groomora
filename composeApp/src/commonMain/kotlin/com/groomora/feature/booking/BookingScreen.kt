package com.groomora.feature.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.design.*
import com.groomora.design.components.*
import com.groomora.feature.discovery.Professional
import com.groomora.feature.shop.Service

@Composable
fun BookingScreen(
    serviceId: String? = null,
    packageId: String? = null,
    serviceIds: List<String> = emptyList(),
    shopId: String? = null,
    viewModel: BookingViewModel,
    onNavigateToAddresses: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val hasPreselectedServices = serviceIds.isNotEmpty()
    val initialStep = when {
        packageId != null -> 3
        hasPreselectedServices -> 2
        else -> 1
    }

    var currentStep by remember(packageId, serviceIds) {
        mutableIntStateOf(initialStep)
    }

    LaunchedEffect(serviceId, packageId, serviceIds, shopId) {
        com.groomora.app.DependencyContainer.analyticsManager.logScreenView("booking_screen")
        viewModel.onIntent(
            BookingIntent.Initialize(
                serviceId = serviceId,
                packageId = packageId,
                serviceIds = serviceIds,
                shopId = shopId
            )
        )
    }

    LaunchedEffect(currentStep) {
        val stepName = when (currentStep) {
            1 -> "select_service"
            2 -> "select_professional"
            3 -> "select_date_time"
            else -> "confirm_booking"
        }
        com.groomora.app.DependencyContainer.analyticsManager.logFunnelStep("booking_funnel", currentStep, stepName)
    }

    Scaffold(
        topBar = {
            if (!state.isBookingConfirmed) {
                GroomoraTopAppBar(
                    title = when (currentStep) {
                        1 -> "1. Select Services"
                        2 -> "2. Select Professional"
                        3 -> "3. Select Date & Time"
                        else -> "4. Confirm Booking"
                    },
                    subtitle = when {
                        state.selectedPackage != null -> "Package: ${state.selectedPackage?.name}"
                        state.shopName != null -> "Salon: ${state.shopName}"
                        else -> null
                    },
                    onBack = {
                        if (currentStep > initialStep) {
                            currentStep -= 1
                        } else {
                            onBack()
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (!state.isBookingConfirmed) {
                Surface(
                    color = Color.White,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        val canProceed = when (currentStep) {
                            1 -> state.selectedServices.isNotEmpty() || state.selectedService != null || state.selectedPackage != null
                            2 -> state.selectedProfessional != null
                            3 -> true
                            4 -> true
                            else -> true
                        }

                        GroomoraPrimaryButton(
                            text = when (currentStep) {
                                1 -> if (state.selectedServices.size > 1) "Continue with ${state.selectedServices.size} Services" else "Continue to Stylist"
                                4 -> "Confirm Booking • ₹${state.priceBreakdown.total.toInt()}"
                                else -> "Continue"
                            },
                            enabled = canProceed,
                            onClick = {
                                if (currentStep < 4) {
                                    currentStep += 1
                                } else {
                                    viewModel.onIntent(BookingIntent.ConfirmBooking)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            isLoading = state.isLoading
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (state.isBookingConfirmed) {
            BookingConfirmationSuccessView(
                confirmationId = state.lastConfirmedBookingId ?: "BK-${(1000..9999).random()}",
                state = state,
                onViewHistory = onNavigateToHistory,
                onBackToHome = onBack
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarmIvory)
                    .padding(padding)
            ) {
                BookingStepProgressRow(
                    currentStep = currentStep,
                    isPackage = packageId != null,
                    hasPreselectedServices = hasPreselectedServices
                )

                when (currentStep) {
                    1 -> Step1SelectMultipleServicesView(
                        state = state,
                        onToggleService = { service ->
                            viewModel.onIntent(BookingIntent.ToggleService(service))
                        }
                    )
                    2 -> Step2SelectProfessionalView(
                        state = state,
                        onSelectProfessional = { pro ->
                            viewModel.onIntent(BookingIntent.SelectProfessional(pro))
                            currentStep = 3
                        }
                    )
                    3 -> Step3SelectDateTimeView(
                        state = state,
                        onSelectDate = { date -> viewModel.onIntent(BookingIntent.SelectDate(date)) },
                        onSelectSlot = { slot ->
                            viewModel.onIntent(BookingIntent.SelectTime(slot))
                            currentStep = 4
                        }
                    )
                    4 -> Step4ConfirmBookingView(
                        state = state,
                        viewModel = viewModel,
                        onNavigateToAddresses = onNavigateToAddresses
                    )
                }
            }
        }
    }
}

@Composable
fun BookingStepProgressRow(
    currentStep: Int,
    isPackage: Boolean = false,
    hasPreselectedServices: Boolean = false
) {
    val steps = when {
        isPackage -> listOf("Date/Time", "Confirm")
        hasPreselectedServices -> listOf("Stylist", "Date/Time", "Confirm")
        else -> listOf("Services", "Stylist", "Date/Time", "Confirm")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, stepName ->
            val stepNumber = index + 1
            val mappedCurrent = when {
                isPackage -> currentStep - 2
                hasPreselectedServices -> currentStep - 1
                else -> currentStep
            }
            val isActive = stepNumber == mappedCurrent
            val isCompleted = stepNumber < mappedCurrent

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isActive -> HoneyAmber
                                isCompleted -> DeepIndigo
                                else -> Color(0xFFDCD8CF)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(Icons.Default.Check, contentDescription = "Completed", tint = Color.White, modifier = Modifier.size(14.dp))
                    } else {
                        Text("$stepNumber", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stepName,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isActive || isCompleted) AppText else MutedText,
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

// ----------------- STEP 1: MULTIPLE SERVICE SELECTION -----------------
@Composable
fun Step1SelectMultipleServicesView(
    state: BookingState,
    onToggleService: (Service) -> Unit
) {
    val sampleServices = listOf(
        Service("ser1", "Hair Spa & Scalp Therapy", 599.0, "60 min", "Deep conditioning & scalp massage", "hair"),
        Service("ser2", "Haircut & Styling", 299.0, "30 min", "Master cut, wash & blow dry", "hair"),
        Service("ser3", "Beard Trim & Shape", 199.0, "45 min", "Razor sharp beard detailing", "beard"),
        Service("ser4", "Face Clean Up & Detan", 499.0, "40 min", "Instant brightening & pore cleanse", "skin")
    )
    val servicePhotos = mapOf(
        "ser1" to "https://images.unsplash.com/photo-1519699047748-de8e457a634e?w=400&q=80",
        "ser2" to "https://images.unsplash.com/photo-1599351431202-1e0f0137899a?w=400&q=80",
        "ser3" to "https://images.unsplash.com/photo-1621605815971-fbc98d665033?w=400&q=80",
        "ser4" to "https://images.unsplash.com/photo-1512290900672-1f02e71d4793?w=400&q=80"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Select Services (Multiple)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (state.selectedServices.isNotEmpty()) {
                    Text(
                        "${state.selectedServices.size} selected",
                        style = MaterialTheme.typography.labelMedium,
                        color = HoneyAmber,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        items(sampleServices) { service ->
            val isSelected = state.selectedServices.any { it.id == service.id } || state.selectedService?.id == service.id
            ServiceItemCard(
                service = service,
                isSelected = isSelected,
                onSelect = { onToggleService(service) },
                showRadio = true,
                imageUrl = servicePhotos[service.id]
            )
        }
    }
}

// ----------------- STEP 2: SELECT PROFESSIONAL -----------------
@Composable
fun Step2SelectProfessionalView(
    state: BookingState,
    onSelectProfessional: (Professional) -> Unit
) {
    val sampleStylists = listOf(
        Professional("pro1", "Arjun", "Senior Stylist", 4.8, "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&q=80", listOf("Master Haircut", "Hair Spa")),
        Professional("pro2", "Rahul", "Stylist", 4.6, "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&q=80", listOf("Fade", "Beard Trim")),
        Professional("pro3", "Vikram", "Expert Stylist", 4.7, "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=400&q=80", listOf("Hair Spa", "Facials"))
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Select Professional", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        items(sampleStylists) { pro ->
            StylistCard(
                professional = pro,
                isSelected = state.selectedProfessional?.id == pro.id,
                onSelect = { onSelectProfessional(pro) },
                showRadio = true
            )
        }
    }
}

// ----------------- STEP 3: SELECT DATE & TIME -----------------
@Composable
fun Step3SelectDateTimeView(
    state: BookingState,
    onSelectDate: (String) -> Unit,
    onSelectSlot: (String) -> Unit
) {
    var selectedDay by remember { mutableIntStateOf(21) }
    var selectedTime by remember { mutableStateOf(state.selectedTime ?: "10:30 AM") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GroomoraCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { /* Prev */ }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous Month")
                        }
                        Text("May 2025", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { /* Next */ }) {
                            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Month")
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { day ->
                            Text(day, style = MaterialTheme.typography.labelSmall, color = MutedText, textAlign = TextAlign.Center, modifier = Modifier.width(36.dp))
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(
                            listOf(1, 2, 3, 4, 5, 6, 7),
                            listOf(8, 9, 10, 11, 12, 13, 14),
                            listOf(15, 16, 17, 18, 19, 20, 21),
                            listOf(22, 23, 24, 25, 26, 27, 28)
                        ).forEach { week ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                                week.forEach { d ->
                                    val isDaySelected = selectedDay == d
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape)
                                            .background(if (isDaySelected) DeepIndigo else Color.Transparent)
                                            .clickable(role = Role.Button, onClickLabel = "Select May $d") {
                                                selectedDay = d
                                                onSelectDate("$d May 2025")
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            "$d",
                                            color = if (isDaySelected) Color.White else AppText,
                                            fontWeight = if (isDaySelected) FontWeight.Bold else FontWeight.Normal,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Text("Available Time Slots", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            val slots = listOf("09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                slots.take(3).forEach { slot ->
                    TimeSlotPill(slot = slot, isSelected = selectedTime == slot, onSelect = {
                        selectedTime = slot
                        onSelectSlot(slot)
                    })
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                slots.drop(3).forEach { slot ->
                    TimeSlotPill(slot = slot, isSelected = selectedTime == slot, onSelect = {
                        selectedTime = slot
                        onSelectSlot(slot)
                    })
                }
            }
        }
    }
}

@Composable
fun RowScope.TimeSlotPill(slot: String, isSelected: Boolean, onSelect: () -> Unit) {
    Surface(
        modifier = Modifier
            .weight(1f)
            .defaultMinSize(minHeight = 48.dp)
            .clickable(role = Role.Button, onClickLabel = "Select time $slot", onClick = onSelect),
        shape = CircleShape,
        color = if (isSelected) DeepIndigo else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, BorderGray)
    ) {
        Box(modifier = Modifier.padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
            Text(
                slot,
                color = if (isSelected) Color.White else AppText,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

// ----------------- STEP 4: CONFIRM BOOKING & COUPON -----------------
@Composable
fun Step4ConfirmBookingView(
    state: BookingState,
    viewModel: BookingViewModel,
    onNavigateToAddresses: () -> Unit
) {
    var couponInput by remember { mutableStateOf("") }

    val serviceNames = when {
        state.selectedServices.isNotEmpty() -> state.selectedServices.joinToString(", ") { it.name }
        state.selectedPackage != null -> state.selectedPackage.name
        else -> state.selectedService?.name ?: "Hair Spa & Grooming"
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            GroomoraCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Appointment Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    HorizontalDivider(color = BorderGray)

                    if (state.shopName != null) {
                        GroomoraSummaryRow("Salon / Venue", state.shopName)
                    }
                    GroomoraSummaryRow("Professional", state.selectedProfessional?.name ?: "Elena Vance (Specialist)")
                    GroomoraSummaryRow("Date", state.selectedDate ?: "21 May 2025")
                    GroomoraSummaryRow("Time", state.selectedTime ?: "10:30 AM")
                    GroomoraSummaryRow("Venue Type", if (state.isHomeService) "Home Service (+₹99)" else "In-Shop Salon")

                    HorizontalDivider(color = BorderGray)
                    Text("Selected Services & Items", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = AppText)

                    if (state.selectedPackage != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(state.selectedPackage.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = AppText)
                                Text(state.selectedPackage.description, style = MaterialTheme.typography.labelSmall, color = MutedText)
                            }
                            Text("₹${state.selectedPackage.price.toInt()}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = HoneyAmber)
                        }
                    } else if (state.selectedServices.isNotEmpty()) {
                        state.selectedServices.forEach { s ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(s.name, style = MaterialTheme.typography.bodyMedium, color = AppText)
                                Text("₹${s.price.toInt()}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = AppText)
                            }
                        }
                    } else if (state.selectedService != null) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(state.selectedService.name, style = MaterialTheme.typography.bodyMedium, color = AppText)
                            Text("₹${state.selectedService.price.toInt()}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = AppText)
                        }
                    }

                    HorizontalDivider(color = BorderGray)
                    GroomoraSummaryRow("Base Amount", "₹${state.priceBreakdown.basePrice.toInt()}")
                    if (state.priceBreakdown.addOnsTotal > 0) {
                        GroomoraSummaryRow("Add-ons", "+₹${state.priceBreakdown.addOnsTotal.toInt()}")
                    }
                    if (state.priceBreakdown.travelFee > 0) {
                        GroomoraSummaryRow("Home Delivery Travel Fee", "+₹${state.priceBreakdown.travelFee.toInt()}")
                    }
                    if (state.priceBreakdown.discount > 0) {
                        GroomoraSummaryRow("Coupon Discount", "-₹${state.priceBreakdown.discount.toInt()}", valueColor = SuccessGreen, isBold = true)
                    }
                    HorizontalDivider(color = BorderGray)
                    GroomoraSummaryRow("Total Payable", "₹${state.priceBreakdown.total.toInt()}", isBold = true, valueColor = HoneyAmber)
                }
            }
        }

        // Coupon Code Card
        item {
            GroomoraCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Have a Coupon Code?", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Text("Enter any promo code to get flat 2% instant discount", style = MaterialTheme.typography.labelSmall, color = MutedText)
                    Spacer(Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedTextField(
                            value = couponInput,
                            onValueChange = { couponInput = it },
                            placeholder = { Text("e.g. GROOM2, SAVE2", color = MutedText, style = MaterialTheme.typography.bodyMedium) },
                            singleLine = true,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White,
                                focusedBorderColor = HoneyAmber,
                                unfocusedBorderColor = BorderGray
                            )
                        )
                        Button(
                            onClick = {
                                viewModel.onIntent(BookingIntent.ApplyOfferCode(couponInput.trim().uppercase()))
                            },
                            enabled = couponInput.trim().isNotEmpty(),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Charcoal,
                                contentColor = Champagne,
                                disabledContainerColor = Color(0xFFE8E5DD),
                                disabledContentColor = Color.Gray
                            )
                        ) {
                            Text("Apply", fontWeight = FontWeight.Bold)
                        }
                    }

                    state.appliedOffer?.let { offer ->
                        Spacer(Modifier.height(10.dp))
                        Surface(
                            shape = CircleShape,
                            color = SuccessGreen.copy(alpha = 0.12f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(6.dp))
                                    Text(
                                        text = "${offer.title} (-₹${state.priceBreakdown.discount.toInt()})",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = SuccessGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = "Remove",
                                    color = ErrorRed,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        viewModel.onIntent(BookingIntent.RemoveOfferCode)
                                        couponInput = ""
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


// ----------------- SUCCESSFUL BOOKING SCREEN -----------------
@Composable
fun BookingConfirmationSuccessView(
    confirmationId: String,
    state: BookingState,
    onViewHistory: () -> Unit,
    onBackToHome: () -> Unit
) {
    val serviceNames = when {
        state.selectedServices.isNotEmpty() -> state.selectedServices.joinToString(", ") { it.name }
        state.selectedPackage != null -> state.selectedPackage.name
        else -> state.selectedService?.name ?: "Salon & Grooming Session"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        GroomoraCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(SuccessGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Booking Confirmed", tint = Color.White, modifier = Modifier.size(44.dp))
                }

                Text("Booking Confirmed!", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = AppText)
                Text(
                    "Your appointment has been successfully scheduled. We've sent the details to your notifications.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MutedText,
                    textAlign = TextAlign.Center
                )

                Surface(
                    color = WarmIvory,
                    shape = MaterialTheme.shapes.medium,
                    border = BorderStroke(1.dp, BorderGray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        GroomoraSummaryRow("Booking ID", confirmationId, isBold = true)
                        if (state.shopName != null) {
                            GroomoraSummaryRow("Salon / Venue", state.shopName)
                        }
                        GroomoraSummaryRow("Service", serviceNames)
                        GroomoraSummaryRow("Date & Time", "${state.selectedDate ?: "21 May 2025"} • ${state.selectedTime ?: "10:30 AM"}")
                        GroomoraSummaryRow("Stylist", state.selectedProfessional?.name ?: "Elena Vance")
                        GroomoraSummaryRow("Total Paid", "₹${state.priceBreakdown.total.toInt()}", isBold = true, valueColor = HoneyAmber)
                    }
                }

                Spacer(Modifier.height(6.dp))

                GroomoraPrimaryButton(
                    text = "View in My Bookings",
                    onClick = onViewHistory,
                    modifier = Modifier.fillMaxWidth()
                )

                GroomoraOutlinedButton(
                    text = "Back to Home",
                    onClick = onBackToHome,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
