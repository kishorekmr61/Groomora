package com.groomora.feature.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.design.*
import com.groomora.design.GroomoraImage
import com.groomora.design.components.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    viewModel: ProductViewModel,
    onNavigateToProductDetails: (String) -> Unit,
    onNavigateToCart: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val cartCount = state.cartItems.values.sum()

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var pageLimit by remember { mutableIntStateOf(6) }
    var isPagingLoading by remember { mutableStateOf(false) }

    val categories = listOf("All", "Hair Care", "Skincare", "Beard Grooming", "Spa & Wellness")

    val filteredProducts = remember(state.products, selectedCategory, searchQuery) {
        state.products.filter { product ->
            val matchesCategory = when (selectedCategory) {
                "All" -> true
                "Hair Care" -> product.category == "hair"
                "Skincare" -> product.category == "skin"
                "Beard Grooming" -> product.category == "grooming"
                "Spa & Wellness" -> product.category == "spa"
                else -> product.category.equals(selectedCategory, ignoreCase = true)
            }
            val matchesQuery = searchQuery.isBlank() ||
                product.name.contains(searchQuery, ignoreCase = true) ||
                product.brand.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }

    val pagedProducts = filteredProducts.take(pageLimit)
    val gridState = rememberLazyGridState()

    // Auto load more pagination on scroll
    LaunchedEffect(gridState, filteredProducts.size, pageLimit) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastVisibleIndex ->
                if (lastVisibleIndex != null &&
                    lastVisibleIndex >= pagedProducts.size - 2 &&
                    pageLimit < filteredProducts.size &&
                    !isPagingLoading
                ) {
                    isPagingLoading = true
                    delay(300) // smooth pagination throttle
                    pageLimit = (pageLimit + 4).coerceAtMost(filteredProducts.size)
                    isPagingLoading = false
                }
            }
    }

    Scaffold(
        topBar = {
            GroomoraTopAppBar(
                title = "Beauty & Grooming Store",
                subtitle = "${filteredProducts.size} salon products available",
                onBack = onBack,
                actions = {
                    BadgedBox(
                        badge = {
                            if (cartCount > 0) {
                                Badge(containerColor = HoneyAmber, contentColor = Color.White) {
                                    Text("$cartCount", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = onNavigateToCart) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Shopping Cart", tint = AppText)
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            GroomoraLoadingState()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarmIvory)
                    .padding(padding)
            ) {
                // Search Bar
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    GroomoraSearchBar(
                        query = searchQuery,
                        onQueryChange = {
                            searchQuery = it
                            pageLimit = 6
                        },
                        placeholder = "Search shampoo, beard oil, face wash..."
                    )
                }

                // Category Tabs Row
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { cat ->
                        GroomoraFilterChip(
                            label = cat,
                            isSelected = selectedCategory == cat,
                            onClick = {
                                selectedCategory = cat
                                pageLimit = 6
                            }
                        )
                    }
                }

                // Products Vertical Grid with Pagination
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(pagedProducts) { product ->
                        val inCartQuantity = state.cartItems[product.id] ?: 0
                        ProductGridCard(
                            product = product,
                            inCartQuantity = inCartQuantity,
                            onClick = { onNavigateToProductDetails(product.id) },
                            onAddToCart = { viewModel.onIntent(ProductIntent.AddToCart(product.id)) }
                        )
                    }

                    // Pagination Footer Indicator / Load More
                    if (pageLimit < filteredProducts.size) {
                        item(span = { GridItemSpan(2) }) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                if (isPagingLoading) {
                                    CircularProgressIndicator(
                                        color = HoneyAmber,
                                        modifier = Modifier.size(24.dp),
                                        strokeWidth = 2.5.dp
                                    )
                                    Spacer(Modifier.height(6.dp))
                                    Text(
                                        "Loading more beauty essentials...",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MutedText
                                    )
                                } else {
                                    GroomoraOutlinedButton(
                                        text = "Load More Products (${pagedProducts.size}/${filteredProducts.size})",
                                        onClick = {
                                            pageLimit = (pageLimit + 4).coerceAtMost(filteredProducts.size)
                                        },
                                        height = 38.dp
                                    )
                                }
                            }
                        }
                    } else if (filteredProducts.isNotEmpty()) {
                        item(span = { GridItemSpan(2) }) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "You've reached the end of the catalog (${filteredProducts.size} items)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MutedText
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductGridCard(
    product: Product,
    inCartQuantity: Int,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    GroomoraCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        containerColor = Color.White
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Charcoal),
                contentAlignment = Alignment.Center
            ) {
                GroomoraImage(
                    url = product.imageUrl.ifBlank { "https://images.unsplash.com/photo-1608248597359-009eb73d6d03?w=500&q=80" },
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Discount badge
                if (product.originalPrice != null && product.originalPrice > product.price) {
                    val discountPercent = (((product.originalPrice - product.price) / product.originalPrice) * 100).toInt()
                    Surface(
                        color = ErrorRed,
                        shape = CircleShape,
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "$discountPercent% OFF",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 9.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = product.brand,
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedText,
                    maxLines = 1
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = AppText,
                    maxLines = 1
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "₹${product.price.toInt()}",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = HoneyAmber
                    )
                    if (product.originalPrice != null) {
                        Text(
                            text = "₹${product.originalPrice.toInt()}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MutedText,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
                Spacer(Modifier.height(10.dp))

                if (inCartQuantity > 0) {
                    Surface(
                        color = HoneyAmber.copy(alpha = 0.15f),
                        shape = CircleShape,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = HoneyAmber, modifier = Modifier.size(14.dp))
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "$inCartQuantity in Cart",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = HoneyAmber
                            )
                        }
                    }
                } else {
                    GroomoraPrimaryButton(
                        text = "Add to Cart",
                        onClick = onAddToCart,
                        height = 34.dp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
