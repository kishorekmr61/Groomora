package com.groomora.core.notifications

actual fun createPushNotificationManager(): PushNotificationManager = MockPushNotificationManager()
