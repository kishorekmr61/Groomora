package com.groomora.design.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.design.*
import com.groomora.feature.discovery.Professional
import com.groomora.feature.discovery.Shop
import com.groomora.feature.shop.Service

@Composable
fun GroomoraCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    shape: Shape = MaterialTheme.shapes.large,
    containerColor: Color = Color.White,
    borderColor: Color? = BorderGray,
    elevation: Dp = 0.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = if (onClick != null) modifier.clickable(onClick = onClick) else modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = borderColor?.let { BorderStroke(1.dp, it) },
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        content = content
    )
}

@Composable
fun ShopCard(
    shop: Shop,
    onViewShop: () -> Unit,
    modifier: Modifier = Modifier,
    imageUrl: String = "https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=600&q=80",
    actionButtonText: String = "View Shop",
    isFavorite: Boolean = false,
    onFavoriteToggle: (() -> Unit)? = null
) {
    GroomoraCard(
        modifier = modifier.fillMaxWidth(),
        onClick = onViewShop
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Charcoal),
                contentAlignment = Alignment.Center
            ) {
                GroomoraImage(
                    url = if (shop.imageUrl.isNotEmpty()) shop.imageUrl else imageUrl,
                    contentDescription = shop.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = shop.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppText,
                    maxLines = 1
                )
                Spacer(Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = HoneyAmber,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text(
                        text = "${shop.rating} (${shop.reviewCount}) • ${shop.distance}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MutedText
                    )
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Haircut from ₹299",
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedText
                )
            }

            if (onFavoriteToggle != null) {
                IconButton(onClick = onFavoriteToggle) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) ErrorRed else MutedText
                    )
                }
            } else {
                Button(
                    onClick = onViewShop,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HoneyAmber,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = actionButtonText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ServiceItemCard(
    service: Service,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onSelect: () -> Unit,
    showRadio: Boolean = false,
    imageUrl: String? = null
) {
    GroomoraCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = MaterialTheme.shapes.medium,
        borderColor = if (isSelected) HoneyAmber else BorderGray
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(Charcoal),
                    contentAlignment = Alignment.Center
                ) {
                    if (imageUrl != null) {
                        GroomoraImage(
                            url = imageUrl,
                            contentDescription = service.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = HoneyAmber,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = service.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = AppText
                    )
                    Text(
                        text = "${service.duration} • ₹${service.price.toInt()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedText
                    )
                }
            }

            if (showRadio) {
                RadioButton(
                    selected = isSelected,
                    onClick = onSelect,
                    colors = RadioButtonDefaults.colors(selectedColor = HoneyAmber)
                )
            } else {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "₹${service.price.toInt()}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppText
                    )
                    Spacer(Modifier.width(6.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color.LightGray,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StylistCard(
    professional: Professional,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    onSelect: () -> Unit,
    showRadio: Boolean = false
) {
    GroomoraCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = MaterialTheme.shapes.large,
        borderColor = if (isSelected) DeepIndigo else BorderGray
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(DeepIndigo.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    if (professional.imageUrl.isNotEmpty()) {
                        GroomoraImage(
                            url = professional.imageUrl,
                            contentDescription = professional.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = professional.name.take(1),
                            fontWeight = FontWeight.Bold,
                            color = DeepIndigo,
                            fontSize = 20.sp
                        )
                    }
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        text = professional.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = professional.role,
                        style = MaterialTheme.typography.bodySmall,
                        color = MutedText
                    )
                    Spacer(Modifier.height(2.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = HoneyAmber,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = "${professional.rating}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                        if (professional.specialization.isNotEmpty()) {
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "• ${professional.specialization.joinToString(", ")}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MutedText,
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            if (showRadio) {
                RadioButton(
                    selected = isSelected,
                    onClick = onSelect,
                    colors = RadioButtonDefaults.colors(selectedColor = DeepIndigo)
                )
            } else {
                Button(
                    onClick = onSelect,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = HoneyAmber,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Book",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun ReviewCard(
    author: String,
    meta: String,
    comment: String,
    rating: Double = 5.0,
    modifier: Modifier = Modifier
) {
    GroomoraCard(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = author,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = meta,
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedText
                )
            }
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                repeat(rating.toInt().coerceIn(1, 5)) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        tint = HoneyAmber,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = comment,
                style = MaterialTheme.typography.bodyMedium,
                color = AppText
            )
        }
    }
}
