package com.groomora.feature.loyalty

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.groomora.design.Champagne
import com.groomora.design.Charcoal
import com.groomora.design.WarmIvory
import com.groomora.design.components.GroomoraPrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    planId: String,
    amount: Double,
    address: String? = null,
    onPaymentComplete: () -> Unit,
    onBack: () -> Unit
) {
    var selectedMethod by remember { mutableStateOf("UPI") }
    var isProcessing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Complete Payment") },
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
                GroomoraPrimaryButton(
                    text = "Pay ₹${amount.toInt()}",
                    onClick = {
                        isProcessing = true
                        // In a real app, we would process payment and place order here
                        onPaymentComplete()
                    },
                    isLoading = isProcessing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp)
                )
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Order Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(12.dp))
                        
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            val label = if (address != null) "Product Order" else "Membership Plan ($planId)"
                            Text(label, color = Color.Gray)
                            Text("₹${amount.toInt()}", fontWeight = FontWeight.Bold)
                        }
                        
                        if (address != null) {
                            Spacer(Modifier.height(12.dp))
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
                            Spacer(Modifier.height(12.dp))
                            Text("Delivery Address", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Charcoal, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(address, style = MaterialTheme.typography.bodySmall)
                            }
                        }

                        HorizontalDivider(Modifier.padding(vertical = 12.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Payable", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("₹${amount.toInt()}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Charcoal)
                        }
                    }
                }
            }

            item {
                Text("Select Payment Method", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            item {
                PaymentMethodItem("UPI (Google Pay, PhonePe)", "UPI", selectedMethod == "UPI") { selectedMethod = "UPI" }
            }
            item {
                PaymentMethodItem("Credit / Debit Card", "CARD", selectedMethod == "CARD") { selectedMethod = "CARD" }
            }
            item {
                PaymentMethodItem("Net Banking", "NB", selectedMethod == "NB") { selectedMethod = "NB" }
            }
        }
    }
}

@Composable
fun PaymentMethodItem(label: String, value: String, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFFFBF9F5) else Color.White),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Charcoal) else null,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = Charcoal))
            Spacer(Modifier.width(12.dp))
            Text(label, style = MaterialTheme.typography.bodyLarge, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
        }
    }
}
