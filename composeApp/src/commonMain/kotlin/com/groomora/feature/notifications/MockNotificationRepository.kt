package com.groomora.feature.notifications

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class MockNotificationRepository : NotificationRepository {
    private val _notifications = MutableStateFlow(
        listOf(
            Notification(
                id = "n1",
                title = "Booking Confirmed",
                body = "Your appointment at The Golden Scissor is confirmed for Oct 24, 10:00 AM.",
                timestamp = "2 hours ago",
                isRead = false,
                type = NotificationType.BOOKING
            ),
            Notification(
                id = "n2",
                title = "Exclusive Offer",
                body = "Get 30% off on all spa services this weekend!",
                timestamp = "5 hours ago",
                isRead = true,
                type = NotificationType.OFFER
            ),
            Notification(
                id = "n3",
                title = "Points Earned",
                body = "You just earned 100 loyalty points from your last visit.",
                timestamp = "1 day ago",
                isRead = true,
                type = NotificationType.LOYALTY
            )
        )
    )

    override fun getNotifications(): Flow<List<Notification>> = _notifications.asStateFlow()

    override fun getUnreadCount(): Flow<Int> = _notifications.map { list ->
        list.count { !it.isRead }
    }

    override suspend fun markAsRead(id: String) {
        delay(200)
        val current = _notifications.value.toMutableList()
        val index = current.indexOfFirst { it.id == id }
        if (index != -1) {
            current[index] = current[index].copy(isRead = true)
            _notifications.value = current
        }
    }

    override suspend fun markAllAsRead() {
        delay(300)
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }
}
