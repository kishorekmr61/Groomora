package com.groomora.core.network

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.app.DependencyContainer
import com.groomora.design.*
import com.groomora.design.components.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

interface NetworkConnectivityManager {
    val isConnected: StateFlow<Boolean>
    fun setConnected(connected: Boolean)
}

class DefaultNetworkConnectivityManager : NetworkConnectivityManager {
    private val _isConnected = MutableStateFlow(true)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    override fun setConnected(connected: Boolean) {
        _isConnected.value = connected
    }
}

@Composable
fun rememberNetworkConnectivity(): Boolean {
    val isConnected by DependencyContainer.networkConnectivityManager.isConnected.collectAsState()
    return isConnected
}

@Composable
fun NetworkOfflineBanner(
    isConnected: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = !isConnected,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(ErrorRed)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Offline",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "No Internet Connection. Please check your network.",
                color = Color.White,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun NoInternetScreen(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRetrying by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        DependencyContainer.analyticsManager.logScreenView("no_internet_screen")
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmIvory)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        GroomoraCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Offline Icon Badge
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(ErrorRed.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "No Internet",
                        tint = ErrorRed,
                        modifier = Modifier.size(44.dp)
                    )
                }

                // Headline
                GroomoraHeadline(
                    text = "No Internet Connection",
                    textAlign = TextAlign.Center,
                    color = AppText,
                    fontSize = 22.sp
                )

                // Subtitle Message
                GroomoraBody(
                    text = "It looks like you're offline. Check your connection and try again.",
                    color = MutedText,
                    textAlign = TextAlign.Center
                )

                // Quick Checklist Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFBF9F5),
                    border = BorderStroke(1.dp, BorderGray),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Please check:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Charcoal
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("•", color = HoneyAmber, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                            Text("Wi-Fi or Mobile Cellular data connection", style = MaterialTheme.typography.bodySmall, color = MutedText)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("•", color = HoneyAmber, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                            Text("Ensure Airplane Mode is turned off", style = MaterialTheme.typography.bodySmall, color = MutedText)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("•", color = HoneyAmber, fontWeight = FontWeight.Bold, modifier = Modifier.padding(end = 8.dp))
                            Text("Try reconnecting to your Wi-Fi network", style = MaterialTheme.typography.bodySmall, color = MutedText)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Retry Button
                GroomoraPrimaryButton(
                    text = if (isRetrying) "Checking Connection..." else "Retry Connection",
                    onClick = {
                        if (!isRetrying) {
                            isRetrying = true
                            scope.launch {
                                onRetry()
                                delay(1200)
                                isRetrying = false
                            }
                        }
                    },
                    isLoading = isRetrying,
                    icon = Icons.Default.Refresh,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
