package com.groomora.feature.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.groomora.app.DependencyContainer
import com.groomora.core.location.Address
import com.groomora.design.Champagne
import com.groomora.design.Charcoal
import com.groomora.design.WarmIvory
import com.groomora.design.components.GroomoraPrimaryButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCheckoutScreen(
    viewModel: ProductViewModel,
    onNavigateToPayment: (Int, String) -> Unit,
    onAddNewAddress: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val addresses by DependencyContainer.locationRepository.getSavedAddresses().collectAsState(initial = emptyList())
    var selectedAddress by remember { mutableStateOf<Address?>(null) }

    val cartProducts = state.products.filter { state.cartItems.containsKey(it.id) }
    val subtotal = cartProducts.sumOf { it.price * (state.cartItems[it.id] ?: 0) }
    val deliveryFee = if (subtotal > 999.0 || subtotal == 0.0) 0.0 else 49.0
    val totalAmount = subtotal + deliveryFee

    LaunchedEffect(addresses) {
        if (selectedAddress == null && addresses.isNotEmpty()) {
            selectedAddress = addresses.find { it.isDefault } ?: addresses.first()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Delivery Address") },
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
                    text = "Deliver to this Address",
                    enabled = selectedAddress != null,
                    onClick = {
                        selectedAddress?.let { addr ->
                            onNavigateToPayment(totalAmount.toInt(), addr.fullAddress)
                        }
                    },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Saved Addresses", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            if (addresses.isEmpty()) {
                item {
                    Text("No saved addresses found. Please add one to continue.", color = Color.Gray)
                }
            } else {
                items(addresses) { address ->
                    AddressSelectionItem(
                        address = address,
                        isSelected = selectedAddress?.id == address.id,
                        onClick = { selectedAddress = address }
                    )
                }
            }

            item {
                OutlinedButton(
                    onClick = onAddNewAddress,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Charcoal)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Add New Address")
                }
            }

            item {
                Spacer(Modifier.height(8.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Order Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Payable", color = Color.Gray)
                            Text("₹${totalAmount.toInt()}", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddressSelectionItem(address: Address, isSelected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) Color(0xFFFBF9F5) else Color.White),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, Charcoal) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(selected = isSelected, onClick = onClick, colors = RadioButtonDefaults.colors(selectedColor = Charcoal))
            Spacer(Modifier.width(12.dp))
            Column {
                Text(address.label ?: "Address", fontWeight = FontWeight.Bold)
                Text(address.fullAddress, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
        }
    }
}
