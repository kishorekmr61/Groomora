package com.groomora.feature.homeservice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.design.*
import com.groomora.design.GroomoraImage
import com.groomora.design.components.*

data class HomeServicePackage(
    val id: String,
    val title: String,
    val category: String,
    val duration: String,
    val price: Double,
    val originalPrice: Double,
    val imageUrl: String,
    val items: List<String>
)

data class HomeServiceCategory(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeServiceScreen(
    viewModel: HomeServiceViewModel,
    onNavigateToDiscovery: (String) -> Unit,
    onNavigateToBooking: (String) -> Unit = {},
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    val categories = listOf(
        HomeServiceCategory(
            title = "Facials & Cleanups",
            description = "Hydra glow, 24k gold radiance, detan & fruit cleanups",
            icon = Icons.Default.Face,
            color = Color(0xFFFDE8E9)
        ),
        HomeServiceCategory(
            title = "Waxing & Threading",
            description = "Painless honey wax, Rica brazilian, eyebrow shaping",
            icon = Icons.Default.Star,
            color = Color(0xFFFFF3E0)
        ),
        HomeServiceCategory(
            title = "Hair Spa & Styling",
            description = "Deep nourishing hair spa, root touch-up, haircut & styling",
            icon = Icons.Default.Build,
            color = Color(0xFFE8F5E9)
        ),
        HomeServiceCategory(
            title = "Mani-Pedi Spa",
            description = "Rose petal pedicure, crystal foot spa, cuticle care & gel polish",
            icon = Icons.Default.Favorite,
            color = Color(0xFFE1F5FE)
        ),
        HomeServiceCategory(
            title = "Men's Grooming",
            description = "Classic haircut, beard sculpt, charcoal detan & head massage",
            icon = Icons.Default.Person,
            color = Color(0xFFEDE7F6)
        ),
        HomeServiceCategory(
            title = "Pre-Bridal & Makeup",
            description = "Full body polishing, saree draping, party hair & bridal makeup",
            icon = Icons.Default.FavoriteBorder,
            color = Color(0xFFFCE4EC)
        )
    )


    val bestSellerPackages = listOf(
        HomeServicePackage(
            id = "pkg_home_glow",
            title = "Complete Glow Ritual",
            category = "Facial & Waxing",
            duration = "90 mins",
            price = 1299.0,
            originalPrice = 1899.0,
            imageUrl = "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?w=600&q=80",
            items = listOf(
                "Brightening Vitamin C Facial",
                "Full Arms & Full Legs Honey Waxing",
                "Eyebrow & Upper Lip Threading",
                "15-min Relaxing Scalp Massage"
            )
        ),
        HomeServicePackage(
            id = "pkg_home_spa",
            title = "Aromatherapy Stress Relief",
            category = "Body & Foot Spa",
            duration = "75 mins",
            price = 1499.0,
            originalPrice = 2199.0,
            imageUrl = "https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=600&q=80",
            items = listOf(
                "60-min Lavender Essential Oil Full Body Massage",
                "Revitalizing Foot Reflexology",
                "Hot Towel Therapy & Organic Body Butter"
            )
        ),
        HomeServicePackage(
            id = "pkg_home_mens",
            title = "Men's Executive Grooming",
            category = "Hair & Beard",
            duration = "60 mins",
            price = 799.0,
            originalPrice = 1199.0,
            imageUrl = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=600&q=80",
            items = listOf(
                "Signature Haircut & Scissor Styling",
                "Precision Beard Trim & Hot Foam Shave",
                "Charcoal Detan Face Cleanup",
                "Invigorating Head Massage"
            )
        ),
        HomeServicePackage(
            id = "pkg_home_manipedi",
            title = "Rose Deluxe Mani-Pedi Duo",
            category = "Hands & Feet",
            duration = "60 mins",
            price = 699.0,
            originalPrice = 999.0,
            imageUrl = "https://images.unsplash.com/photo-1632345031435-8727f6897d53?w=600&q=80",
            items = listOf(
                "Organic Rose Petal Foot Soak & Scrub",
                "Dead Skin Callus Removal",
                "Hydrating Cuticle Oil Therapy",
                "Nail Shaping & Long-lasting Gel Polish"
            )
        )
    )

    Scaffold(
        topBar = {
            GroomoraTopAppBar(
                title = "Salon & Spa at Home",
                subtitle = "Certified Experts • Doorstep Luxury",
                onBack = onBack
            )
        },
        bottomBar = {
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
                            text = "Doorstep Services",
                            style = MaterialTheme.typography.labelSmall,
                            color = MutedText
                        )
                        Text(
                            text = "From ₹299 onwards",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = HoneyAmber
                        )
                    }

                    GroomoraPrimaryButton(
                        text = "Find Home Experts ->",
                        onClick = { onNavigateToDiscovery("home") },
                        height = 44.dp
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
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 1. Hero Showcase Banner
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        GroomoraImage(
                            url = "https://images.unsplash.com/photo-1560066984-138dadb4c035?w=900&q=80",
                            contentDescription = "Salon at Home Banner",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Color.Black.copy(alpha = 0.55f)
                                )
                                .padding(20.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Column(modifier = Modifier.fillMaxWidth(0.85f)) {
                                Surface(
                                    color = HoneyAmber,
                                    shape = CircleShape
                                ) {
                                    Text(
                                        text = "DOORSTEP LUXURY",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = "Salon & Spa Care\nat Your Home",
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    lineHeight = 26.sp
                                )
                                Spacer(Modifier.height(6.dp))
                                Text(
                                    text = "Verified professionals arrive with sealed, single-use hygienic kits.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }
                    }
                }

                // 2. Trust Assurance Pills Strip
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        item { TrustPill("100% Sealed Kits", Icons.Default.CheckCircle, TealGreen) }
                        item { TrustPill("Verified Beauticians", Icons.Default.AccountCircle, HoneyAmber) }
                        item { TrustPill("Post-Service Cleanup", Icons.Default.Refresh, Color(0xFF3F51B5)) }
                        item { TrustPill("Zero Travel Hassle", Icons.Default.Home, Charcoal) }
                    }
                }


                // 3. "What Can You Do at Home Services?" (Interactive Categories)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column {
                            Text(
                                text = "What Can You Do at Home?",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = AppText
                            )
                            Text(
                                text = "Choose from full salon, spa & grooming services delivered to your door",
                                style = MaterialTheme.typography.bodySmall,
                                color = MutedText
                            )
                        }

                        // 2-Column Grid of Home Service Categories
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            categories.chunked(2).forEach { rowCategories ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    rowCategories.forEach { cat ->
                                        GroomoraCard(
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable(role = Role.Button, onClickLabel = cat.title) {
                                                    onNavigateToDiscovery("home")
                                                },
                                            containerColor = Color.White
                                        ) {
                                            Column(modifier = Modifier.padding(14.dp)) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(42.dp)
                                                        .clip(CircleShape)
                                                        .background(cat.color),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = cat.icon,
                                                        contentDescription = null,
                                                        tint = Charcoal,
                                                        modifier = Modifier.size(22.dp)
                                                    )
                                                }
                                                Spacer(Modifier.height(10.dp))
                                                Text(
                                                    text = cat.title,
                                                    style = MaterialTheme.typography.titleSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = AppText
                                                )
                                                Spacer(Modifier.height(4.dp))
                                                Text(
                                                    text = cat.description,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MutedText,
                                                    lineHeight = 15.sp,
                                                    maxLines = 2
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. Best-Selling At-Home Packages
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Best-Selling Home Packages",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = AppText
                                )
                                Text(
                                    text = "Save up to 35% with all-inclusive ritual packages",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MutedText
                                )
                            }
                        }

                        bestSellerPackages.forEach { pkg ->
                            GroomoraCard(
                                modifier = Modifier.fillMaxWidth(),
                                containerColor = Color.White
                            ) {
                                Column {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(140.dp)
                                    ) {
                                        GroomoraImage(
                                            url = pkg.imageUrl,
                                            contentDescription = pkg.title,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                        Surface(
                                            color = Charcoal.copy(alpha = 0.8f),
                                            shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 8.dp),
                                            modifier = Modifier.align(Alignment.TopEnd)
                                        ) {
                                            Text(
                                                text = pkg.duration,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column {
                                                Text(
                                                    text = pkg.title,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold,
                                                    color = AppText
                                                )
                                                Text(
                                                    text = pkg.category,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = HoneyAmber,
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            }

                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "₹${pkg.price.toInt()}",
                                                    style = MaterialTheme.typography.titleLarge,
                                                    fontWeight = FontWeight.Bold,
                                                    color = AppText
                                                )
                                                Spacer(Modifier.width(6.dp))
                                                Text(
                                                    text = "₹${pkg.originalPrice.toInt()}",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    color = MutedText,
                                                    textDecoration = TextDecoration.LineThrough
                                                )
                                            }
                                        }

                                        Spacer(Modifier.height(10.dp))

                                        // Inclusions
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            pkg.items.forEach { itemText ->
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = TealGreen,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                    Spacer(Modifier.width(6.dp))
                                                    Text(
                                                        text = itemText,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = Charcoal
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(Modifier.height(14.dp))

                                        GroomoraPrimaryButton(
                                            text = "Book This Package",
                                            onClick = { onNavigateToBooking(pkg.id) },
                                            modifier = Modifier.fillMaxWidth(),
                                            height = 40.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 5. "How Doorstep Service Works" 4-Step Timeline
                item {
                    GroomoraCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        containerColor = Color.White
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Text(
                                text = "How At-Home Service Works",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AppText
                            )
                            Spacer(Modifier.height(14.dp))

                            HowItWorksStep(1, "Pick Service & Slot", "Choose your preferred grooming treatments, date, and home address.")
                            HowItWorksStep(2, "Certified Expert Assigned", "Verified specialist arrives on time with a single-use sealed kit.")
                            HowItWorksStep(3, "Hygienic Setup & Pampering", "Disposable sheets & sanitized tools guarantee 100% mess-free service.")
                            HowItWorksStep(4, "Cleanup & Radiant Look", "Stylist disposes single-use items, leaving your space spotless.")
                        }
                    }
                }

                // 6. Safety & Hygiene Section
                item {
                    GroomoraCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        containerColor = TealGreen.copy(alpha = 0.08f),
                        borderColor = TealGreen.copy(alpha = 0.3f)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = TealGreen, modifier = Modifier.size(24.dp))
                                Spacer(Modifier.width(10.dp))
                                Text("Groomora Safety & Hygiene Promise", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = AppText)
                            }

                            Spacer(Modifier.height(10.dp))
                            SafetyCheckItem("Monodose Sealed Products: Opened right in front of you")
                            SafetyCheckItem("Hospital-Grade Sanitization of all metal tools")
                            SafetyCheckItem("100% Background-checked & certified salon specialists")
                            SafetyCheckItem("Mess-Free Floor Protection Sheets included")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TrustPill(text: String, icon: ImageVector, color: Color) {
    Surface(
        color = Color.White,
        shape = CircleShape,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(text, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, color = AppText)
        }
    }
}

@Composable
private fun HowItWorksStep(step: Int, title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(HoneyAmber),
            contentAlignment = Alignment.Center
        ) {
            Text("$step", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = AppText)
            Spacer(Modifier.height(2.dp))
            Text(desc, style = MaterialTheme.typography.bodySmall, color = MutedText)
        }
    }
}

@Composable
private fun SafetyCheckItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TealGreen, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodySmall, color = AppText)
    }
}
