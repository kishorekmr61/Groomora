package com.groomora.feature.discovery

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.design.*
import com.groomora.design.components.*

@Composable
fun GenderSelectionScreen(
    onNavigateToGenderServices: (String) -> Unit,
    onNavigateToDiscovery: (String?) -> Unit,
    onBack: () -> Unit
) {
    LaunchedEffect(Unit) {
        com.groomora.app.DependencyContainer.analyticsManager.logScreenView("gender_selection_screen")
    }

    Scaffold(

        topBar = {
            GroomoraTopAppBar(
                title = "Select Gender",
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Choose your category",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AppText
            )

            // For Men Card with Live Photography
            GroomoraCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                onClick = { onNavigateToGenderServices("men") },
                containerColor = Charcoal,
                borderColor = null,
                elevation = 4.dp
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    GroomoraImage(
                        url = "https://images.unsplash.com/photo-1621605815971-fbc98d665033?w=800&q=80",
                        contentDescription = "Men Grooming",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Gradient background overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Charcoal.copy(alpha = 0.95f),
                                        Charcoal.copy(alpha = 0.7f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "For Men",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Haircuts, Beard, Grooming\n& More",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }

                        // Gold circle arrow
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(HoneyAmber),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Select Men",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            // For Women Card with Live Photography
            GroomoraCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                onClick = { onNavigateToGenderServices("women") },
                containerColor = Charcoal,
                borderColor = null,
                elevation = 4.dp
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    GroomoraImage(
                        url = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=800&q=80",
                        contentDescription = "Women Beauty",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        Charcoal.copy(alpha = 0.95f),
                                        Charcoal.copy(alpha = 0.7f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "For Women",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Hair, Skin, Makeup,\nNails & More",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(HoneyAmber),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = "Select Women",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            // Unisex Services Card
            GroomoraCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onNavigateToDiscovery(null) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Unisex Services", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AppText)
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "Bridal • Makeup • Spa • Threading • Bleach • Waxing",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedText
                        )
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = HoneyAmber)
                }
            }
        }
    }
}

data class ServiceCategoryItem(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String
)

@Composable
fun GenderServicesScreen(
    gender: String,
    onNavigateToDiscovery: (String?) -> Unit,
    onNavigateToBooking: (String) -> Unit,
    onBack: () -> Unit
) {
    val isWomen = gender.equals("women", ignoreCase = true)
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(gender) {
        com.groomora.app.DependencyContainer.analyticsManager.logScreenView("gender_services_$gender")
    }


    val categories = remember(isWomen) {
        if (isWomen) {
            listOf(
                ServiceCategoryItem("hair", "Hair", "Haircut, Hair Spa, Smoothening, Keratin & more", "https://images.unsplash.com/photo-1560066984-138dadb4c035?w=400&q=80"),
                ServiceCategoryItem("skin", "Skin", "Facial, Clean up, D-Tan, Bleach & more", "https://images.unsplash.com/photo-1512290900672-1f02e71d4793?w=400&q=80"),
                ServiceCategoryItem("makeup", "Makeup", "Makeup, Party Makeup, HD Makeup & more", "https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?w=400&q=80"),
                ServiceCategoryItem("nails", "Nails", "Manicure, Pedicure, Nail Art, Extensions & more", "https://images.unsplash.com/photo-1632345031435-8727f6897d53?w=400&q=80"),
                ServiceCategoryItem("waxing", "Waxing", "Full Body, Half Body, Face Waxing & more", "https://images.unsplash.com/photo-1519699047748-de8e457a634e?w=400&q=80"),
                ServiceCategoryItem("bridal", "Bridal", "Bridal Makeup, Bridal Hair, Pre Bridal Packages", "https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=400&q=80")
            )
        } else {
            listOf(
                ServiceCategoryItem("hair", "Hair", "Haircut, Hair Spa, Hair Color, Styling & more", "https://images.unsplash.com/photo-1599351431202-1e0f0137899a?w=400&q=80"),
                ServiceCategoryItem("beard", "Beard", "Beard Trim, Shave, Beard Spa, Beard Color", "https://images.unsplash.com/photo-1621605815971-fbc98d665033?w=400&q=80"),
                ServiceCategoryItem("skin", "Skin", "Facial, Clean up, D-Tan, Face Spa & more", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&q=80"),
                ServiceCategoryItem("grooming", "Grooming", "Threading, Waxing, Manicure, Pedicure", "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=400&q=80"),
                ServiceCategoryItem("packages", "Packages", "All Male Grooming Packages", "https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=400&q=80")
            )
        }
    }

    val filteredCategories = categories.filter {
        it.title.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            GroomoraTopAppBar(
                title = if (isWomen) "Women Services" else "Men Services",
                onBack = onBack,
                actions = {
                    IconButton(onClick = { /* Filter */ }) {
                        Icon(Icons.Default.Menu, contentDescription = "Filter", tint = AppText)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Reusable Search Bar
            GroomoraSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = if (isWomen) "Search services for women..." else "Search services for men..."
            )

            // Category List
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredCategories) { cat ->
                    GroomoraCard(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if (cat.id == "packages") {
                                onNavigateToBooking("pkg1")
                            } else {
                                onNavigateToDiscovery(cat.id)
                            }
                        }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Circular Category Image Avatar
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(Charcoal),
                                contentAlignment = Alignment.Center
                            ) {
                                GroomoraImage(
                                    url = cat.imageUrl,
                                    contentDescription = cat.title,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cat.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = AppText
                                )
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = cat.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedText,
                                    maxLines = 2
                                )
                            }

                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color.LightGray
                            )
                        }
                    }
                }
            }
        }
    }
}
