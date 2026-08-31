package com.groomora.core.util

/**
 * Multiplatform Logging Utility.
 * automatically suppresses debug logs in release builds for Android.
 */
expect object GroomoraLog {
    fun d(tag: String, message: String)
    fun e(tag: String, message: String, throwable: Throwable? = null)
}
