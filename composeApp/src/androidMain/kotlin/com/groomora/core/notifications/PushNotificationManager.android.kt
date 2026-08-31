package com.groomora.core.notifications

import android.app.Activity
import com.groomora.app.DependencyContainer

actual fun createPushNotificationManager(): PushNotificationManager = MockPushNotificationManager()
