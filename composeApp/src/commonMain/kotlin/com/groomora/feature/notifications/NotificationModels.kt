package com.groomora.feature.notifications

import kotlinx.serialization.Serializable

@Serializable
data class Notification(
    val id: String,
    val title: String,
    val body: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val type: NotificationType,
    val deepLink: String? = null
)

enum class NotificationType {
    BOOKING, PAYMENT, OFFER, LOYALTY, ANNOUNCEMENT, SYSTEM
}

data class NotificationsState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
    val unreadCount: Int = 0,
    val error: String? = null
)

sealed interface NotificationsIntent {
    data object LoadNotifications : NotificationsIntent
    data class MarkAsRead(val id: String) : NotificationsIntent
    data object MarkAllAsRead : NotificationsIntent
}
