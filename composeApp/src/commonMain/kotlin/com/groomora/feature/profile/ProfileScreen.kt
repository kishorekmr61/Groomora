package com.groomora.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.groomora.design.Champagne
import com.groomora.design.Charcoal
import com.groomora.design.WarmIvory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onNavigateToSettings: () -> Unit,
    onNavigateToOrders: () -> Unit,
    onNavigateToBookingHistory: () -> Unit,
    onNavigateToAddresses: () -> Unit,
    onNavigateToFavorites: () -> Unit,
    onNavigateToSupport: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Champagne)
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    ProfileHeader(state.user?.name ?: "Alex Morgan", state.user?.phoneNumber ?: "+91 98765 43210")
                }

                item {
                    Text("My Activity", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
                }

                item {
                    ProfileMenuItem("My Bookings (Reschedule & Cancel)", Icons.Default.DateRange, onClick = onNavigateToBookingHistory)
                }
                item {
                    ProfileMenuItem("Product Orders & Delivery", Icons.Default.ShoppingCart, onClick = onNavigateToOrders)
                }
                item {
                    ProfileMenuItem("My Favorites & Saved Salons", Icons.Default.Favorite, onClick = onNavigateToFavorites)
                }
                item {
                    ProfileMenuItem("Saved Addresses", Icons.Default.LocationOn, onClick = onNavigateToAddresses)
                }

                item {
                    Text("Support & Settings", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp, bottom = 4.dp))
                }
                item {
                    ProfileMenuItem("Help & Support Center", Icons.Default.Info, onClick = onNavigateToSupport)
                }
                item {
                    ProfileMenuItem("App Settings & Preferences", Icons.Default.Settings, onClick = onNavigateToSettings)
                }


                item {
                    Spacer(Modifier.height(24.dp))
                    Button(
                        onClick = { viewModel.onIntent(ProfileIntent.Logout) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Red),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red)
                    ) {
                        Text("Logout")
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(name: String, phone: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.AccountCircle,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Charcoal
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(name, style = MaterialTheme.typography.headlineSmall)
            Text(phone, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}

@Composable
fun ProfileMenuItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.White,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(24.dp), tint = Charcoal)
            Spacer(Modifier.width(16.dp))
            Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.LightGray)
        }
    }
}
