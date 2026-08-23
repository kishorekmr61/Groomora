package com.groomora.feature.offers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.groomora.design.Champagne
import com.groomora.design.Charcoal
import com.groomora.design.WarmIvory
import com.groomora.design.components.GroomoraBottomNav
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersScreen(
    viewModel: OffersViewModel,
    onNavigateToHome: () -> Unit = {},
    onNavigateToBookings: () -> Unit = {},
    onNavigateToOffers: () -> Unit = {},
    onNavigateToWallet: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Available Offers") },
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
            GroomoraBottomNav(
                currentRoute = "offers",
                onHomeClick = onNavigateToHome,
                onBookingsClick = onNavigateToBookings,
                onOffersClick = onNavigateToOffers,
                onWalletClick = onNavigateToWallet,
                onProfileClick = onNavigateToProfile
            )
        }
    )
 { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Charcoal)
            }
        } else {
            var pageLimit by remember { mutableIntStateOf(4) }
            val pagedOffers = state.availableOffers.take(pageLimit)
            val listState = rememberLazyListState()

            LaunchedEffect(listState, state.availableOffers.size, pageLimit) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .collect { lastIdx ->
                        if (lastIdx != null && lastIdx >= pagedOffers.size - 1 && pageLimit < state.availableOffers.size) {
                            delay(200)
                            pageLimit = (pageLimit + 4).coerceAtMost(state.availableOffers.size)
                        }
                    }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarmIvory)
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(pagedOffers) { offer ->
                    OfferCard(offer = offer)
                }

                if (pageLimit < state.availableOffers.size) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {
                                    pageLimit = (pageLimit + 4).coerceAtMost(state.availableOffers.size)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne)
                            ) {
                                Text("Load More Offers (${pagedOffers.size}/${state.availableOffers.size})")
                            }
                        }
                    }
                }
            }
        }


    }
}

@Composable
fun OfferCard(offer: Offer) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = Charcoal,
                    contentColor = Champagne
                ) {
                    Text(
                        text = offer.code,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
                Text(
                    text = "Expires: ${offer.expiryDate}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
            
            Spacer(Modifier.height(12.dp))
            Text(offer.title, style = MaterialTheme.typography.titleLarge)
            Text(offer.description, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
            
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(Modifier.height(8.dp))
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.Gray
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = if (offer.minOrderValue > 0) "Min. order ₹${offer.minOrderValue}" else "No minimum order",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Gray
                )
            }
        }
    }
}
