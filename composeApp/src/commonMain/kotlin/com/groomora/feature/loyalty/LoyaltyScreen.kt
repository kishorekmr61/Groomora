package com.groomora.feature.loyalty

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
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
import com.groomora.design.ads.GroomoraRewardedAdCard
import com.groomora.app.DependencyContainer


import com.groomora.design.components.GroomoraBottomNav

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoyaltyScreen(
    viewModel: LoyaltyViewModel,
    onNavigateToHome: () -> Unit = {},
    onNavigateToBookings: () -> Unit = {},
    onNavigateToOffers: () -> Unit = {},
    onNavigateToWallet: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToPayment: (String, Int) -> Unit = { _, _ -> },
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Loyalty & Rewards") },
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
            GroomoraBottomNav(
                currentRoute = "wallet",
                onHomeClick = onNavigateToHome,
                onBookingsClick = onNavigateToBookings,
                onOffersClick = onNavigateToOffers,
                onWalletClick = onNavigateToWallet,
                onProfileClick = onNavigateToProfile
            )
        }
    )
 { padding ->
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
                state.profile?.let { profile ->
                    item {
                        LoyaltyPointsCard(profile)
                    }
                    
                    item {
                        ReferralCard(profile.referralCode)
                    }

                    // Google Ad Manager Rewarded Video Ad
                    item {
                        GroomoraRewardedAdCard(
                            rewardPoints = 50,
                            onWatchAd = {
                                DependencyContainer.adManager.showRewarded(
                                    onRewarded = { amount, _ ->
                                        viewModel.redeemPoints(-amount) // Add reward points
                                    }
                                )

                            }
                        )
                    }
                }

                item {

                    Text("Membership Plans", style = MaterialTheme.typography.titleLarge)
                }
                items(state.membershipPlans) { plan ->
                    MembershipPlanItem(plan, onJoin = { onNavigateToPayment(plan.id, plan.price.toInt()) })
                }

                item {
                    Text("Transaction History", style = MaterialTheme.typography.titleLarge)
                }
                items(state.transactions) { transaction ->
                    TransactionItem(transaction)
                }
            }
        }
    }
}

@Composable
fun LoyaltyPointsCard(profile: LoyaltyProfile) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Charcoal),
        shape = MaterialTheme.shapes.extraLarge
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Points Balance", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelLarge)
            Text(
                "${profile.pointsBalance}",
                color = Champagne,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                color = Champagne.copy(alpha = 0.2f)
            ) {
                Text(
                    "${profile.memberLevel} Member",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    color = Champagne,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { 0.6f }, // Mock progress
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = Champagne,
                trackColor = Color.DarkGray,
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            Text(
                "${profile.pointsToNextLevel} points to Platinum",
                color = Color.White.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ReferralCard(code: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Refer & Earn", style = MaterialTheme.typography.titleMedium)
                Text("Get 500 points per referral", style = MaterialTheme.typography.bodySmall)
            }
            Button(
                onClick = { /* Share code */ },
                colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne)
            ) {
                Text(code)
            }
        }
    }
}

@Composable
fun MembershipPlanItem(plan: MembershipPlan, onJoin: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(plan.name, style = MaterialTheme.typography.titleMedium)
                Text("₹${plan.price}", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(8.dp))
            plan.benefits.forEach { benefit ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(14.dp), tint = Champagne)
                    Spacer(Modifier.width(8.dp))
                    Text(benefit, style = MaterialTheme.typography.bodySmall)
                }
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = onJoin,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Charcoal)
            ) {
                Text("Join for ${plan.durationMonths} Months")
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: LoyaltyTransaction) {
    ListItem(
        headlineContent = { Text(transaction.description) },
        supportingContent = { Text(transaction.date) },
        trailingContent = {
            Text(
                text = "${if (transaction.type == TransactionType.EARNED || transaction.type == TransactionType.REFERRAL_BONUS) "+" else "-"}${transaction.points}",
                color = if (transaction.type == TransactionType.EARNED || transaction.type == TransactionType.REFERRAL_BONUS) Color(0xFF2E7D5B) else Color.Red,
                style = MaterialTheme.typography.titleMedium
            )
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
}
