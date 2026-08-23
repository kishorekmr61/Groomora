package com.groomora.feature.discovery

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
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
import com.groomora.design.*
import com.groomora.design.components.GalleryPhoto
import com.groomora.design.components.GroomoraPhotoViewerDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfessionalProfileScreen(
    professionalId: String,
    viewModel: ProfessionalProfileViewModel,
    onNavigateToBooking: (String?) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var selectedPortfolioIndex by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(professionalId) {
        viewModel.onIntent(ProfessionalProfileIntent.LoadProfile(professionalId))
    }

    val portfolioPhotos = remember(state.professional?.portfolioImages) {
        val images = state.professional?.portfolioImages?.ifEmpty {
            listOf(
                "https://images.unsplash.com/photo-1622286342621-4bd786c2447c?w=800&q=80",
                "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=800&q=80",
                "https://images.unsplash.com/photo-1621605815971-fbc98d665033?w=800&q=80",
                "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=800&q=80"
            )
        } ?: listOf(
            "https://images.unsplash.com/photo-1622286342621-4bd786c2447c?w=800&q=80",
            "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=800&q=80"
        )
        images.mapIndexed { idx, url ->
            GalleryPhoto(
                title = "${state.professional?.name ?: "Stylist"} Work #${idx + 1}",
                imageUrl = url,
                description = "Crafted by ${state.professional?.name ?: "Verified Stylist"}",
                category = state.professional?.role ?: "Portfolio"
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.professional?.name ?: "Profile", fontWeight = FontWeight.Bold) },
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
            Surface(shadowElevation = 8.dp, color = Color.White) {
                Button(
                    onClick = { onNavigateToBooking(null) },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne)
                ) {
                    Text("Book an Appointment with ${state.professional?.name?.split(" ")?.firstOrNull() ?: "Stylist"}", fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Charcoal)
            }
        } else {
            state.professional?.let { prof ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(WarmIvory)
                        .padding(padding)
                ) {
                    item {
                        ProfessionalHeader(prof)
                    }

                    item {
                        ProfessionalBio(prof)
                    }

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Work Portfolio",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Charcoal
                            )
                            Surface(
                                shape = CircleShape,
                                color = HoneyAmber.copy(alpha = 0.12f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, HoneyAmber.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    "${portfolioPhotos.size} Photos",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = HoneyAmber,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    item {
                        PortfolioGrid(
                            photos = portfolioPhotos,
                            onPhotoClick = { index -> selectedPortfolioIndex = index }
                        )
                    }
                }
            }
        }

        // Full Screen Lightbox Dialog for portfolio photos
        if (selectedPortfolioIndex != null) {
            GroomoraPhotoViewerDialog(
                photos = portfolioPhotos,
                initialIndex = selectedPortfolioIndex!!,
                onDismissRequest = { selectedPortfolioIndex = null }
            )
        }
    }
}

@Composable
fun ProfessionalHeader(prof: ProfessionalDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .background(Charcoal)
                .border(2.dp, HoneyAmber, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            GroomoraImage(
                url = prof.imageUrl.ifEmpty { "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&q=80" },
                contentDescription = prof.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(prof.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Charcoal)
        Text(prof.role, style = MaterialTheme.typography.titleMedium, color = MutedText)
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 6.dp)) {
            Icon(Icons.Default.Star, contentDescription = null, tint = HoneyAmber, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(4.dp))
            Text("${prof.rating} (${prof.reviewCount} verified reviews)", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
        Text("${prof.yearsOfExperience} Years Experience • Certified Stylist", style = MaterialTheme.typography.labelMedium, color = HoneyAmber, modifier = Modifier.padding(top = 4.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfessionalBio(prof: ProfessionalDetail) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("About", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Charcoal)
        Spacer(Modifier.height(6.dp))
        Text(prof.bio, style = MaterialTheme.typography.bodyMedium, color = AppText)
        Spacer(Modifier.height(16.dp))
        Text("Specializations & Skills", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Charcoal)
        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            prof.skills.forEach { skill ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderGray)
                ) {
                    Text(
                        text = skill,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PortfolioGrid(
    photos: List<GalleryPhoto>,
    onPhotoClick: (Int) -> Unit
) {
    FlowRow(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        photos.forEachIndexed { index, photo ->
            Box(
                modifier = Modifier
                    .size(105.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Charcoal)
                    .clickable { onPhotoClick(index) }
            ) {
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
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        photo.title,
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}
