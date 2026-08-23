package com.groomora.feature.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.groomora.design.Champagne
import com.groomora.design.Charcoal
import com.groomora.design.WarmIvory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    serviceId: String,
    viewModel: BookingViewModel,
    onNavigateToAddresses: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(serviceId) {
        viewModel.onIntent(BookingIntent.Initialize(serviceId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Book Service") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Charcoal,
                    titleContentColor = Champagne,
                    navigationIconContentColor = Champagne
                )
            )
        },
        bottomBar = {
            if (!state.isBookingConfirmed) {
                Surface(shadowElevation = 8.dp) {
                    Button(
                        onClick = { viewModel.onIntent(BookingIntent.ConfirmBooking) },
                        modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp),
                        enabled = state.selectedDate != null && state.selectedTime != null && !state.isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne)
                    ) {
                        if (state.isLoading) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Champagne)
                        else Text("Confirm Booking • ₹${state.priceBreakdown.total}")
                    }
                }
            }
        }
    ) { padding ->
        if (state.isBookingConfirmed) {
            BookingSuccessView(onBack)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarmIvory)
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Service Summary
                item {
                    state.selectedService?.let { service ->
                        Text(service.name, style = MaterialTheme.typography.headlineSmall)
                    }
                }

                // Home Service Toggle
                item {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Home Service", style = MaterialTheme.typography.titleMedium)
                                Text("Available for this service", style = MaterialTheme.typography.bodySmall)
                            }
                            Switch(
                                checked = state.isHomeService,
                                onCheckedChange = { viewModel.onIntent(BookingIntent.ToggleHomeService(it)) },
                                colors = SwitchDefaults.colors(checkedThumbColor = Champagne, checkedTrackColor = Charcoal)
                            )
                        }
                        
                        if (state.isHomeService) {
                            Spacer(Modifier.height(12.dp))
                            Surface(
                                onClick = onNavigateToAddresses,
                                shape = MaterialTheme.shapes.medium,
                                color = Color.White,
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = Charcoal)
                                    Spacer(Modifier.width(12.dp))
                                    Text(
                                        text = "Select Delivery Address",
                                        modifier = Modifier.weight(1f),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text("Change", color = Color.Blue, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }

                // Add-ons
                if (state.availableAddOns.isNotEmpty()) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Enhance your experience", style = MaterialTheme.typography.titleMedium)
                            state.availableAddOns.forEach { addOn ->
                                AddOnItem(
                                    addOn = addOn,
                                    isSelected = state.selectedAddOns.contains(addOn.id),
                                    onToggle = { viewModel.onIntent(BookingIntent.ToggleAddOn(addOn.id)) }
                                )
                            }
                        }
                    }
                }

                // Date Selection
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Select Date", style = MaterialTheme.typography.titleMedium)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(state.availability) { availability ->
                                DateChip(
                                    date = availability.date,
                                    isSelected = state.selectedDate == availability.date,
                                    onClick = { viewModel.onIntent(BookingIntent.SelectDate(availability.date)) }
                                )
                            }
                        }
                    }
                }

                // Time Selection
                item {
                    if (state.selectedDate != null) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Select Time", style = MaterialTheme.typography.titleMedium)
                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val slots = state.availability.find { it.date == state.selectedDate }?.slots ?: emptyList()
                                slots.forEach { slot ->
                                    TimeChip(
                                        time = slot.time,
                                        isAvailable = slot.isAvailable,
                                        isSelected = state.selectedTime == slot.time,
                                        onClick = { viewModel.onIntent(BookingIntent.SelectTime(slot.time)) }
                                    )
                                }
                            }
                        }
                    }
                }

                // Price Breakdown
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Bill Summary", style = MaterialTheme.typography.titleMedium)
                        PriceRow("Item Total", state.priceBreakdown.basePrice)
                        if (state.priceBreakdown.addOnsTotal > 0) PriceRow("Add-ons", state.priceBreakdown.addOnsTotal)
                        if (state.priceBreakdown.travelFee > 0) PriceRow("Travel Fee", state.priceBreakdown.travelFee)
                        if (state.priceBreakdown.discount > 0) PriceRow("Discount", -state.priceBreakdown.discount, Color(0xFF2E7D5B))
                        if (state.priceBreakdown.loyaltyRedemption > 0) PriceRow("Loyalty Redeemed", -state.priceBreakdown.loyaltyRedemption, Color(0xFF2E7D5B))
                        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Amount to Pay", style = MaterialTheme.typography.titleLarge)
                            Text("₹${state.priceBreakdown.total}", style = MaterialTheme.typography.titleLarge)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddOnItem(addOn: AddOn, isSelected: Boolean, onToggle: () -> Unit) {
    Surface(
        onClick = onToggle,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) Charcoal.copy(alpha = 0.05f) else Color.White,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) Charcoal else Color.LightGray)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(addOn.name, style = MaterialTheme.typography.bodyLarge)
                Text(addOn.duration, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Text("₹${addOn.price}", style = MaterialTheme.typography.titleSmall)
        }
    }
}

@Composable
fun PriceRow(label: String, amount: Double, color: Color = Charcoal) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        Text("${if (amount < 0) "- " else ""}₹${if (amount < 0) -amount else amount}", style = MaterialTheme.typography.bodyMedium, color = color)
    }
}

@Composable
fun DateChip(date: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) Charcoal else Color.White,
        border = if (isSelected) null else BoxCombinedBorder,
        shadowElevation = if (isSelected) 4.dp else 0.dp
    ) {
        Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), contentAlignment = Alignment.Center) {
            Text(date, color = if (isSelected) Champagne else Charcoal)
        }
    }
}

@Composable
fun TimeChip(time: String, isAvailable: Boolean, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = if (isAvailable) onClick else ({}),
        shape = MaterialTheme.shapes.small,
        color = when {
            isSelected -> Charcoal
            !isAvailable -> Color.LightGray.copy(alpha = 0.5f)
            else -> Color.White
        },
        border = if (isSelected || !isAvailable) null else BoxCombinedBorder
    ) {
        Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Text(
                time,
                color = if (isSelected) Champagne else if (isAvailable) Charcoal else Color.Gray,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun BookingSuccessView(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(80.dp), tint = Color(0xFF2E7D5B))
        Spacer(Modifier.height(16.dp))
        Text("Booking Confirmed!", style = MaterialTheme.typography.headlineMedium)
        Text("We've sent the details to your phone.", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne)
        ) {
            Text("Back to Home")
        }
    }
}

private val BoxCombinedBorder = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        content = { content() }
    )
}
