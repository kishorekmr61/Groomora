package com.groomora.design

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Charcoal = Color(0xFF1C1C1C)
val NearBlack = Color(0xFF111111)
val WarmGold = Color(0xFFC9A227)
val Champagne = Color(0xFFE5C76B)
val WarmIvory = Color(0xFFFAF8F3)
val AppText = Color(0xFF202020)

private val GroomoraColors = lightColorScheme(
    primary = WarmGold,
    onPrimary = NearBlack,
    secondary = Champagne,
    background = WarmIvory,
    surface = Color.White,
    onBackground = AppText,
    onSurface = AppText
)

@Composable
fun GroomoraTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = GroomoraColors, content = content)
}
