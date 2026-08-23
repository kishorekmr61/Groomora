package com.groomora.feature.update

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.app.DependencyContainer
import com.groomora.core.configuration.UpdateStatus
import com.groomora.design.*
import com.groomora.design.components.*

@Composable
fun ForceUpdateScreen(
    updateInfo: UpdateStatus.ForceUpdateRequired,
    modifier: Modifier = Modifier
) {
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        DependencyContainer.analyticsManager.logScreenView("force_update_screen")
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
                // Update Icon Badge
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(HoneyAmber.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Update Required",
                        tint = HoneyAmber,
                        modifier = Modifier.size(40.dp)
                    )
                }

                GroomoraHeadline(
                    text = updateInfo.title,
                    textAlign = TextAlign.Center,
                    fontSize = 22.sp
                )

                GroomoraBody(
                    text = updateInfo.message,
                    color = MutedText,
                    textAlign = TextAlign.Center
                )

                Surface(
                    shape = CircleShape,
                    color = Color(0xFFF2FAF7),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SuccessGreen.copy(alpha = 0.3f)),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "Version ${updateInfo.minVersion} or higher required",
                        style = MaterialTheme.typography.labelSmall,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                Spacer(Modifier.height(8.dp))

                GroomoraPrimaryButton(
                    text = "Update Now",
                    onClick = {
                        DependencyContainer.analyticsManager.logEvent(
                            "force_update_clicked",
                            mapOf("store_url" to updateInfo.storeUrl)
                        )
                        try {
                            uriHandler.openUri(updateInfo.storeUrl)
                        } catch (e: Exception) {
                            println("Failed to open update URL: ${e.message}")
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlexibleUpdateBottomSheet(
    updateInfo: UpdateStatus.FlexibleUpdateAvailable,
    onDismiss: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = { BottomSheetDefaults.DragHandle(color = BorderGray) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(HoneyAmber.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = HoneyAmber,
                    modifier = Modifier.size(30.dp)
                )
            }

            GroomoraTitle(
                text = updateInfo.title,
                textAlign = TextAlign.Center
            )

            GroomoraBody(
                text = updateInfo.message,
                color = MutedText,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(4.dp))

            GroomoraPrimaryButton(
                text = "Update to v${updateInfo.latestVersion}",
                onClick = {
                    DependencyContainer.analyticsManager.logEvent(
                        "flexible_update_clicked",
                        mapOf("version" to updateInfo.latestVersion)
                    )
                    try {
                        uriHandler.openUri(updateInfo.storeUrl)
                    } catch (e: Exception) {
                        println("Failed to open update URL: ${e.message}")
                    }
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            )

            TextButton(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                GroomoraCaption(text = "Maybe Later", color = MutedText, fontWeight = FontWeight.Bold)
            }
        }
    }
}
