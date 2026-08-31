package com.groomora.core.util

actual object GroomoraLog {
    actual fun d(tag: String, message: String) {
        // iOS doesn't have a direct BuildConfig.DEBUG equivalent in KMP without extra setup
        // But we can just use println for now which only shows in Xcode debug console
        println("[$tag] $message")
    }

    actual fun e(tag: String, message: String, throwable: Throwable?) {
        println("[$tag] ERROR: $message")
        throwable?.printStackTrace()
    }
}
