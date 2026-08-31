package com.groomora.feature.products

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.groomora.design.Champagne
import com.groomora.design.Charcoal
import com.groomora.design.GroomoraImage
import com.groomora.design.WarmIvory
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: String,
    viewModel: ProductViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val product = state.products.find { it.id == productId }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(product?.name ?: "Product Details") },
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            product?.let {
                Surface(shadowElevation = 8.dp) {
                    Button(
                        onClick = { 
                            viewModel.onIntent(ProductIntent.AddToCart(it.id))
                            scope.launch {
                                snackbarHostState.showSnackbar("Added to Cart")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Charcoal, contentColor = Champagne)
                    ) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Add to Cart • ₹${it.price}")
                    }
                }
            }
        }
    ) { padding ->
        if (product == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Charcoal)
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarmIvory)
                    .padding(padding)
            ) {
                item {
                    ProductImageHeader(product)
                }

                item {
                    ProductInfoSection(product)
                }
                
                item {
                    ProductDescription(product.description)
                }
                
                item {
                    Spacer(Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun ProductImageHeader(product: Product) {
    GroomoraImage(
        url = product.imageUrl,
        contentDescription = product.name,
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun ProductInfoSection(product: Product) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = product.brand,
            style = MaterialTheme.typography.labelLarge,
            color = Color.Gray
        )
        Text(
            text = product.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(Modifier.height(16.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "₹${product.price}",
                style = MaterialTheme.typography.headlineSmall,
                color = Charcoal,
                fontWeight = FontWeight.Bold
            )
            if (product.originalPrice != null) {
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "₹${product.originalPrice}",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Gray,
                    textDecoration = TextDecoration.LineThrough
                )
                Spacer(Modifier.width(12.dp))
                val discount = ((product.originalPrice - product.price) / product.originalPrice * 100).toInt()
                Text(
                    text = "$discount% OFF",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF2E7D5B)
                )
            }
        }
        
        Spacer(Modifier.height(8.dp))
        Surface(
            shape = MaterialTheme.shapes.extraSmall,
            color = if (product.stockStatus == StockStatus.IN_STOCK) Color(0xFF2E7D5B).copy(alpha = 0.1f) else Color.Red.copy(alpha = 0.1f)
        ) {
            Text(
                text = product.stockStatus.name.replace("_", " "),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = if (product.stockStatus == StockStatus.IN_STOCK) Color(0xFF2E7D5B) else Color.Red
            )
        }
    }
}

@Composable
fun ProductDescription(description: String) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Product Details", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.DarkGray,
            lineHeight = androidx.compose.ui.unit.TextUnit.Unspecified // Default
        )
    }
}
