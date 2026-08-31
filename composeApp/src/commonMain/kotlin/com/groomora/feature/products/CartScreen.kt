package com.groomora.feature.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.groomora.design.Champagne
import com.groomora.design.Charcoal
import com.groomora.design.WarmGold
import com.groomora.design.WarmIvory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: ProductViewModel,
    onNavigateToCheckout: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val cartProducts = state.products.filter { state.cartItems.containsKey(it.id) }
    val subtotal = cartProducts.sumOf { it.price * (state.cartItems[it.id] ?: 0) }
    val deliveryFee = if (subtotal > 999.0 || subtotal == 0.0) 0.0 else 49.0
    val totalAmount = subtotal + deliveryFee

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart (${cartProducts.sumOf { state.cartItems[it.id] ?: 0 }})") },
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
            if (cartProducts.isNotEmpty()) {
                Surface(shadowElevation = 8.dp, color = Color.White) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Amount", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                            Text("₹${totalAmount.toInt()}", style = MaterialTheme.typography.headlineSmall, color = Charcoal, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = onNavigateToCheckout,
                            colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Proceed to Checkout", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (cartProducts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarmIvory)
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.LightGray)
                    Text("Your cart is currently empty", style = MaterialTheme.typography.titleMedium, color = Color.Gray)
                    OutlinedButton(onClick = onBack) {
                        Text("Browse Grooming Products", color = Charcoal)
                    }
                }
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
                // Free delivery incentive banner
                item {
                    Surface(
                        color = if (deliveryFee == 0.0) Color(0xFF2E7D5B).copy(alpha = 0.12f) else WarmGold.copy(alpha = 0.15f),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = if (deliveryFee == 0.0) "🎉 You have unlocked Free Standard Delivery!" else "Add ₹${(999 - subtotal).toInt()} more to get Free Delivery",
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = if (deliveryFee == 0.0) Color(0xFF2E7D5B) else Charcoal
                        )
                    }
                }

                items(cartProducts) { product ->
                    val quantity = state.cartItems[product.id] ?: 0
                    CartItem(
                        product = product,
                        quantity = quantity,
                        onAdd = { viewModel.onIntent(ProductIntent.AddToCart(product.id)) },
                        onRemove = { viewModel.onIntent(ProductIntent.RemoveFromCart(product.id)) }
                    )
                }

                // Price Summary Breakdown
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Order Summary", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Items Subtotal", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                                Text("₹${subtotal.toInt()}", style = MaterialTheme.typography.bodyMedium)
                            }
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Delivery Fee", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                                Text(if (deliveryFee == 0.0) "FREE" else "₹${deliveryFee.toInt()}", style = MaterialTheme.typography.bodyMedium, color = if (deliveryFee == 0.0) Color(0xFF2E7D5B) else Charcoal)
                            }
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Grand Total", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("₹${totalAmount.toInt()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Charcoal)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartItem(
    product: Product,
    quantity: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(WarmIvory, MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center
            ) {
                Text(product.name.take(2).uppercase(), color = Charcoal, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
                Text("₹${product.price.toInt()} each", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Decrease", tint = Color.Gray, modifier = Modifier.size(18.dp))
                }
                Text("$quantity", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                IconButton(onClick = onAdd, modifier = Modifier.size(32.dp)) {
                    Text("+", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Charcoal)
                }
            }
        }
    }
}
