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

// Prototype Brand Colors
val HoneyAmber = Color(0xFFC58A38)
val AmberGold = Color(0xFFC99742)
val Charcoal = Color(0xFF1C1C1C)
val NearBlack = Color(0xFF111111)
val WarmIvory = Color(0xFFF8F6F2)
val WarmGold = Color(0xFFC58A38)
val Champagne = Color(0xFFE5C76B)
val DeepIndigo = Color(0xFF1B3B6F)
val TealGreen = Color(0xFF1B6B50)
val PlumPurple = Color(0xFF543884)
val AppText = Color(0xFF202020)
val MutedText = Color(0xFF6B6B6B)
val SuccessGreen = Color(0xFF2E7D5B)
val ErrorRed = Color(0xFFB23A48)
val DividerGray = Color(0xFFE5E2DC)
val BorderGray = Color(0xFFEBE7DE)
val CardWhite = Color(0xFFFFFFFF)

// Category Accents
val BarbershopGold = Color(0xFFC58A38)
val SpaPlum = Color(0xFF543884)
val BeautyRose = Color(0xFFD63384)
val BridalBurgundy = Color(0xFF800020)
val HomeServiceTeal = Color(0xFF1B6B50)

private val GroomoraLightColors = lightColorScheme(
    primary = HoneyAmber,
    onPrimary = Color.White,
    secondary = Charcoal,
    onSecondary = Color.White,
    background = WarmIvory,
    onBackground = AppText,
    surface = CardWhite,
    onSurface = AppText,
    outline = BorderGray,
    error = ErrorRed,
    onError = Color.White
)

private val GroomoraDarkColors = darkColorScheme(
    primary = AmberGold,
    onPrimary = NearBlack,
    secondary = HoneyAmber,
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
        fontSize = 30.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        letterSpacing = 0.sp
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        letterSpacing = 0.sp
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        letterSpacing = 0.1.sp
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        letterSpacing = 0.3.sp
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.2.sp
    ),
    bodySmall = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.2.sp
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    ),
    labelMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.1.sp
    ),
    labelSmall = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 0.1.sp
    )
)

val GroomoraShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(18.dp),
    extraLarge = RoundedCornerShape(26.dp)
)

@Composable
fun GroomoraTheme(
    darkTheme: Boolean = false,
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
