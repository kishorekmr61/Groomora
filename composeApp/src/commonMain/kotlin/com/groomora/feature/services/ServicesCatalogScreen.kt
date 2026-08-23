package com.groomora.feature.services

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import kotlinx.coroutines.delay


import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.design.*
import com.groomora.design.components.*
import com.groomora.feature.shop.Service

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesCatalogScreen(
    initialCategory: String? = null,
    onFindSalons: (List<String>) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToBookings: () -> Unit,
    onNavigateToOffers: () -> Unit,
    onNavigateToWallet: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onBack: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(initialCategory ?: "All") }
    var searchQuery by remember { mutableStateOf("") }
    val selectedServices = remember { mutableStateListOf<Service>() }

    val allServices = remember {
        listOf(
            Service("ser1", "Luxury Hair Spa & Scalp Therapy", 599.0, "60 min", "Deep nourishment, steam & head relaxation massage", "hair"),
            Service("ser2", "Master Haircut & Style Consultation", 299.0, "30 min", "Precision haircut, hair wash and styling", "hair"),
            Service("ser3", "Royal Beard Trim & Hot Towel Shape", 199.0, "45 min", "Razor sharp line up with nourishing beard oil", "beard"),
            Service("ser4", "Face Clean Up & Charcoal Detan", 499.0, "40 min", "Pore unclogging, instant brightness & sun protection", "skin"),
            Service("ser5", "Full Body Swedish Massage", 1499.0, "60 min", "Aromatherapy relaxation with soothing warm essential oils", "spa"),
            Service("ser6", "Gel Nail Art & Polish Extension", 699.0, "45 min", "Long-lasting UV gel finish with custom nail art", "nails"),
            Service("ser7", "Hydrating Diamond Facial", 899.0, "50 min", "Cellular glow treatment with natural diamond dust mask", "skin"),
            Service("ser8", "Bridal Glow Pre-Wedding Ritual", 2499.0, "120 min", "Full body polish, organic facial & hair conditioning", "bridal"),
            Service("ser9", "Doorstep At-Home Haircut & Grooming", 399.0, "45 min", "Certified barber visits your home with sanitized toolkit", "home")
        )
    }

    val servicePhotos = mapOf(
        "ser1" to "https://images.unsplash.com/photo-1519699047748-de8e457a634e?w=400&q=80",
        "ser2" to "https://images.unsplash.com/photo-1599351431202-1e0f0137899a?w=400&q=80",
        "ser3" to "https://images.unsplash.com/photo-1621605815971-fbc98d665033?w=400&q=80",
        "ser4" to "https://images.unsplash.com/photo-1512290900672-1f02e71d4793?w=400&q=80",
        "ser5" to "https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=400&q=80",
        "ser6" to "https://images.unsplash.com/photo-1632345031435-8727f6897d53?w=400&q=80",
        "ser7" to "https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?w=400&q=80",
        "ser8" to "https://images.unsplash.com/photo-1583939003579-730e3918a45a?w=400&q=80",
        "ser9" to "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=400&q=80"
    )

    val categories = listOf("All", "Hair", "Beard", "Skin & Face", "Spa & Massage", "Nails", "Bridal", "Home Services")

    val filteredServices = allServices.filter { service ->
        val matchesCategory = when (selectedCategory) {
            "All" -> true
            "Hair" -> service.category == "hair"
            "Beard" -> service.category == "beard"
            "Skin & Face" -> service.category == "skin"
            "Spa & Massage" -> service.category == "spa"
            "Nails" -> service.category == "nails"
            "Bridal" -> service.category == "bridal"
            "Home Services" -> service.category == "home"
            else -> service.category.equals(selectedCategory, ignoreCase = true)
        }
        val matchesQuery = searchQuery.isBlank() || service.name.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    Scaffold(
        topBar = {
            GroomoraTopAppBar(
                title = "Explore Services",
                subtitle = "Pick services to find available salons",
                onBack = onBack
            )
        },
        bottomBar = {
            Column {
                if (selectedServices.isNotEmpty()) {
                    Surface(
                        color = Color.White,
                        shadowElevation = 12.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "${selectedServices.size} Service(s) Selected",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedText
                                )
                                Text(
                                    "₹${selectedServices.sumOf { it.price }.toInt()}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = HoneyAmber
                                )
                            }

                            GroomoraPrimaryButton(
                                text = "Find Available Salons ->",
                                onClick = {
                                    onFindSalons(selectedServices.map { it.id })
                                },
                                height = 44.dp
                            )
                        }
                    }
                }

                GroomoraBottomNav(
                    currentRoute = "home",
                    onHomeClick = onNavigateToHome,
                    onBookingsClick = onNavigateToBookings,
                    onOffersClick = onNavigateToOffers,
                    onWalletClick = onNavigateToWallet,
                    onProfileClick = onNavigateToProfile
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .padding(padding)
        ) {
            // Search Bar
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                GroomoraSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = "Search hair spa, facials, haircut..."
                )
            }

            // Categories horizontal row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories) { cat ->
                    GroomoraFilterChip(
                        label = cat,
                        isSelected = selectedCategory == cat,
                        onClick = { selectedCategory = cat }
                    )
                }
            }

            var pageLimit by remember { mutableIntStateOf(6) }
            val pagedServices = filteredServices.take(pageLimit)
            val listState = rememberLazyListState()

            LaunchedEffect(listState, filteredServices.size, pageLimit) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .collect { lastIdx ->
                        if (lastIdx != null && lastIdx >= pagedServices.size - 1 && pageLimit < filteredServices.size) {
                            delay(200)
                            pageLimit = (pageLimit + 4).coerceAtMost(filteredServices.size)
                        }
                    }
            }

            // Services catalog list
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(pagedServices) { service ->
                    val isSelected = selectedServices.any { it.id == service.id }

                    GroomoraCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(role = Role.Button, onClickLabel = "Select ${service.name}") {
                                if (isSelected) {
                                    selectedServices.removeAll { it.id == service.id }
                                } else {
                                    selectedServices.add(service)
                                }
                            },
                        borderColor = if (isSelected) HoneyAmber else BorderGray
                    ) {

                        Row(
                            modifier = Modifier
                                .padding(14.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(MaterialTheme.shapes.medium)
                                    .background(Charcoal),
                                contentAlignment = Alignment.Center
                            ) {
                                GroomoraImage(
                                    url = servicePhotos[service.id] ?: "https://images.unsplash.com/photo-1560066984-138dadb4c035?w=500&q=80",
                                    contentDescription = service.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = service.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = AppText,
                                    maxLines = 1
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = service.description ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedText,
                                    maxLines = 2
                                )

                                Spacer(Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${service.duration} • ₹${service.price.toInt()}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = HoneyAmber
                                    )
                                }
                            }

                            Spacer(Modifier.width(10.dp))

                            // Checkbox selection circle
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) HoneyAmber else Color.Transparent)
                                    .border(2.dp, if (isSelected) HoneyAmber else BorderGray, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
