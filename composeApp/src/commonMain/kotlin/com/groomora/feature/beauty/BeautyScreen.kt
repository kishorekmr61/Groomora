package com.groomora.feature.beauty

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.groomora.design.Champagne
import com.groomora.design.Charcoal
import com.groomora.design.WarmGold
import com.groomora.design.WarmIvory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeautyScreen(
    viewModel: BeautyViewModel,
    onNavigateToBooking: (String) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Beauty & Parlour Lounge") },
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
        if (state.isLoading && state.categories.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Charcoal)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarmIvory)
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Promotional Header
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Charcoal),
                        shape = MaterialTheme.shapes.large
                    ) {
                        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = Champagne, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("PREMIUM BEAUTY & SPA", color = Champagne, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            }
                            Text("Indulge in Expert Self-Care", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            Text("Certified beauticians, sterilized single-use kits, and premium organic brands at salon or your home.", color = WarmIvory.copy(alpha = 0.85f), style = MaterialTheme.typography.bodyMedium)
                        }

                    }
                }

                // Curated Packages Section
                if (state.packages.isNotEmpty()) {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("Value Pamper Packages", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            state.packages.forEach { pkg ->
                                BeautyPackageCard(pkg = pkg, onBook = { onNavigateToBooking("ser1") })
                            }
                        }
                    }
                }

                // Category Tabs
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Explore by Treatment", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(state.categories) { cat ->
                                val isSelected = cat.id == state.selectedCategoryId
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.onIntent(BeautyIntent.SelectCategory(cat.id)) },
                                    label = { Text(cat.name) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Charcoal,
                                        selectedLabelColor = Champagne,
                                        containerColor = Color.White,
                                        labelColor = Charcoal
                                    )
                                )
                            }
                        }
                    }
                }

                // Individual Services
                items(state.services) { service ->
                    BeautyServiceCard(
                        service = service,
                        onBook = { onNavigateToBooking(service.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun BeautyPackageCard(
    pkg: BeautyPackage,
    onBook: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = WarmGold.copy(alpha = 0.15f),
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        text = "SAVE ${pkg.savingsPercent}%",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Charcoal
                    )
                }
                Text(pkg.duration, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }

            Text(pkg.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(pkg.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                pkg.includedServices.forEach { item ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF2E7D5B), modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(item, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("₹${pkg.price.toInt()}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(8.dp))
                    Text("₹${pkg.originalPrice.toInt()}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                }
                Button(
                    onClick = onBook,
                    colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne)
                ) {
                    Text("Book Package")
                }
            }
        }
    }
}

@Composable
fun BeautyServiceCard(
    service: BeautyService,
    onBook: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(service.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                if (service.isPopular) {
                    Surface(
                        color = WarmGold.copy(alpha = 0.2f),
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(
                            "POPULAR",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )
                    }
                }
            }

            Text(service.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)

            if (service.benefits.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    service.benefits.forEach { benefit ->
                        Surface(
                            color = WarmIvory,
                            shape = MaterialTheme.shapes.extraSmall,
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f))
                        ) {
                            Text(benefit, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = Charcoal)
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("₹${service.price.toInt()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(service.duration, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
                Button(
                    onClick = onBook,
                    colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("Book Now")
                }
            }
        }
    }
}
