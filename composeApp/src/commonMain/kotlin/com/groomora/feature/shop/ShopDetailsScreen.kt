package com.groomora.feature.shop

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.groomora.design.Champagne
import com.groomora.design.Charcoal
import com.groomora.design.WarmIvory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopDetailsScreen(
    shopId: String,
    viewModel: ShopDetailsViewModel,
    onNavigateToBooking: (String) -> Unit,
    onNavigateToReviews: (String) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(shopId) {
        viewModel.onIntent(ShopDetailsIntent.LoadDetails(shopId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.shop?.name ?: "Shop Details") },
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
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Charcoal)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarmIvory)
                    .padding(padding)
            ) {
                state.shop?.let { shop ->
                    item {
                        ShopHeader(shop, onReviewClick = { onNavigateToReviews(shop.id) })
                    }
                }

                if (state.packages.isNotEmpty()) {
                    item {
                        Text(
                            "Special Packages",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    items(state.packages) { pkg ->
                        PackageItem(pkg)
                    }
                }

                item {
                    Text(
                        "Services",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                items(state.services) { service ->
                    ServiceItem(service, onBook = { onNavigateToBooking(service.id) })
                }
            }
        }
    }
}

@Composable
fun ShopHeader(shop: com.groomora.feature.discovery.Shop, onReviewClick: () -> Unit) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text("Shop Image", color = Color.Gray)
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(shop.name, style = MaterialTheme.typography.headlineMedium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onReviewClick() }.padding(vertical = 4.dp)
            ) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107))
                Text(
                    text = "${shop.rating} (${shop.reviewCount} reviews)",
                    modifier = Modifier.padding(start = 4.dp),
                    color = Charcoal,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    " • View all",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.Blue,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
            Text(shop.address, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}

@Composable
fun PackageItem(pkg: ServicePackage) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Charcoal)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(pkg.name, color = Champagne, style = MaterialTheme.typography.titleMedium)
                Text("₹${pkg.price}", color = Champagne, style = MaterialTheme.typography.titleMedium)
            }
            Text(pkg.description, color = Color.White, style = MaterialTheme.typography.bodySmall)
            Text(
                "Save ₹${pkg.originalPrice - pkg.price}",
                color = Color(0xFF2E7D5B),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
fun ServiceItem(service: Service, onBook: () -> Unit) {
    ListItem(
        headlineContent = { Text(service.name) },
        supportingContent = { Text("${service.duration} • ${service.description ?: ""}") },
        trailingContent = {
            Button(
                onClick = onBook,
                colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne)
            ) {
                Text("Book ₹${service.price}")
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.LightGray)
}
