package com.groomora.core.notifications

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.groomora.app.DependencyContainer
import com.groomora.core.util.GroomoraLog

class GroomoraFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        GroomoraLog.d("FCM", "New Token: $token")
        // In a real app, send this token to your backend
        DependencyContainer.analyticsManager.logEvent("fcm_token_updated", mapOf("token" to token))
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        GroomoraLog.d("FCM", "Message Received: ${message.notification?.body}")
        
        // Log the notification event
        message.notification?.let {
            DependencyContainer.analyticsManager.logEvent("notification_received", mapOf(
                "title" to (it.title ?: ""),
                "body" to (it.body ?: "")
            ))
        }
    }
}
