package com.groomora.feature.discovery

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.groomora.app.DependencyContainer
import com.groomora.design.*
import com.groomora.design.components.*


@Composable
fun DiscoveryScreen(
    categoryId: String?,
    viewModel: DiscoveryViewModel,
    onNavigateToShop: (String) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var selectedViewIndex by remember { mutableIntStateOf(0) }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedShopForPreview by remember { mutableStateOf<Shop?>(null) }

    LaunchedEffect(categoryId) {
        DependencyContainer.analyticsManager.logScreenView("discovery_screen")
        if (categoryId != null) {
            viewModel.onIntent(DiscoveryIntent.FilterByCategory(categoryId))
        } else {
            viewModel.onIntent(DiscoveryIntent.LoadDiscoveryData)
        }
    }


    Scaffold(
        topBar = {
            GroomoraTopAppBar(
                title = "Nearby Places",
                onBack = onBack,
                actions = {
                    IconButton(onClick = { /* Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = AppText)
                    }
                }
            )
        },
        bottomBar = {
            GroomoraBottomNav(
                currentRoute = "discovery",
                onHomeClick = onBack,
                onBookingsClick = {},
                onOffersClick = {},
                onWalletClick = {},
                onProfileClick = {}
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .padding(padding)
        ) {
            // View Mode Toggle (Segmented Pill: List View vs Map View)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                GroomoraSegmentedControl(
                    options = listOf("List View", "Map View"),
                    selectedIndex = selectedViewIndex,
                    onOptionSelected = { selectedViewIndex = it }
                )
            }

            // Filter Chips Row (Reusable GroomoraFilterChip)
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf("All", "Salons", "Barbers", "Home Services")
                items(filters) { filter ->
                    GroomoraFilterChip(
                        label = filter,
                        isSelected = selectedFilter == filter,
                        onClick = {
                            selectedFilter = filter
                            when (filter) {
                                "Salons" -> viewModel.onIntent(DiscoveryIntent.FilterByCategory("hair"))
                                "Barbers" -> viewModel.onIntent(DiscoveryIntent.FilterByCategory("hair"))
                                "Home Services" -> viewModel.onIntent(DiscoveryIntent.FilterByCategory("home"))
                                else -> viewModel.onIntent(DiscoveryIntent.LoadDiscoveryData)
                            }
                        }
                    )
                }
            }

            if (state.isLoading) {
                GroomoraLoadingState()
            } else if (selectedViewIndex == 1) {
                // Map View
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFE5E0D8))
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            state.shops.take(4).forEachIndexed { index, shop ->
                                val isSelected = selectedShopForPreview?.id == shop.id
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = (index * 24).dp),
                                    horizontalArrangement = if (index % 2 == 0) Arrangement.Start else Arrangement.End
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isSelected) HoneyAmber else Charcoal,
                                        shadowElevation = 6.dp,
                                        modifier = Modifier.clickable { selectedShopForPreview = shop }
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                                            Spacer(Modifier.width(4.dp))
                                            Text(
                                                "${shop.name.take(12)} • ★ ${shop.rating}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    val previewShop = selectedShopForPreview ?: state.shops.firstOrNull()
                    previewShop?.let { shop ->
                        GroomoraCard(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp)
                                .fillMaxWidth(),
                            elevation = 8.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp).fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(shop.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                    GroomoraRatingBadge(
                                        rating = shop.rating,
                                        reviewCount = shop.reviewCount
                                    )
                                }
                                GroomoraPrimaryButton(
                                    text = "View Shop",
                                    onClick = { onNavigateToShop(shop.id) },
                                    height = 36.dp
                                )
                            }
                        }
                    }
                }
            } else if (state.shops.isEmpty()) {
                GroomoraEmptyState(
                    title = "No Places Found",
                    message = "We couldn't find any salons or barbers matching your filter.",
                    actionButtonText = "Show All Places",
                    onActionClick = {
                        selectedFilter = "All"
                        viewModel.onIntent(DiscoveryIntent.LoadDiscoveryData)
                    }
                )
            } else {
                // List View (Reusable ShopCard Component)
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.shops) { shop ->
                        ShopCard(
                            shop = shop,
                            onViewShop = { onNavigateToShop(shop.id) }
                        )
                    }
                }
            }
        }
    }
}

