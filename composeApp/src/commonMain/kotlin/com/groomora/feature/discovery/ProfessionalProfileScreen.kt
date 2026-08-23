package com.groomora.feature.discovery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
fun ProfessionalProfileScreen(
    professionalId: String,
    viewModel: ProfessionalProfileViewModel,
    onNavigateToBooking: (String?) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(professionalId) {
        viewModel.onIntent(ProfessionalProfileIntent.LoadProfile(professionalId))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(state.professional?.name ?: "Profile") },
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
            Surface(shadowElevation = 8.dp) {
                Button(
                    onClick = { onNavigateToBooking(null) },
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne)
                ) {
                    Text("Book an Appointment")
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
                        Text(
                            "Portfolio",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }

                    item {
                        PortfolioGrid(prof.portfolioImages)
                    }
                }
            }
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
                .size(120.dp)
                .background(Color.LightGray, MaterialTheme.shapes.extraLarge),
            contentAlignment = Alignment.Center
        ) {
            Text("Photo", color = Color.Gray)
        }
        Spacer(Modifier.height(16.dp))
        Text(prof.name, style = MaterialTheme.typography.headlineMedium)
        Text(prof.role, style = MaterialTheme.typography.titleMedium, color = Color.Gray)
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(20.dp))
            Text("${prof.rating} (${prof.reviewCount} reviews)", style = MaterialTheme.typography.bodyMedium)
        }
        Text("${prof.yearsOfExperience} Years Experience", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 4.dp))
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfessionalBio(prof: ProfessionalDetail) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("About", style = MaterialTheme.typography.titleMedium)
        Text(prof.bio, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
        Spacer(Modifier.height(16.dp))
        Text("Skills", style = MaterialTheme.typography.titleMedium)
        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            prof.skills.forEach { skill ->
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
                ) {
                    Text(
                        text = skill,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PortfolioGrid(images: List<String>) {
    // Note: Grid in LazyColumn is tricky, using FlowRow or fixed items for now
    FlowRow(
        modifier = Modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        images.forEach { _ ->
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.LightGray, MaterialTheme.shapes.medium),
                contentAlignment = Alignment.Center
            ) {
                Text("Work", color = Color.Gray, style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}
