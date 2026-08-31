package com.groomora.core.crash

import com.groomora.core.util.GroomoraLog

/**
 * iOS implementation of CrashReporter.
 * To enable real Firebase Crashlytics on iOS:
 * 1. Add FirebaseCrashlytics pod to your Xcode project.
 * 2. Bridge the calls below to FIRCrashlytics.
 */
class IosCrashReporter : CrashReporter {

    override fun recordException(throwable: Throwable, context: Map<String, String>) {
        GroomoraLog.e("Crash-iOS", "Exception recorded: ${throwable.message}")
    }

    override fun recordNonFatal(throwable: Throwable, context: Map<String, String>) {
        GroomoraLog.e("Crash-iOS", "Non-fatal error: ${throwable.message}")
    }

    override fun logBreadcrumb(message: String) {
        GroomoraLog.d("Crash-iOS", "Breadcrumb: $message")
    }

    override fun setUserId(userId: String) {
        GroomoraLog.d("Crash-iOS", "User ID: $userId")
    }

    override fun setCustomKey(key: String, value: String) {
        GroomoraLog.d("Crash-iOS", "Key: $key=$value")
    }

    override fun getBreadcrumbs(): List<String> = emptyList()
}

actual fun createCrashReporter(): CrashReporter = IosCrashReporter()
