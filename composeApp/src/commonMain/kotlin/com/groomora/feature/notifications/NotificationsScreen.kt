package com.groomora.feature.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.groomora.app.DependencyContainer
import com.groomora.design.*
import com.groomora.design.components.*

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        DependencyContainer.analyticsManager.logScreenView("notifications_screen")
    }

    Scaffold(
        topBar = {
            GroomoraTopAppBar(
                title = "Notifications",
                onBack = onBack,
                actions = {
                    if (state.unreadCount > 0) {
                        TextButton(onClick = { viewModel.onIntent(NotificationsIntent.MarkAllAsRead) }) {
                            Text("Mark all read", color = HoneyAmber, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            )
        }
    ) { padding ->
        if (state.isLoading) {
            GroomoraLoadingState()
        } else if (state.notifications.isEmpty()) {
            GroomoraEmptyState(
                title = "No Notifications",
                message = "You're all caught up! Updates regarding appointments and offers will appear here.",
                icon = Icons.Default.Notifications,
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(WarmIvory)
                    .padding(padding)
            ) {
                items(state.notifications) { notification ->
                    NotificationItem(
                        notification = notification,
                        onClick = { viewModel.onIntent(NotificationsIntent.MarkAsRead(notification.id)) }
                    )
                    HorizontalDivider(color = BorderGray)
                }
            }
        }
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = if (notification.isRead) Color.Transparent else HoneyAmber.copy(alpha = 0.06f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .padding(top = 6.dp)
                    .background(
                        color = if (notification.isRead) Color.Transparent else HoneyAmber,
                        shape = MaterialTheme.shapes.extraSmall
                    )
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = notification.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                    color = AppText
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = notification.body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppText
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = notification.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedText
                )
            }
        }
    }
}
