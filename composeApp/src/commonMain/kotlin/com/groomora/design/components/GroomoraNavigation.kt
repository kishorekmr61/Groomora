package com.groomora.design.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.groomora.design.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroomoraTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    containerColor: Color = WarmIvory,
    contentColor: Color = AppText
) {
    TopAppBar(
        title = {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedText
                    )
                }
            }
        },
        navigationIcon = {
            if (onBack != null) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back",
                        tint = contentColor
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            titleContentColor = contentColor,
            navigationIconContentColor = contentColor
        ),
        modifier = modifier
    )
}

@Composable
fun GroomoraBottomNav(
    currentRoute: String,
    onHomeClick: () -> Unit,
    onBookingsClick: () -> Unit,
    onOffersClick: () -> Unit,
    onWalletClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = AppText,
        tonalElevation = 8.dp,
        modifier = modifier
    ) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = HoneyAmber,
                selectedTextColor = HoneyAmber,
                indicatorColor = HoneyAmber.copy(alpha = 0.12f),
                unselectedIconColor = MutedText,
                unselectedTextColor = MutedText
            )
        )
        NavigationBarItem(
            selected = currentRoute == "bookings",
            onClick = onBookingsClick,
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Bookings") },
            label = { Text("Bookings") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = HoneyAmber,
                selectedTextColor = HoneyAmber,
                indicatorColor = HoneyAmber.copy(alpha = 0.12f),
                unselectedIconColor = MutedText,
                unselectedTextColor = MutedText
            )
        )
        NavigationBarItem(
            selected = currentRoute == "offers",
            onClick = onOffersClick,
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Offers") },
            label = { Text("Offers") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = HoneyAmber,
                selectedTextColor = HoneyAmber,
                indicatorColor = HoneyAmber.copy(alpha = 0.12f),
                unselectedIconColor = MutedText,
                unselectedTextColor = MutedText
            )
        )
        NavigationBarItem(
            selected = currentRoute == "wallet",
            onClick = onWalletClick,
            icon = { Icon(Icons.Default.Star, contentDescription = "Wallet") },
            label = { Text("Wallet") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = HoneyAmber,
                selectedTextColor = HoneyAmber,
                indicatorColor = HoneyAmber.copy(alpha = 0.12f),
                unselectedIconColor = MutedText,
                unselectedTextColor = MutedText
            )
        )
        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = onProfileClick,
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
            label = { Text("Profile") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = HoneyAmber,
                selectedTextColor = HoneyAmber,
                indicatorColor = HoneyAmber.copy(alpha = 0.12f),
                unselectedIconColor = MutedText,
                unselectedTextColor = MutedText
            )
        )
    }
}

@Composable
fun GroomoraSegmentedControl(
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = CircleShape,
        color = Color(0xFFE8E5DD),
        modifier = modifier.defaultMinSize(minHeight = 48.dp)
    ) {
        Row(
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            options.forEachIndexed { index, option ->
                val isSelected = selectedIndex == index
                Surface(
                    shape = CircleShape,
                    color = if (isSelected) Charcoal else Color.Transparent,
                    modifier = Modifier
                        .defaultMinSize(minWidth = 48.dp, minHeight = 40.dp)
                        .selectable(
                            selected = isSelected,
                            role = Role.Tab,
                            onClick = { onOptionSelected(index) }
                        )
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = option,
                        color = if (isSelected) Color.White else AppText,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun GroomoraFilterChip(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Surface(
        shape = CircleShape,
        color = if (isSelected) HoneyAmber else Color.White,
        border = if (isSelected) null else BorderStroke(1.dp, BorderGray),
        modifier = modifier
            .defaultMinSize(minHeight = 48.dp)
            .selectable(
                selected = isSelected,
                role = Role.Checkbox,
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else AppText,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(6.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = if (isSelected) Color.White else AppText,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
