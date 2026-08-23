package com.groomora.design.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.design.*

@Composable
fun GroomoraHeadline(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AppText,
    fontWeight: FontWeight = FontWeight.Bold,
    textAlign: TextAlign = TextAlign.Start,
    fontSize: TextUnit = 24.sp,
    letterSpacing: TextUnit = 0.sp
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.headlineMedium.copy(
            fontSize = fontSize,
            letterSpacing = letterSpacing
        ),
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign
    )
}

@Composable
fun GroomoraTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AppText,
    fontWeight: FontWeight = FontWeight.Bold,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    fontSize: TextUnit = 18.sp
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.titleMedium.copy(fontSize = fontSize),
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = if (maxLines != Int.MAX_VALUE) TextOverflow.Ellipsis else TextOverflow.Clip
    )
}

@Composable
fun GroomoraBody(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AppText,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    lineHeight: TextUnit = 20.sp
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = lineHeight),
        color = color,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = if (maxLines != Int.MAX_VALUE) TextOverflow.Ellipsis else TextOverflow.Clip
    )
}

@Composable
fun GroomoraCaption(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MutedText,
    textAlign: TextAlign = TextAlign.Start,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLines: Int = Int.MAX_VALUE
) {
    Text(
        text = text,
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        textAlign = textAlign,
        fontWeight = fontWeight,
        maxLines = maxLines,
        overflow = if (maxLines != Int.MAX_VALUE) TextOverflow.Ellipsis else TextOverflow.Clip
    )
}

@Composable
fun GroomoraPriceText(
    amount: Double,
    modifier: Modifier = Modifier,
    originalAmount: Double? = null,
    color: Color = AppText,
    fontSize: TextUnit = 16.sp
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "₹${amount.toInt()}",
            style = MaterialTheme.typography.titleMedium.copy(fontSize = fontSize),
            fontWeight = FontWeight.Bold,
            color = color
        )
        if (originalAmount != null && originalAmount > amount) {
            Spacer(Modifier.width(6.dp))
            Text(
                text = "₹${originalAmount.toInt()}",
                style = MaterialTheme.typography.bodySmall.copy(
                    textDecoration = TextDecoration.LineThrough
                ),
                color = MutedText
            )
        }
    }
}

@Composable
fun GroomoraSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GroomoraTitle(text = title)
        if (actionText != null && onActionClick != null) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelMedium,
                color = HoneyAmber,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(onClick = onActionClick)
            )
        }
    }
}
