package com.groomora.feature.products

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.app.DependencyContainer
import com.groomora.design.Champagne
import com.groomora.design.Charcoal
import com.groomora.design.WarmGold
import com.groomora.design.WarmIvory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: ProductViewModel,
    onNavigateToOrders: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val cartProducts = state.products.filter { state.cartItems.containsKey(it.id) }
    val subtotal = cartProducts.sumOf { it.price * (state.cartItems[it.id] ?: 0) }
    val deliveryFee = if (subtotal > 999.0 || subtotal == 0.0) 0.0 else 49.0
    val totalAmount = subtotal + deliveryFee

    var showCheckoutDialog by remember { mutableStateOf(false) }
    var selectedPaymentMethod by remember { mutableStateOf("UPI (Google Pay / PhonePe)") }
    var deliveryAddress by remember { mutableStateOf("221B Baker Street, Flat 4A, Bangalore - 560001") }
    var isPlacingOrder by remember { mutableStateOf(false) }
    var placedOrder by remember { mutableStateOf<Order?>(null) }

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
            if (cartProducts.isNotEmpty() && placedOrder == null) {
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
                            onClick = { showCheckoutDialog = true },
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
        if (placedOrder != null) {
            val order = placedOrder!!
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarmIvory)
                    .padding(padding)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color(0xFF2E7D5B), androidx.compose.foundation.shape.CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack, // Or checkmark
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(0.dp) // dummy for layout
                            )
                            Text("✓", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                        }

                        Text("Order Placed Successfully!", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Charcoal)
                        Text(
                            "Thank you for shopping with Groomora. Your order has been placed and is being prepared.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )

                        Surface(
                            color = WarmIvory,
                            shape = MaterialTheme.shapes.medium,
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Order ID", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                    Text(order.id, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Charcoal)
                                }
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Total Paid", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                    Text("₹${order.totalAmount.toInt()}", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFFC5A059))
                                }
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Payment", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                    Text(order.paymentMethod, style = MaterialTheme.typography.labelMedium, color = Charcoal)
                                }
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Items", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                                    Text("${order.items.sumOf { it.quantity }} item(s)", style = MaterialTheme.typography.labelMedium, color = Charcoal)
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))

                        Button(
                            onClick = onNavigateToOrders,
                            colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne),
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("View in My Orders", modifier = Modifier.padding(vertical = 4.dp))
                        }

                        OutlinedButton(
                            onClick = onBack,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Continue Shopping", color = Charcoal)
                        }
                    }
                }
            }
        } else if (cartProducts.isEmpty()) {
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

    // Checkout Confirmation Dialog
    if (showCheckoutDialog) {
        AlertDialog(
            onDismissRequest = { if (!isPlacingOrder) showCheckoutDialog = false },
            title = { Text("Complete Order", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Text("Delivery Address:", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    Surface(
                        color = WarmIvory,
                        shape = MaterialTheme.shapes.small,
                        border = BorderStroke(1.dp, Color.LightGray)
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Charcoal, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(deliveryAddress, style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Text("Payment Method:", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("UPI (Google Pay / PhonePe)", "Credit / Debit Card", "Cash on Delivery").forEach { method ->
                            Surface(
                                onClick = { selectedPaymentMethod = method },
                                shape = MaterialTheme.shapes.small,
                                color = if (selectedPaymentMethod == method) Charcoal.copy(alpha = 0.08f) else Color.White,
                                border = BorderStroke(1.dp, if (selectedPaymentMethod == method) Charcoal else Color.LightGray)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp).fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = selectedPaymentMethod == method,
                                        onClick = { selectedPaymentMethod = method },
                                        colors = RadioButtonDefaults.colors(selectedColor = Charcoal)
                                    )
                                    Spacer(Modifier.width(6.dp))
                                    Text(method, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }

                    Text("Amount to Pay: ₹${totalAmount.toInt()}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Charcoal)
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        isPlacingOrder = true
                        coroutineScope.launch {
                            val items = cartProducts.map { p ->
                                OrderItem(productId = p.id, productName = p.name, quantity = state.cartItems[p.id] ?: 1, price = p.price)
                            }
                            val order = DependencyContainer.orderRepository.placeOrder(
                                items = items,
                                totalAmount = totalAmount,
                                address = deliveryAddress,
                                paymentMethod = selectedPaymentMethod
                            )
                            viewModel.onIntent(ProductIntent.ClearCart)
                            isPlacingOrder = false
                            showCheckoutDialog = false
                            placedOrder = order
                        }
                    },
                    enabled = !isPlacingOrder,
                    colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne)
                ) {
                    if (isPlacingOrder) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Champagne)
                    } else {
                        Text("Place Order & Pay")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCheckoutDialog = false },
                    enabled = !isPlacingOrder
                ) {
                    Text("Back to Cart")
                }
            }
        )
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
