package com.groomora.design.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.groomora.design.*

@Composable
fun GroomoraPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    icon: ImageVector? = null,
    containerColor: Color = HoneyAmber,
    contentColor: Color = Color.White,
    height: Dp = 50.dp
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .height(height),
        enabled = enabled && !isLoading,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.5f),
            disabledContentColor = contentColor.copy(alpha = 0.7f)
        ),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = contentColor,
                modifier = Modifier
                    .size(22.dp)
                    .semantics { contentDescription = "Loading, please wait" },
                strokeWidth = 2.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null, // decorative when text is present
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun GroomoraSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    height: Dp = 48.dp
) {
    GroomoraPrimaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        icon = icon,
        containerColor = Charcoal,
        contentColor = Color.White,
        height = height
    )
}

@Composable
fun GroomoraOutlinedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    borderColor: Color = BorderGray,
    contentColor: Color = AppText,
    height: Dp = 48.dp
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .height(height),
        enabled = enabled,
        shape = CircleShape,
        border = BorderStroke(1.dp, borderColor),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null, // decorative
                    modifier = Modifier.size(16.dp),
                    tint = contentColor
                )
                Spacer(Modifier.width(6.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = contentColor
            )
        }
    }
}

@Composable
fun GroomoraCircularActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconColor: Color = AppText,
    backgroundColor: Color = Color.White,
    borderColor: Color = BorderGray,
    size: Dp = 48.dp
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
            .clickable(
                role = Role.Button,
                onClickLabel = label,
                onClick = onClick
            )
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(backgroundColor)
                .border(1.dp, borderColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(size * 0.48f)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = AppText,
            fontWeight = FontWeight.Medium
        )
    }
}
