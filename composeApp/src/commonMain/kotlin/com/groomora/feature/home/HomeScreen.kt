package com.groomora.feature.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.app.DependencyContainer
import com.groomora.design.*
import com.groomora.design.components.*
import com.groomora.design.ads.GroomoraAdBanner


import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.ui.semantics.Role
import com.groomora.feature.discovery.Shop
import kotlinx.coroutines.launch

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
    onNavigateToBeauty: () -> Unit,
    onNavigateToHomeService: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToBookingHistory: () -> Unit,
    onNavigateToBooking: (String) -> Unit,
    onNavigateToGenderSelection: () -> Unit = {},
    onNavigateToAddresses: () -> Unit = {},
    onNavigateToServices: (String?) -> Unit = {}
) {

    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedWorkPhotoIndex by remember { mutableStateOf<Int?>(null) }

    val workGalleryPhotos = remember {
        listOf(
            GalleryPhoto(
                title = "Classic Fade",
                imageUrl = "https://images.unsplash.com/photo-1622286342621-4bd786c2447c?w=800&q=80",
                description = "Crisp mid skin fade with razor-sharp temple taper and natural texture top.",
                category = "Men's Grooming"
            ),
            GalleryPhoto(
                title = "Braided Updo",
                imageUrl = "https://images.unsplash.com/photo-1527799820374-dcf8d9d4a388?w=800&q=80",
                description = "Intricate bohemian crown braid finished with pearl hair jewelry for weddings.",
                category = "Bridal Styling"
            ),
            GalleryPhoto(
                title = "Balayage Waves",
                imageUrl = "https://images.unsplash.com/photo-1519699047748-de8e457a634e?w=800&q=80",
                description = "Caramel sun-kissed hand-painted balayage with high-gloss keratin finish.",
                category = "Hair Color"
            ),
            GalleryPhoto(
                title = "Bridal Look",
                imageUrl = "https://images.unsplash.com/photo-1583939003579-730e3918a45a?w=800&q=80",
                description = "Traditional royal bridal HD makeover with 24K gold eye pigments and contouring.",
                category = "Bridal Makeup"
            ),
            GalleryPhoto(
                title = "Smokey Eyes",
                imageUrl = "https://images.unsplash.com/photo-1516975080664-ed2fc6a32937?w=800&q=80",
                description = "Glamorous matte charcoal smokey eyes with dramatic feathered mink lashes.",
                category = "Party Makeup"
            ),
            GalleryPhoto(
                title = "Ozone Beard Spa",
                imageUrl = "https://images.unsplash.com/photo-1621605815971-fbc98d665033?w=800&q=80",
                description = "Deep conditioning warm eucalyptus steam treatment with beard shaping.",
                category = "Beard Ritual"
            )
        )
    }

    LaunchedEffect(Unit) {
        DependencyContainer.analyticsManager.logScreenView("home_screen")
    }

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Column(
                        modifier = Modifier.clickable(role = Role.Button, onClickLabel = "Change address") {
                            onNavigateToAddresses()
                        }
                    ) {
                        Text(
                            "Home",
                            style = MaterialTheme.typography.labelSmall,
                            color = MutedText
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val locationText = remember(state.location) {
                                val address = state.location?.address
                                if (address != null) {
                                    val city = address.city
                                    val area = address.label ?: address.fullAddress.split(",").firstOrNull()?.trim() ?: city
                                    "$area, $city"
                                } else {
                                    "Locating..."
                                }
                            }
                            Text(
                                text = locationText,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = AppText
                            )
                            Spacer(Modifier.width(4.dp))
                            Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = "Change location",
                                modifier = Modifier.size(18.dp),
                                tint = AppText
                            )
                        }
                    }
                },

                actions = {
                    IconButton(onClick = onNavigateToNotifications) {
                        BadgedBox(
                            badge = {
                                Badge(containerColor = HoneyAmber) {
                                    Text("2", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = AppText)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = WarmIvory
                )
            )
        },
        bottomBar = {
            GroomoraBottomNav(
                currentRoute = "home",
                onHomeClick = {},
                onBookingsClick = onNavigateToBookingHistory,
                onOffersClick = onNavigateToOffers,
                onWalletClick = onNavigateToLoyalty,
                onProfileClick = onNavigateToProfile
            )
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
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // 1. Search Bar (Reusable Component)
                item {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        GroomoraSearchBar(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            placeholder = "Search for services, salons, barbers...",
                            onClick = { onNavigateToDiscovery(null) }
                        )
                    }
                }

                // 2. Hero Carousel Banner
                item {
                    val banners = if (state.banners.isNotEmpty()) state.banners else listOf(
                        PromotionBanner("b1", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=800&q=80", "Look Good\nFeel Better", "Book your perfect style today • FLAT 20% OFF", "Book Now", "groomora://services"),
                        PromotionBanner("b2", "https://images.unsplash.com/photo-1583939003579-730e3918a45a?w=800&q=80", "Luxury Bridal\n& Glow Rituals", "Exclusive bridal packages for your special day", "Explore Bridal", "groomora://bridal"),
                        PromotionBanner("b3", "https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=800&q=80", "Relaxing Spa\n& Home Care", "Certified salon experts at your doorstep • ₹150 OFF", "Book At Home", "groomora://homeservice")
                    )
                    val pagerState = rememberPagerState(pageCount = { banners.size })
                    val coroutineScope = rememberCoroutineScope()

                    val bannerColors = listOf(
                        Charcoal,
                        Color(0xFF3B1E38),
                        Color(0xFF1B3B36)
                    )
                    val bannerTags = listOf(
                        "FLAT 20% OFF",
                        "PRE-BRIDAL SPECIAL",
                        "DOORSTEP SERVICE"
                    )

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxWidth()
                    ) { page ->
                        val banner = banners[page]
                        val cardBg = bannerColors.getOrElse(page % bannerColors.size) { Charcoal }
                        val cardTag = bannerTags.getOrElse(page % bannerTags.size) { "SPECIAL OFFER" }

                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            GroomoraCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(180.dp),
                                containerColor = cardBg,
                                borderColor = null,
                                elevation = 4.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(18.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1.2f),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Surface(
                                            color = HoneyAmber,
                                            shape = CircleShape
                                        ) {
                                            Text(
                                                text = cardTag,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                            )
                                        }
                                        Spacer(Modifier.height(6.dp))
                                        Text(
                                            banner.title,
                                            style = MaterialTheme.typography.headlineSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            lineHeight = 22.sp
                                        )
                                        Spacer(Modifier.height(4.dp))
                                        Text(
                                            banner.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White.copy(alpha = 0.85f),
                                            maxLines = 1
                                        )
                                        Spacer(Modifier.height(10.dp))
                                        GroomoraPrimaryButton(
                                            text = banner.ctaLabel,
                                            onClick = {
                                                when {
                                                    banner.deepLink.contains("bridal") -> onNavigateToBridal()
                                                    banner.deepLink.contains("homeservice") || banner.deepLink.contains("home") -> onNavigateToHomeService()
                                                    else -> onNavigateToServices(null)
                                                }
                                            },
                                            height = 36.dp
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(100.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.1f)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        GroomoraImage(
                                            url = banner.imageUrl,
                                            contentDescription = banner.title,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Interactive Pagination Dots
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(banners.size) { index ->
                            val isSelected = pagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 3.dp)
                                    .height(8.dp)
                                    .width(if (isSelected) 24.dp else 8.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) HoneyAmber else Color.LightGray)
                                    .clickable {
                                        coroutineScope.launch {
                                            pagerState.animateScrollToPage(index)
                                        }
                                    }
                            )
                        }
                    }
                }



                // 3. Quick Action Row (Reusable Circular Action Buttons)
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GroomoraCircularActionButton(
                            label = "Near Me",
                            icon = Icons.Default.LocationOn,
                            iconColor = Color(0xFF4A6FA5),
                            onClick = { onNavigateToDiscovery(null) }
                        )
                        GroomoraCircularActionButton(
                            label = "Salons",
                            icon = Icons.Default.Home,
                            iconColor = Color(0xFFC58A38),
                            onClick = { onNavigateToDiscovery("salons") }
                        )
                        GroomoraCircularActionButton(
                            label = "Barbers",
                            icon = Icons.Default.Star,
                            iconColor = Color(0xFF333333),
                            onClick = { onNavigateToDiscovery("barbers") }
                        )
                        GroomoraCircularActionButton(
                            label = "Home Services",
                            icon = Icons.Default.Place,
                            iconColor = TealGreen,
                            onClick = onNavigateToHomeService
                        )
                        GroomoraCircularActionButton(
                            label = "Offers",
                            icon = Icons.Default.ShoppingCart,
                            iconColor = Color(0xFFB23A48),
                            onClick = onNavigateToOffers
                        )
                    }
                }

                // 4. Explore by Category
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Explore by Category", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(
                                "View All",
                                style = MaterialTheme.typography.labelMedium,
                                color = MutedText,
                                modifier = Modifier.clickable { onNavigateToGenderSelection() }
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            ExploreCategoryThumbnailCard(
                                name = "Hair",
                                imageUrl = "https://images.unsplash.com/photo-1560066984-138dadb4c035?w=500&q=80",
                                onClick = { onNavigateToServices("Hair") }
                            )
                            ExploreCategoryThumbnailCard(
                                name = "Skin",
                                imageUrl = "https://images.unsplash.com/photo-1512290900672-1f02e71d4793?w=500&q=80",
                                onClick = { onNavigateToServices("Skin & Face") }
                            )
                            ExploreCategoryThumbnailCard(
                                name = "Makeup",
                                imageUrl = "https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?w=500&q=80",
                                onClick = { onNavigateToServices("Bridal") }
                            )
                            ExploreCategoryThumbnailCard(
                                name = "Nails",
                                imageUrl = "https://images.unsplash.com/photo-1632345031435-8727f6897d53?w=500&q=80",
                                onClick = { onNavigateToServices("Nails") }
                            )
                        }

                    }
                }

                // 4.5 Google Ad Manager Sponsored Banner
                item {
                    GroomoraAdBanner(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onAdClick = {
                            DependencyContainer.adManager.logAdClick("banner", "home_screen_top_banner")
                        }
                    )
                }

                // 5. Top Rated Near You (Reusable ShopCard Component)

                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("Top Rated Near You", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                        ShopCard(
                            shop = Shop(
                                id = "s1",
                                name = "King's Barber Studio",
                                rating = 4.8,
                                reviewCount = 245,
                                address = "Koramangala",
                                distance = "0.8 km",
                                imageUrl = "https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=600&q=80"
                            ),
                            onViewShop = { onNavigateToDiscovery(null) }
                        )

                        ShopCard(
                            shop = Shop(
                                id = "s2",
                                name = "Bella Beauty Parlour",
                                rating = 4.7,
                                reviewCount = 198,
                                address = "Koramangala",
                                distance = "1.2 km",
                                imageUrl = "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=600&q=80"
                            ),
                            onViewShop = { onNavigateToDiscovery(null) }
                        )
                    }
                }

                // 6. SPECIAL SERVICES & HOME SERVICES Section
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            color = Color(0xFF3B286D),
                            shape = MaterialTheme.shapes.small,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(
                                "SPECIAL SERVICES & HOME SERVICES",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }

                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // 1. Bridal Services Card
                            item {
                                GroomoraCard(
                                    modifier = Modifier.width(280.dp),
                                    containerColor = Color(0xFFFFF9F5),
                                    borderColor = Color(0xFFF3E1D5)
                                ) {
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(130.dp)
                                                .clip(MaterialTheme.shapes.large)
                                        ) {
                                            GroomoraImage(
                                                url = "https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=800&q=80",
                                                contentDescription = "Bridal Services",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }

                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text("Bridal Services", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AppText)
                                            Spacer(Modifier.height(8.dp))
                                            ChecklistRow("Bridal Makeup")
                                            ChecklistRow("Bridal Hair Style")
                                            ChecklistRow("Pre Bridal Packages")
                                            ChecklistRow("Saree Draping")
                                            ChecklistRow("Engagement Makeup")
                                            Spacer(Modifier.height(14.dp))
                                            GroomoraPrimaryButton(
                                                text = "View Bridal Packages",
                                                onClick = onNavigateToBridal,
                                                modifier = Modifier.fillMaxWidth(),
                                                height = 42.dp
                                            )
                                        }
                                    }
                                }
                            }

                            // 2. Home Services Card
                            item {
                                GroomoraCard(
                                    modifier = Modifier.width(280.dp),
                                    containerColor = Color(0xFFF2FAF7),
                                    borderColor = Color(0xFFD4EFE6)
                                ) {
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(130.dp)
                                                .clip(MaterialTheme.shapes.large)
                                        ) {
                                            GroomoraImage(
                                                url = "https://images.unsplash.com/photo-1560066984-138dadb4c035?w=800&q=80",
                                                contentDescription = "Salon at Home",
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }

                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Text("Home Services", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AppText)
                                            Spacer(Modifier.height(8.dp))
                                            ChecklistRow("Salon at Home", checkColor = TealGreen)
                                            ChecklistRow("Comfort of Home", checkColor = TealGreen)
                                            ChecklistRow("Verified Professionals", checkColor = TealGreen)
                                            ChecklistRow("Hygienic & Safe", checkColor = TealGreen)
                                            ChecklistRow("On-time Service", checkColor = TealGreen)
                                            Spacer(Modifier.height(14.dp))
                                            GroomoraPrimaryButton(
                                                text = "Book Home Service",
                                                onClick = onNavigateToHomeService,
                                                containerColor = TealGreen,
                                                modifier = Modifier.fillMaxWidth(),
                                                height = 42.dp
                                            )
                                        }
                                    }
                                }
                            }

                            // 3. Other Services Card
                            item {
                                GroomoraCard(
                                    modifier = Modifier.width(280.dp),
                                    containerColor = Color.White
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text("Other Services", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AppText)
                                        Spacer(Modifier.height(12.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceAround
                                        ) {
                                            OtherServiceItem("Spa & Massage", Color(0xFFE8D7F1))
                                            OtherServiceItem("Hair Color", Color(0xFFFFE5D9))
                                            OtherServiceItem("Treatments", Color(0xFFD8E2DC))
                                        }
                                        Spacer(Modifier.height(10.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceAround
                                        ) {
                                            OtherServiceItem("Threading", Color(0xFFF4ACB7))
                                            OtherServiceItem("Bleach", Color(0xFFFFCAD4))
                                            OtherServiceItem("Feet Care", Color(0xFFB5E2FA))
                                        }
                                        Spacer(Modifier.height(16.dp))
                                        GroomoraPrimaryButton(
                                            text = "Explore All Services",
                                            onClick = { onNavigateToServices(null) },
                                            containerColor = PlumPurple,
                                            modifier = Modifier.fillMaxWidth(),
                                            height = 42.dp
                                        )

                                    }
                                }
                            }
                        }
                    }
                }

                // 7. OUR WORK GALLERY
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "OUR WORK GALLERY (Showcase Your Skills)",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = AppText
                            )
                            Surface(
                                shape = CircleShape,
                                color = HoneyAmber.copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, HoneyAmber.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = "${workGalleryPhotos.size} Photos",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = HoneyAmber,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            itemsIndexed(workGalleryPhotos) { index, photo ->
                                GroomoraCard(
                                    modifier = Modifier
                                        .size(width = 130.dp, height = 130.dp)
                                        .clickable { selectedWorkPhotoIndex = index },
                                    shape = RoundedCornerShape(14.dp),
                                    containerColor = Charcoal
                                ) {
                                    Box(Modifier.fillMaxSize()) {
                                        GroomoraImage(
                                            url = photo.imageUrl,
                                            contentDescription = photo.title,
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = ContentScale.Crop
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .align(Alignment.BottomCenter)
                                                .background(
                                                    androidx.compose.ui.graphics.Brush.verticalGradient(
                                                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                                    )
                                                )
                                                .padding(horizontal = 6.dp, vertical = 4.dp),
                                            contentAlignment = Alignment.BottomStart
                                        ) {
                                            Text(
                                                photo.title,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color.White,
                                                fontWeight = FontWeight.SemiBold,
                                                maxLines = 1,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 7.5 BEAUTY & GROOMING STORE (Featured Products)
                item {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Beauty & Grooming Store", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = AppText)
                                Text("Salon-grade haircare, skincare & beard essentials", style = MaterialTheme.typography.labelSmall, color = MutedText)
                            }
                            Text(
                                "View All ->",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = HoneyAmber,
                                modifier = Modifier.clickable { onNavigateToProducts() }
                            )
                        }

                        val featuredProducts = listOf(
                            Triple("Organic Argan Hair Growth Oil", "₹599", "https://images.unsplash.com/photo-1608248597359-009eb73d6d03?w=500&q=80"),
                            Triple("Matte Finish Styling Clay", "₹450", "https://images.unsplash.com/photo-1599351431202-1e0f0137899a?w=500&q=80"),
                            Triple("Vitamin C Glow Face Wash", "₹349", "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=500&q=80"),
                            Triple("Gold Radiance Night Cream", "₹899", "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?w=500&q=80"),
                            Triple("Beard Conditioning Balm", "₹399", "https://images.unsplash.com/photo-1621605815971-fbc98d665033?w=500&q=80")
                        )

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(featuredProducts) { (name, price, imageUrl) ->
                                GroomoraCard(
                                    modifier = Modifier
                                        .width(160.dp)
                                        .clickable { onNavigateToProducts() },
                                    containerColor = Color.White
                                ) {
                                    Column {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(115.dp)
                                                .clip(MaterialTheme.shapes.medium)
                                                .background(Charcoal)
                                        ) {
                                            GroomoraImage(
                                                url = imageUrl,
                                                contentDescription = name,
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Text(name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, maxLines = 1, color = AppText)
                                            Spacer(Modifier.height(2.dp))
                                            Text(price, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = HoneyAmber)
                                            Spacer(Modifier.height(8.dp))
                                            GroomoraPrimaryButton(
                                                text = "Buy Now",
                                                onClick = onNavigateToProducts,
                                                height = 32.dp,
                                                modifier = Modifier.fillMaxWidth()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 8. Trust Badges Strip
                item {
                    LazyRow(

                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        item { TrustBadgeItem("Real-time\nAvailability", Icons.Default.DateRange) }
                        item { TrustBadgeItem("Verified\nProfessionals", Icons.Default.AccountCircle) }
                        item { TrustBadgeItem("Easy\nRescheduling", Icons.Default.Refresh) }
                        item { TrustBadgeItem("Secure\nPayments", Icons.Default.Lock) }
                        item { TrustBadgeItem("Loyalty\nRewards", Icons.Default.Star) }
                        item { TrustBadgeItem("Offers &\nDiscounts", Icons.Default.ShoppingCart) }
                        item { TrustBadgeItem("Booking\nHistory", Icons.Default.DateRange) }
                        item { TrustBadgeItem("Customer\nSupport", Icons.Default.Info) }
                    }
                }
            }
        }

        // Full-Screen High Res Photo Lightbox Dialog
        if (selectedWorkPhotoIndex != null) {
            GroomoraPhotoViewerDialog(
                photos = workGalleryPhotos,
                initialIndex = selectedWorkPhotoIndex!!,
                onDismissRequest = { selectedWorkPhotoIndex = null }
            )
        }
    }
}

@Composable
fun ChecklistRow(text: String, checkColor: Color = HoneyAmber) {
    Row(
        modifier = Modifier.padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            tint = checkColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, color = AppText)
    }
}

@Composable
fun OtherServiceItem(name: String, bgTint: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(bgTint),
            contentAlignment = Alignment.Center
        ) {
            Text(name.take(1), fontWeight = FontWeight.Bold, color = Charcoal)
        }
        Spacer(Modifier.height(4.dp))
        Text(
            name,
            style = MaterialTheme.typography.labelSmall,
            color = AppText,
            maxLines = 1
        )
    }
}

@Composable
fun ExploreCategoryThumbnailCard(name: String, imageUrl: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(MaterialTheme.shapes.medium)
                .background(Charcoal),
            contentAlignment = Alignment.Center
        ) {
            GroomoraImage(
                url = imageUrl,
                contentDescription = name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(name, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = AppText)
    }
}

@Composable
fun TrustBadgeItem(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(76.dp)
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, BorderGray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = HoneyAmber, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.height(4.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            fontSize = 10.sp,
            color = MutedText,
            lineHeight = 12.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
