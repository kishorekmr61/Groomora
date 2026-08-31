package com.groomora.feature.onboarding

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.design.Charcoal
import com.groomora.design.HoneyAmber
import com.groomora.design.WarmIvory
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    var startAnimation by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2000) // 2 second delay for branding
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Logo Placeholder (Stylized "G")
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(Charcoal),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    color = HoneyAmber,
                    style = MaterialTheme.typography.displayLarge,
                    fontWeight = FontWeight.Bold,
                    fontSize = 72.sp
                )
            }

            Spacer(Modifier.height(24.dp))

            // Brand Name
            Text(
                text = "Groomora",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Charcoal,
                modifier = Modifier.scale(scale)
            )

            Spacer(Modifier.height(8.dp))

            // Tagline
            Text(
                text = "Elevate Your Grooming Experience",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.scale(scale)
            )
        }
    }
}
