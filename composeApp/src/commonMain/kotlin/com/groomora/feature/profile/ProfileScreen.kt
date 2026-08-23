package com.groomora.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.design.Champagne
import com.groomora.design.Charcoal
import com.groomora.design.HoneyAmber
import com.groomora.design.WarmIvory
import com.groomora.design.GroomoraImage
import com.groomora.design.components.GroomoraBottomNav
import com.groomora.design.components.GroomoraPrimaryButton
import com.groomora.feature.auth.User


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
    onNavigateToEditProfile: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToOffers: () -> Unit = {},
    onNavigateToWallet: () -> Unit = {},
    onLogout: () -> Unit = {},
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
                    IconButton(onClick = onNavigateToEditProfile) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Profile", tint = Champagne)
                    }
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
        },
        bottomBar = {
            GroomoraBottomNav(
                currentRoute = "profile",
                onHomeClick = onNavigateToHome,
                onBookingsClick = onNavigateToBookingHistory,
                onOffersClick = onNavigateToOffers,
                onWalletClick = onNavigateToWallet,
                onProfileClick = {}
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
                    ProfileHeader(
                        user = state.user,
                        onEditClick = onNavigateToEditProfile
                    )
                }

                item {
                    Text("My Account", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
                }

                item {
                    ProfileMenuItem("Edit Personal Profile", Icons.Default.Person, onClick = onNavigateToEditProfile)
                }
                item {
                    ProfileMenuItem("Saved Addresses & Location", Icons.Default.LocationOn, onClick = onNavigateToAddresses)
                }

                item {
                    Text("My Activity", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 12.dp, bottom = 4.dp))
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
                    Text("Support & Settings", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 12.dp, bottom = 4.dp))
                }
                item {
                    ProfileMenuItem("Help & Support Center", Icons.Default.Info, onClick = onNavigateToSupport)
                }
                item {
                    ProfileMenuItem("App Settings & Preferences", Icons.Default.Settings, onClick = onNavigateToSettings)
                }

                item {
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.onIntent(ProfileIntent.Logout)
                            onLogout()
                        },
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
fun ProfileHeader(
    user: User?,
    onEditClick: () -> Unit
) {
    val name = user?.name?.takeIf { it.isNotBlank() } ?: "Groomora Member"
    val rawPhone = user?.phoneNumber ?: ""
    val cleanDigits = rawPhone.filter { it.isDigit() }
    val phone = when {
        cleanDigits.length == 10 -> "+91 ${cleanDigits.substring(0, 5)} ${cleanDigits.substring(5)}"
        rawPhone.isNotBlank() -> if (rawPhone.startsWith("+91")) rawPhone else "+91 $rawPhone"
        else -> "Mobile not linked"
    }
    val avatarUrl = user?.profileImageUrl?.takeIf { it.isNotBlank() } ?: "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&q=80"

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.Button, onClickLabel = "Edit Profile", onClick = onEditClick),
        color = Color.White,
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Charcoal)
                    .border(2.dp, HoneyAmber, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                GroomoraImage(
                    url = avatarUrl,
                    contentDescription = name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Charcoal
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = phone,
                        style = MaterialTheme.typography.bodySmall,
                        color = Charcoal,
                        fontWeight = FontWeight.Medium
                    )
                    if (user?.phoneNumber?.isNotBlank() == true) {
                        Spacer(Modifier.width(6.dp))
                        Surface(
                            color = Color(0xFF2E7D5B).copy(alpha = 0.15f),
                            shape = CircleShape
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Verified",
                                    tint = Color(0xFF2E7D5B),
                                    modifier = Modifier.size(10.dp)
                                )
                                Spacer(Modifier.width(2.dp))
                                Text(
                                    text = "Verified",
                                    color = Color(0xFF2E7D5B),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                Surface(
                    color = HoneyAmber.copy(alpha = 0.15f),
                    shape = CircleShape,
                    modifier = Modifier.clickable(onClick = onEditClick)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = HoneyAmber,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "Edit Profile",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = HoneyAmber
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Edit Profile",
                tint = Color.LightGray
            )
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

