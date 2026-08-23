package com.groomora.design.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.design.*

@Composable
fun GroomoraRatingBadge(
    rating: Double,
    reviewCount: Int? = null,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = HoneyAmber,
            modifier = Modifier.size(14.dp)
        )
        Spacer(Modifier.width(2.dp))
        Text(
            text = "$rating",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = AppText
        )
        if (reviewCount != null) {
            Spacer(Modifier.width(2.dp))
            Text(
                text = "($reviewCount)",
                style = MaterialTheme.typography.labelSmall,
                color = MutedText
            )
        }
    }
}

@Composable
fun GroomoraStatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    statusColor: Color = SuccessGreen
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(statusColor)
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = statusColor,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun GroomoraAmenityChip(
    label: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = CircleShape,
        color = Color.White,
        border = BorderStroke(1.dp, BorderGray),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = SuccessGreen,
                modifier = Modifier.size(14.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = AppText
            )
        }
    }
}

@Composable
fun GroomoraSummaryRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isBold: Boolean = false,
    valueColor: Color = AppText
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isBold) AppText else MutedText,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold
        )
    }
}

@Composable
fun GroomoraLoadingState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = HoneyAmber)
    }
}

@Composable
fun GroomoraEmptyState(
    title: String,
    message: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Info,
    actionButtonText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color.LightGray
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = AppText
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText,
                textAlign = TextAlign.Center
            )
            if (actionButtonText != null && onActionClick != null) {
                Spacer(Modifier.height(20.dp))
                GroomoraPrimaryButton(
                    text = actionButtonText,
                    onClick = onActionClick
                )
            }
        }
    }
}

@Composable
fun GroomoraErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    GroomoraEmptyState(
        title = "Something went wrong",
        message = message,
        actionButtonText = "Try Again",
        onActionClick = onRetry,
        modifier = modifier
    )
}
