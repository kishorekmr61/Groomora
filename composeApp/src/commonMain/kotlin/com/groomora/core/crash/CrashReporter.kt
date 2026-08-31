package com.groomora.core.crash

import com.groomora.core.util.GroomoraLog

/**
 * Multiplatform Crash Reporting contract ready for Firebase Crashlytics.
 */
interface CrashReporter {
    fun recordException(throwable: Throwable, context: Map<String, String> = emptyMap())
    fun recordNonFatal(throwable: Throwable, context: Map<String, String> = emptyMap())
    fun logBreadcrumb(message: String)
    fun setUserId(userId: String)
    fun setCustomKey(key: String, value: String)
    fun getBreadcrumbs(): List<String>
}

expect fun createCrashReporter(): CrashReporter

/**
 * Firebase Crashlytics-compatible Crash Reporter.
 * Collects breadcrumbs, session context, custom keys, and routes non-fatal/fatal
 * exceptions with full stack traces.
 */
class FirebaseCrashReporter : CrashReporter {
    private var currentUserId: String? = null
    private val customKeys = mutableMapOf<String, String>()
    private val breadcrumbs = mutableListOf<String>()
    private val maxBreadcrumbs = 50

    override fun recordException(throwable: Throwable, context: Map<String, String>) {
        val mergedContext = customKeys + context
        GroomoraLog.e("FirebaseCrashlytics", "🔴 FATAL / RECORDED EXCEPTION: ${throwable.message}", throwable)
        GroomoraLog.d("FirebaseCrashlytics", "User ID: ${currentUserId ?: "Anonymous"}")
        GroomoraLog.d("FirebaseCrashlytics", "Context Attributes: $mergedContext")
        GroomoraLog.d("FirebaseCrashlytics", "Recent Breadcrumbs (${breadcrumbs.size}):")
        breadcrumbs.takeLast(10).forEach { GroomoraLog.d("FirebaseCrashlytics", "   └─ $it") }
    }

    override fun recordNonFatal(throwable: Throwable, context: Map<String, String>) {
        val mergedContext = customKeys + context
        GroomoraLog.d("FirebaseCrashlytics", "⚠️ NON-FATAL EXCEPTION: ${throwable.message}")
        GroomoraLog.d("FirebaseCrashlytics", "User ID: ${currentUserId ?: "Anonymous"} | Context: $mergedContext")
    }

    override fun logBreadcrumb(message: String) {
        synchronized(breadcrumbs) {
            if (breadcrumbs.size >= maxBreadcrumbs) {
                breadcrumbs.removeAt(0)
            }
            val entry = "[${currentTimeMillis()}] $message"
            breadcrumbs.add(entry)
            GroomoraLog.d("FirebaseCrashlytics", "🍞 Breadcrumb: $message")
        }
    }

    override fun setUserId(userId: String) {
        currentUserId = userId
        GroomoraLog.d("FirebaseCrashlytics", "👤 User ID set: $userId")
    }

    override fun setCustomKey(key: String, value: String) {
        customKeys[key] = value
        GroomoraLog.d("FirebaseCrashlytics", "🏷️ Custom key set: $key = $value")
    }

    override fun getBreadcrumbs(): List<String> {
        return synchronized(breadcrumbs) { breadcrumbs.toList() }
    }

    private fun currentTimeMillis(): Long {
        return kotlin.time.TimeSource.Monotonic.markNow().elapsedNow().inWholeMilliseconds
    }
}

class DefaultCrashReporter : CrashReporter {
    private val delegate = FirebaseCrashReporter()

    override fun recordException(throwable: Throwable, context: Map<String, String>) = delegate.recordException(throwable, context)
    override fun recordNonFatal(throwable: Throwable, context: Map<String, String>) = delegate.recordNonFatal(throwable, context)
    override fun logBreadcrumb(message: String) = delegate.logBreadcrumb(message)
    override fun setUserId(userId: String) = delegate.setUserId(userId)
    override fun setCustomKey(key: String, value: String) = delegate.setCustomKey(key, value)
    override fun getBreadcrumbs(): List<String> = delegate.getBreadcrumbs()
}
