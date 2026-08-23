package com.groomora.feature.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
fun CartScreen(
    viewModel: ProductViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val cartProducts = state.products.filter { state.cartItems.containsKey(it.id) }
    val totalAmount = cartProducts.sumOf { it.price * (state.cartItems[it.id] ?: 0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart") },
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
                Surface(shadowElevation = 8.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Amount", style = MaterialTheme.typography.labelLarge)
                            Text("₹$totalAmount", style = MaterialTheme.typography.headlineSmall, color = Charcoal)
                        }
                        Button(
                            onClick = { /* TODO: Checkout */ },
                            colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne)
                        ) {
                            Text("Checkout")
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (cartProducts.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Your cart is empty", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
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
                items(cartProducts) { product ->
                    val quantity = state.cartItems[product.id] ?: 0
                    CartItem(
                        product = product,
                        quantity = quantity,
                        onAdd = { viewModel.onIntent(ProductIntent.AddToCart(product.id)) },
                        onRemove = { viewModel.onIntent(ProductIntent.RemoveFromCart(product.id)) }
                    )
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
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(Color.LightGray, MaterialTheme.shapes.small)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text("₹${product.price}", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onRemove) {
                    Icon(
                        if (quantity > 1) Icons.Default.Delete else Icons.Default.Delete, // Should be minus icon but delete works for now
                        contentDescription = null,
                        tint = Charcoal
                    )
                }
                Text("$quantity", style = MaterialTheme.typography.titleMedium)
                IconButton(onClick = onAdd) {
                    Text("+", style = MaterialTheme.typography.headlineSmall) // Temporary plus
                }
            }
        }
    }
}
