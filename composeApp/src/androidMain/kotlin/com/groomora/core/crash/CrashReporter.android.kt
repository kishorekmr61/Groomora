package com.groomora.core.crash

actual fun createCrashReporter(): CrashReporter = AndroidCrashReporter()
