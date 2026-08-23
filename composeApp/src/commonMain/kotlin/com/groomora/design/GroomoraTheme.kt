package com.groomora.design

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Brand Colors
val Charcoal = Color(0xFF1C1C1C)
val NearBlack = Color(0xFF111111)
val WarmGold = Color(0xFFC9A227)
val Champagne = Color(0xFFE5C76B)
val WarmIvory = Color(0xFFFAF8F3)
val AppText = Color(0xFF202020)
val MutedText = Color(0xFF6B6B6B)
val SuccessGreen = Color(0xFF2E7D5B)
val ErrorRed = Color(0xFFB23A48)
val DividerGray = Color(0xFFE5E2DC)

// Category Accents
val BarbershopGold = Color(0xFFC9A227)
val SpaPlum = Color(0xFF673AB7) // Placeholder for Plum
val BeautyRose = Color(0xFFE91E63) // Placeholder for Rose
val BridalBurgundy = Color(0xFF800020)
val HomeServiceTeal = Color(0xFF008080)

private val GroomoraLightColors = lightColorScheme(
    primary = WarmGold,
    onPrimary = NearBlack,
    secondary = Champagne,
    onSecondary = Charcoal,
    background = WarmIvory,
    onBackground = AppText,
    surface = Color.White,
    onSurface = AppText,
    outline = DividerGray,
    error = ErrorRed,
    onError = Color.White
)

private val GroomoraDarkColors = darkColorScheme(
    primary = Champagne,
    onPrimary = NearBlack,
    secondary = WarmGold,
    onSecondary = NearBlack,
    background = NearBlack,
    onBackground = WarmIvory,
    surface = Charcoal,
    onSurface = WarmIvory,
    outline = MutedText,
    error = ErrorRed,
    onError = Color.White
)

val GroomoraTypography = Typography(
    headlineLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    )
)

val GroomoraShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

@Composable
fun GroomoraTheme(
    darkTheme: Boolean = false, // TODO: Observe system/preference
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) GroomoraDarkColors else GroomoraLightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = GroomoraTypography,
        shapes = GroomoraShapes,
        content = content
    )
}
