package com.groomora.core.notifications

import com.groomora.core.util.GroomoraLog

interface PushNotificationManager {
    fun requestPermission()
    fun isPermissionGranted(): Boolean
    fun getToken(): String?
    fun initialize()
}

expect fun createPushNotificationManager(): PushNotificationManager

class MockPushNotificationManager : PushNotificationManager {
    override fun requestPermission() {
        GroomoraLog.d("PushNotifications", "Requesting permission (Mock)")
    }

    override fun isPermissionGranted(): Boolean = true

    override fun getToken(): String? = "mock-fcm-token-12345"

    override fun initialize() {
        GroomoraLog.d("PushNotifications", "Initialized (Mock)")
    }
}
