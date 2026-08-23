package com.groomora.feature.products

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.groomora.app.DependencyContainer
import com.groomora.design.*
import com.groomora.design.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    viewModel: OrdersViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        DependencyContainer.analyticsManager.logScreenView("orders_screen")
    }

    Scaffold(
        topBar = {
            GroomoraTopAppBar(
                title = "My Orders",
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .padding(padding)
        ) {
            if (state.message != null) {
                Surface(
                    color = HoneyAmber.copy(alpha = 0.15f),
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    GroomoraBody(
                        text = state.message!!,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            if (state.isLoading) {
                GroomoraLoadingState()
            } else if (state.orders.isEmpty()) {
                GroomoraEmptyState(
                    title = "No Orders Placed Yet",
                    message = "Discover grooming essentials, hair waxes, beard oils, and skin kits.",
                    icon = Icons.Default.ShoppingCart,
                    actionButtonText = "Shop Products",
                    onActionClick = onBack
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.orders) { order ->
                        OrderCard(order = order)
                    }
                }
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order
) {
    GroomoraCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GroomoraCaption(text = "ID: ${order.id}", fontWeight = FontWeight.Bold)
                OrderStatusBadge(order.status)
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                order.items.forEach { item ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        GroomoraBody(
                            text = "${item.quantity}x ${item.productName}",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        GroomoraPriceText(amount = item.price * item.quantity)
                    }
                }
            }

            Surface(
                color = WarmIvory,
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(1.dp, BorderGray)
            ) {
                Column(modifier = Modifier.padding(10.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    GroomoraCaption(text = "Delivering To: ${order.deliveryAddress}", color = AppText)
                    GroomoraCaption(text = "Payment: ${order.paymentMethod}")
                }
            }

            // Courier Tracking (Only shown if courier number is provided)
            if (!order.trackingNumber.isNullOrBlank()) {
                HorizontalDivider(color = BorderGray)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Courier Tracking",
                        modifier = Modifier.size(16.dp),
                        tint = TealGreen
                    )
                    Spacer(Modifier.width(6.dp))
                    GroomoraCaption(
                        text = "Courier Track: ${order.trackingNumber}",
                        color = TealGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            HorizontalDivider(color = BorderGray)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    GroomoraCaption(text = "Total Paid")
                    GroomoraPriceText(amount = order.totalAmount)
                }
            }
        }
    }
}

@Composable
fun OrderStatusBadge(status: OrderStatus) {
    val (color, label) = when (status) {
        OrderStatus.PLACED -> HoneyAmber to "Placed"
        OrderStatus.PROCESSING -> HoneyAmber to "Processing"
        OrderStatus.SHIPPED -> TealGreen to "Shipped"
        OrderStatus.DELIVERED -> SuccessGreen to "Delivered"
        OrderStatus.CANCELLED -> ErrorRed to "Cancelled"
        OrderStatus.REFUNDED -> Color.Gray to "Refunded"
    }

    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = color.copy(alpha = 0.15f)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}
