package com.groomora.feature.bridal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun BridalScreen(
    viewModel: BridalViewModel,
    onNavigateToBooking: (String) -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bridal & Engagement") },
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
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    BridalHeader()
                }

                items(state.packages) { pkg ->
                    BridalPackageCard(pkg, onBook = { onNavigateToBooking(pkg.id) })
                }
            }
        }
    }
}

@Composable
fun BridalHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF800020)) // Burgundy for Bridal
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                "Your Perfect Day Starts Here",
                style = MaterialTheme.typography.headlineSmall,
                color = Champagne
            )
            Text(
                "Luxury bridal makeovers, hairstyling and more by experts.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
        }
    }
}

@Composable
fun BridalPackageCard(pkg: BridalPackage, onBook: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(pkg.name, style = MaterialTheme.typography.titleLarge, color = Charcoal)
                Text("₹${pkg.price}", style = MaterialTheme.typography.titleLarge, color = Color(0xFF800020))
            }
            Spacer(Modifier.height(8.dp))
            Text(pkg.description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            
            Spacer(Modifier.height(16.dp))
            pkg.items.forEach { item ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Color(0xFF2E7D5B)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(item, style = MaterialTheme.typography.bodySmall)
                }
            }
            
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = onBook,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne)
            ) {
                Text("Book Package")
            }
            
            if (pkg.isHomeServiceAvailable) {
                Text(
                    "Home Service Available",
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF008080), // Teal for home service
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
