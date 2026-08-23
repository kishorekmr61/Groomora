package com.groomora.feature.shop

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.groomora.design.*
import com.groomora.design.components.*
import com.groomora.feature.discovery.Professional

@Composable
fun ShopDetailsScreen(
    shopId: String,
    viewModel: ShopDetailsViewModel,
    onNavigateToBooking: (String) -> Unit,
    onNavigateToReviews: (String) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableStateOf("Overview") }
    var isSaved by remember { mutableStateOf(false) }

    LaunchedEffect(shopId) {
        com.groomora.app.DependencyContainer.analyticsManager.logScreenView("shop_details_screen")
        com.groomora.app.DependencyContainer.analyticsManager.logEvent("shop_details_view", mapOf("shop_id" to shopId))
        viewModel.onIntent(ShopDetailsIntent.LoadDetails(shopId))
    }


    Scaffold(
        topBar = {
            GroomoraTopAppBar(
                title = "Shop Details",
                onBack = onBack,
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = AppText)
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(16.dp)) {
                    GroomoraPrimaryButton(
                        text = "Book Appointment",
                        onClick = {
                            val firstServiceId = state.services.firstOrNull()?.id ?: "ser1"
                            onNavigateToBooking(firstServiceId)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    ) { padding ->
        if (state.isLoading) {
            GroomoraLoadingState()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarmIvory)
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                // 1. Photography Hero Image
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        GroomoraImage(
                            url = "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=1000&q=80",
                            contentDescription = state.shop?.name ?: "Salon Hero",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // 2. Shop Details Title & Hours
                item {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = state.shop?.name ?: "King's Barber Studio",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppText
                        )
                        Spacer(Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            GroomoraRatingBadge(
                                rating = state.shop?.rating ?: 4.8,
                                reviewCount = state.shop?.reviewCount ?: 245
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "• ${state.shop?.distance ?: "0.8 km"} • Koramangala",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MutedText
                            )
                        }
                        Spacer(Modifier.height(6.dp))
                        GroomoraStatusBadge(text = "Open • Closes 9:00 PM")
                    }
                }

                // 3. Action Buttons Row (Reusable GroomoraCircularActionButton)
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GroomoraCircularActionButton(
                            label = "Call",
                            icon = Icons.Default.Phone,
                            onClick = { /* Call */ }
                        )
                        GroomoraCircularActionButton(
                            label = "Direction",
                            icon = Icons.Default.Place,
                            onClick = { /* Direction */ }
                        )
                        GroomoraCircularActionButton(
                            label = "Share",
                            icon = Icons.Default.Share,
                            onClick = { /* Share */ }
                        )
                        GroomoraCircularActionButton(
                            label = if (isSaved) "Saved" else "Save",
                            icon = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            iconColor = if (isSaved) ErrorRed else AppText,
                            onClick = { isSaved = !isSaved }
                        )
                    }
                }

                // 4. Tabs Row (Overview, Services, Barbers, Gallery, Reviews)
                item {
                    Column {
                        HorizontalDivider(color = BorderGray, thickness = 1.dp)
                        ScrollableTabRow(
                            selectedTabIndex = listOf("Overview", "Services", "Barbers", "Gallery", "Reviews").indexOf(selectedTab),
                            containerColor = Color.White,
                            contentColor = HoneyAmber,
                            edgePadding = 16.dp
                        ) {
                            listOf("Overview", "Services", "Barbers", "Gallery", "Reviews").forEach { tab ->
                                Tab(
                                    selected = selectedTab == tab,
                                    onClick = { selectedTab = tab },
                                    text = {
                                        Text(
                                            text = tab,
                                            fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                                            color = if (selectedTab == tab) HoneyAmber else MutedText
                                        )
                                    }
                                )
                            }
                        }
                        HorizontalDivider(color = BorderGray, thickness = 1.dp)
                    }
                }

                // 5. Dynamic Tab Content
                when (selectedTab) {
                    "Overview" -> {
                        item {
                            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                                Text(
                                    text = "Popular Services",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AppText
                                )
                            }
                        }
                        val popularServices = listOf(
                            Service("ser1", "Haircut & Styling", 299.0, "30 min", null, "hair"),
                            Service("ser2", "Beard Trim & Shape", 199.0, "25 min", null, "beard"),
                            Service("ser3", "Deep Scalp Hair Spa", 599.0, "50 min", null, "hair"),
                            Service("ser4", "Detan Face Clean Up", 499.0, "40 min", null, "skin")
                        )
                        items(popularServices) { service ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                                ServiceItemCard(
                                    service = service,
                                    onSelect = { onNavigateToBooking(service.id) }
                                )
                            }
                        }

                        if (state.packages.isNotEmpty()) {
                            item {
                                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                                    Text(
                                        text = "Value Packages",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = AppText
                                    )
                                }
                            }
                            items(state.packages) { pkg ->
                                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                                    PackageItem(pkg = pkg, onBook = { onNavigateToBooking(pkg.id) })
                                }
                            }
                        }

                        // Amenities & Highlights (Reusable GroomoraAmenityChip)
                        item {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Amenities & Highlights", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(10.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    GroomoraAmenityChip("Sterilized Single-use Kits")
                                    GroomoraAmenityChip("AC Lounge")
                                    GroomoraAmenityChip("Free WiFi")
                                }
                            }
                        }
                    }

                    "Services" -> {
                        item {
                            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                                Text(
                                    text = "All Services & Treatments",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AppText
                                )
                            }
                        }
                        items(state.services) { service ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                                ServiceItemCard(
                                    service = service,
                                    onSelect = { onNavigateToBooking(service.id) }
                                )
                            }
                        }
                    }

                    "Barbers" -> {
                        item {
                            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                                Text(
                                    text = "Master Barbers & Stylists",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AppText
                                )
                            }
                        }
                        val sampleStylists = listOf(
                            Professional("p1", "Alex Rivera", "Master Barber", 4.9, "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=400&q=80", listOf("Fade Master", "Beard Detail")),
                            Professional("p2", "Sarah Chen", "Senior Stylist", 4.8, "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?w=400&q=80", listOf("Hair Spa", "Keratin")),
                            Professional("p3", "Arjun", "Senior Stylist", 4.8, "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&q=80", listOf("Beard Trim", "Cut")),
                            Professional("p4", "Vikram", "Expert Stylist", 4.7, "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=400&q=80", listOf("Face Spa", "Facials"))
                        )
                        items(sampleStylists) { pro ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                                StylistCard(
                                    professional = pro,
                                    onSelect = { onNavigateToBooking("ser1") }
                                )
                            }
                        }
                    }

                    "Gallery" -> {
                        item {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = "Salon & Work Gallery", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(12.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    GalleryPhotoCard("Styling Stations", "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=600&q=80", Modifier.weight(1f))
                                    GalleryPhotoCard("Haircut Artistry", "https://images.unsplash.com/photo-1622286342621-4bd786c2447c?w=600&q=80", Modifier.weight(1f))
                                }
                                Spacer(Modifier.height(10.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    GalleryPhotoCard("Spa & Facials", "https://images.unsplash.com/photo-1512290900672-1f02e71d4793?w=600&q=80", Modifier.weight(1f))
                                    GalleryPhotoCard("VIP Lounge", "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=600&q=80", Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    "Reviews" -> {
                        item {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = "Customer Reviews", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            GroomoraRatingBadge(rating = 4.8, reviewCount = 245)
                                            Spacer(Modifier.width(4.dp))
                                            Text(text = "out of 5.0", style = MaterialTheme.typography.bodyMedium, color = MutedText)
                                        }
                                    }
                                    GroomoraSecondaryButton(
                                        text = "Write Review",
                                        onClick = { onNavigateToReviews(shopId) },
                                        height = 36.dp
                                    )
                                }
                            }
                        }

                        items(listOf(
                            Triple("Rohit Sharma", "5.0 • 2 days ago", "Best fade haircut I've gotten in Koramangala. Master barber Alex took real care."),
                            Triple("Priya Nair", "5.0 • 1 week ago", "Excellent hair spa and facial session. Very clean and hygienic environment."),
                            Triple("Karthik V.", "4.5 • 2 weeks ago", "Great service, on time, and very polite stylists. Highly recommended!")
                        )) { (author, meta, comment) ->
                            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                                ReviewCard(
                                    author = author,
                                    meta = meta,
                                    comment = comment,
                                    rating = 5.0
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GalleryPhotoCard(title: String, imageUrl: String, modifier: Modifier = Modifier) {
    GroomoraCard(
        modifier = modifier.height(130.dp),
        shape = MaterialTheme.shapes.medium,
        containerColor = Charcoal
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            GroomoraImage(
                url = imageUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(Color.Black.copy(alpha = 0.6f))
                    .padding(vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(title, color = Color.White, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PackageItem(pkg: ServicePackage, onBook: () -> Unit) {
    GroomoraCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onBook),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(pkg.name, color = AppText, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("₹${pkg.price.toInt()}", color = HoneyAmber, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(4.dp))
            Text(pkg.description, color = MutedText, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Save ₹${(pkg.originalPrice - pkg.price).toInt()}",
                    color = SuccessGreen,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(8.dp))
                Text("• ${pkg.services.joinToString(", ")}", style = MaterialTheme.typography.bodySmall, color = MutedText)
            }
        }
    }
}
