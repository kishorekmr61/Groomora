package com.groomora.core.util

import android.util.Log
import com.groomora.app.BuildConfig

/**
 * Android implementation of GroomoraLog that honors BuildConfig.DEBUG
 */
actual object GroomoraLog {
    actual fun d(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, message)
        }
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, message, throwable)
        }
    }
}
