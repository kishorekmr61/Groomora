package com.groomora.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.groomora.design.Charcoal
import com.groomora.design.Champagne
import com.groomora.design.WarmIvory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToDiscovery: (String?) -> Unit,
    onNavigateToOffers: () -> Unit,
    onNavigateToProducts: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToLoyalty: () -> Unit,
    onNavigateToBridal: () -> Unit,
    onNavigateToHomeService: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("GROOMORA", style = MaterialTheme.typography.titleMedium)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.LocationOn,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Champagne
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                text = state.location?.address?.city ?: "Locating...",
                                style = MaterialTheme.typography.labelSmall,
                                color = Champagne
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToNotifications) {
                        BadgedBox(
                            badge = {
                                // Mock badge for notifications
                                Badge(containerColor = Color.Red) { Text("3") }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Champagne)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Charcoal,
                    titleContentColor = Champagne
                )
            )
        },
        bottomBar = {
            HomeBottomNavigation(
                onHomeClick = {},
                onDiscoveryClick = { onNavigateToDiscovery(null) },
                onProductsClick = onNavigateToProducts,
                onLoyaltyClick = onNavigateToLoyalty,
                onProfileClick = onNavigateToProfile
            )
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Charcoal)
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize()
                    .background(WarmIvory)
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Promotional Banner
                state.banners.firstOrNull()?.let { banner ->
                    Card(
                        modifier = Modifier.fillMaxWidth().height(160.dp),
                        colors = CardDefaults.cardColors(containerColor = Charcoal),
                        onClick = onNavigateToOffers
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp).fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(banner.title, color = Champagne, style = MaterialTheme.typography.headlineSmall)
                            Text(banner.description, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = onNavigateToOffers,
                                colors = ButtonDefaults.buttonColors(containerColor = Champagne, contentColor = Charcoal)
                            ) {
                                Text(banner.ctaLabel)
                            }
                        }
                    }
                }

                // Categories
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Categories", style = MaterialTheme.typography.titleLarge)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(state.categories) { category ->
                            CategoryItem(
                                label = category.label,
                                onClick = {
                                    when (category.id) {
                                        "bridal" -> onNavigateToBridal()
                                        "home" -> onNavigateToHomeService()
                                        else -> onNavigateToDiscovery(category.id)
                                    }
                                }
                            )
                        }
                    }
                }

                // Quick Actions
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onNavigateToDiscovery(null) }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Explore Nearby Shops", style = MaterialTheme.typography.titleMedium)
                        Text("Find the best barbers and stylists in ${state.location?.address?.city ?: "your area"}.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(label: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun HomeBottomNavigation(
    onHomeClick: () -> Unit,
    onDiscoveryClick: () -> Unit,
    onProductsClick: () -> Unit,
    onLoyaltyClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    NavigationBar(
        containerColor = Charcoal,
        contentColor = Champagne
    ) {
        NavigationBarItem(
            selected = true,
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Charcoal,
                selectedTextColor = Champagne,
                indicatorColor = Champagne,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onDiscoveryClick,
            icon = { Icon(Icons.Default.Search, contentDescription = "Discover") },
            label = { Text("Discover") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onProductsClick,
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Shop") },
            label = { Text("Shop") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onLoyaltyClick,
            icon = { Icon(Icons.Default.Star, contentDescription = "Loyalty") },
            label = { Text("Rewards") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onProfileClick,
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
            label = { Text("Account") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray
            )
        )
    }
}
