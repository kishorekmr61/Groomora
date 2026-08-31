package com.groomora.core.crash

import com.google.firebase.crashlytics.FirebaseCrashlytics

class AndroidCrashReporter : CrashReporter {
    private val firebase = FirebaseCrashlytics.getInstance()

    override fun recordException(throwable: Throwable, context: Map<String, String>) {
        context.forEach { (key, value) -> firebase.setCustomKey(key, value) }
        firebase.recordException(throwable)
    }

    override fun recordNonFatal(throwable: Throwable, context: Map<String, String>) {
        context.forEach { (key, value) -> firebase.setCustomKey(key, value) }
        firebase.recordException(throwable)
    }

    override fun logBreadcrumb(message: String) {
        firebase.log(message)
    }

    override fun setUserId(userId: String) {
        firebase.setUserId(userId)
    }

    override fun setCustomKey(key: String, value: String) {
        firebase.setCustomKey(key, value)
    }

    override fun getBreadcrumbs(): List<String> {
        // Firebase doesn't expose breadcrumbs back, but they are sent in reports
        return emptyList()
    }
}
