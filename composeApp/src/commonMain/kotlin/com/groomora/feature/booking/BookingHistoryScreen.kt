package com.groomora.feature.booking

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.groomora.app.DependencyContainer
import com.groomora.design.*
import com.groomora.design.components.*
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingHistoryScreen(
    viewModel: BookingHistoryViewModel,
    onNavigateToReview: (String) -> Unit,
    onNavigateToRebook: (String) -> Unit,
    onNavigateToHome: () -> Unit = {},
    onNavigateToOffers: () -> Unit = {},
    onNavigateToWallet: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }

    val upcomingBookings = state.bookings.filter { it.status == BookingStatus.CONFIRMED || it.status == BookingStatus.IN_PROGRESS }
    val pastBookings = state.bookings.filter { it.status == BookingStatus.COMPLETED || it.status == BookingStatus.CANCELLED || it.status == BookingStatus.REFUNDED }

    // Reschedule Dialog state
    var newRescheduleDate by remember { mutableStateOf("Oct 27, 2024") }
    var newRescheduleTime by remember { mutableStateOf("02:00 PM") }

    // Cancel Dialog state
    var cancelReason by remember { mutableStateOf("Change of plans") }

    LaunchedEffect(Unit) {
        DependencyContainer.analyticsManager.logScreenView("booking_history_screen")
    }

    Scaffold(
        topBar = {
            GroomoraTopAppBar(
                title = "My Bookings",
                onBack = onBack
            )
        },
        bottomBar = {
            GroomoraBottomNav(
                currentRoute = "bookings",
                onHomeClick = onNavigateToHome,
                onBookingsClick = {},
                onOffersClick = onNavigateToOffers,
                onWalletClick = onNavigateToWallet,
                onProfileClick = onNavigateToProfile
            )
        }
    )
 { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .padding(padding)
        ) {
            // Notification / status message banner if present
            if (state.message != null) {
                Surface(
                    color = HoneyAmber.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    GroomoraBody(
                        text = state.message!!,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = HoneyAmber,
                indicator = { tabPositions ->
                    if (selectedTab < tabPositions.size) {
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = HoneyAmber
                        )
                    }
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            "Upcoming (${upcomingBookings.size})",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) HoneyAmber else MutedText
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            "Past & Cancelled (${pastBookings.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) HoneyAmber else MutedText
                        )
                    }
                )
            }

            if (state.isLoading) {
                GroomoraLoadingState()
            } else {
                val currentList = if (selectedTab == 0) upcomingBookings else pastBookings
                if (currentList.isEmpty()) {
                    GroomoraEmptyState(
                        title = if (selectedTab == 0) "No Upcoming Appointments" else "No Past Bookings",
                        message = "Book your next styling or grooming session with top barbers and salons.",
                        icon = Icons.Default.DateRange,
                        actionButtonText = "Explore Salons",
                        onActionClick = { onNavigateToRebook("ser1") }
                    )

                } else {
                    var pageLimit by remember(selectedTab) { mutableIntStateOf(5) }
                    val pagedBookings = currentList.take(pageLimit)
                    val listState = rememberLazyListState()

                    LaunchedEffect(listState, currentList.size, pageLimit) {
                        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                            .collect { lastIdx ->
                                if (lastIdx != null && lastIdx >= pagedBookings.size - 1 && pageLimit < currentList.size) {
                                    delay(200)
                                    pageLimit = (pageLimit + 5).coerceAtMost(currentList.size)
                                }
                            }
                    }

                    LazyColumn(
                        state = listState,
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        items(pagedBookings) { booking ->
                            BookingHistoryCard(
                                booking = booking,
                                onReschedule = { viewModel.onIntent(BookingHistoryIntent.InitiateReschedule(booking.id)) },
                                onCancel = { viewModel.onIntent(BookingHistoryIntent.InitiateCancel(booking.id)) },
                                onReview = { onNavigateToReview(booking.id) },
                                onRebook = { onNavigateToRebook("ser1") }
                            )
                        }

                        if (pageLimit < currentList.size) {
                            item {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    GroomoraOutlinedButton(
                                        text = "Load More Bookings (${pagedBookings.size}/${currentList.size})",
                                        onClick = {
                                            pageLimit = (pageLimit + 5).coerceAtMost(currentList.size)
                                        },
                                        height = 38.dp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    // Reusable GroomoraConfirmationDialog for Reschedule
    if (state.reschedulingBookingId != null) {
        val bookingId = state.reschedulingBookingId!!
        GroomoraConfirmationDialog(
            title = "Reschedule Appointment",
            onDismissRequest = { viewModel.onIntent(BookingHistoryIntent.DismissRescheduleDialog()) },
            onConfirm = {
                viewModel.onIntent(
                    BookingHistoryIntent.ConfirmReschedule(bookingId, newRescheduleDate, newRescheduleTime)
                )
            },
            confirmButtonText = "Confirm Reschedule",
            message = "Select a new date & time slot for $bookingId:"
        ) {
            Spacer(Modifier.height(8.dp))
            GroomoraOutlinedTextField(
                value = newRescheduleDate,
                onValueChange = { newRescheduleDate = it },
                label = "New Date (e.g. Oct 27, 2024)"
            )
            Spacer(Modifier.height(8.dp))
            GroomoraOutlinedTextField(
                value = newRescheduleTime,
                onValueChange = { newRescheduleTime = it },
                label = "New Time Slot (e.g. 02:00 PM)"
            )
        }
    }

    // Reusable GroomoraConfirmationDialog for Cancel
    if (state.cancellingBookingId != null) {
        val bookingId = state.cancellingBookingId!!
        GroomoraConfirmationDialog(
            title = "Cancel Appointment",
            onDismissRequest = { viewModel.onIntent(BookingHistoryIntent.DismissCancelDialog()) },
            onConfirm = {
                viewModel.onIntent(BookingHistoryIntent.ConfirmCancel(bookingId, cancelReason))
            },
            confirmButtonText = "Confirm Cancellation",
            dismissButtonText = "Keep Appointment",
            isDestructive = true,
            message = "Are you sure you want to cancel booking $bookingId? 100% refund will be credited back to your payment source."
        ) {
            Spacer(Modifier.height(8.dp))
            GroomoraOutlinedTextField(
                value = cancelReason,
                onValueChange = { cancelReason = it },
                label = "Reason for Cancellation"
            )
        }
    }
}

@Composable
fun BookingHistoryCard(
    booking: BookingRecord,
    onReschedule: () -> Unit,
    onCancel: () -> Unit,
    onReview: () -> Unit,
    onRebook: () -> Unit
) {
    GroomoraCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // Header Row: Booking ID + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GroomoraCaption(text = "ID: ${booking.id}")
                BookingStatusBadge(booking.status)
            }

            // Title & Shop
            GroomoraTitle(text = booking.title)
            GroomoraCaption(text = booking.shopName)

            if (booking.professionalName != null) {
                GroomoraCaption(
                    text = "Stylist: ${booking.professionalName}",
                    color = AppText,
                    fontWeight = FontWeight.Medium
                )
            }

            // Date, Time, Service Type
            Surface(
                color = WarmIvory,
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp), tint = HoneyAmber)
                        Spacer(Modifier.width(6.dp))
                        GroomoraBody(text = "${booking.date} • ${booking.time}", fontWeight = FontWeight.Medium)
                    }
                    Text(
                        text = if (booking.isHomeService) "Home Service" else "In-Shop",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (booking.isHomeService) TealGreen else Charcoal,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Refund Info if Cancelled
            if (booking.status == BookingStatus.CANCELLED || booking.status == BookingStatus.REFUNDED) {
                Surface(
                    color = Color(0xFFFFF3CD),
                    shape = MaterialTheme.shapes.small,
                    border = BorderStroke(1.dp, Color(0xFFFFEEBA))
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF856404), modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Column {
                            GroomoraCaption(
                                text = "Refund Status: ${if (booking.refundStatus == RefundStatus.REFUNDED) "Refund Credited (₹${booking.totalAmount.toInt()})" else "Refund Processing (1-2 business days)"}",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF856404)
                            )
                            if (booking.cancellationReason != null) {
                                GroomoraCaption(
                                    text = "Reason: ${booking.cancellationReason}",
                                    color = Color(0xFF856404)
                                )
                            }
                        }
                    }
                }
            }

            // Total Price & Actions
            HorizontalDivider(color = BorderGray)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.Center) {
                    GroomoraCaption(text = "Total Paid")
                    Spacer(Modifier.height(2.dp))
                    GroomoraPriceText(amount = booking.totalAmount)
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (booking.canReschedule) {
                        GroomoraOutlinedButton(
                            text = "Reschedule",
                            onClick = onReschedule,
                            height = 38.dp
                        )
                    }
                    if (booking.canCancel) {
                        GroomoraOutlinedButton(
                            text = "Cancel",
                            onClick = onCancel,
                            borderColor = ErrorRed,
                            contentColor = ErrorRed,
                            height = 38.dp
                        )
                    }
                    if (booking.canReview) {
                        GroomoraPrimaryButton(
                            text = "Review",
                            icon = Icons.Default.Star,
                            onClick = onReview,
                            height = 38.dp
                        )
                    }
                    if (booking.canRebook) {
                        GroomoraPrimaryButton(
                            text = "Rebook",
                            onClick = onRebook,
                            height = 38.dp
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BookingStatusBadge(status: BookingStatus) {
    val (bgColor, textColor, label) = when (status) {
        BookingStatus.CONFIRMED -> Triple(SuccessGreen.copy(alpha = 0.15f), SuccessGreen, "Confirmed")
        BookingStatus.IN_PROGRESS -> Triple(HoneyAmber.copy(alpha = 0.15f), HoneyAmber, "In Progress")
        BookingStatus.COMPLETED -> Triple(TealGreen.copy(alpha = 0.15f), TealGreen, "Completed")
        BookingStatus.CANCELLED -> Triple(ErrorRed.copy(alpha = 0.15f), ErrorRed, "Cancelled")
        BookingStatus.REFUNDED -> Triple(Color.Gray.copy(alpha = 0.15f), Color.DarkGray, "Refunded")
    }

    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = bgColor
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}

