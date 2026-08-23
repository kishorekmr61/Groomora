package com.groomora.feature.maintenance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Refresh
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
import com.groomora.core.configuration.MaintenanceConfig
import com.groomora.design.*
import com.groomora.design.components.*

@Composable
fun MaintenanceScreen(
    maintenanceConfig: MaintenanceConfig,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isChecking by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        DependencyContainer.analyticsManager.logScreenView("maintenance_screen")
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
                // Maintenance Icon Badge
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(HoneyAmber.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Under Maintenance",
                        tint = HoneyAmber,
                        modifier = Modifier.size(40.dp)
                    )
                }

                // Brand Headline
                GroomoraHeadline(
                    text = maintenanceConfig.title,
                    textAlign = TextAlign.Center,
                    color = AppText,
                    fontSize = 22.sp
                )

                // Message description
                GroomoraBody(
                    text = maintenanceConfig.message,
                    color = MutedText,
                    textAlign = TextAlign.Center
                )

                // Estimated Recovery Tag
                if (!maintenanceConfig.estimatedEndTime.isNullOrBlank()) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFFFF8E7),
                        border = androidx.compose.foundation.BorderStroke(1.dp, HoneyAmber.copy(alpha = 0.4f)),
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = maintenanceConfig.estimatedEndTime,
                            style = MaterialTheme.typography.labelMedium,
                            color = HoneyAmber,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                // Refresh Status Button
                GroomoraPrimaryButton(
                    text = "Check Status",
                    icon = Icons.Default.Refresh,
                    isLoading = isChecking,
                    onClick = {
                        isChecking = true
                        DependencyContainer.analyticsManager.logEvent("maintenance_check_status_clicked")
                        onRefresh()
                        isChecking = false
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
