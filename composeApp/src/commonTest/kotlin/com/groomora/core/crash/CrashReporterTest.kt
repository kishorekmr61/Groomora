package com.groomora.core.crash

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CrashReporterTest {

    @Test
    fun testBreadcrumbLogging() {
        val reporter = FirebaseCrashReporter()
        reporter.logBreadcrumb("Navigated to LoginScreen")
        reporter.logBreadcrumb("Entered OTP")
        reporter.logBreadcrumb("Authentication successful")

        val crumbs = reporter.getBreadcrumbs()
        assertEquals(3, crumbs.size)
        assertTrue(crumbs.any { it.contains("Navigated to LoginScreen") })
        assertTrue(crumbs.any { it.contains("Authentication successful") })
    }

    @Test
    fun testUserAndCustomKeys() {
        val reporter = FirebaseCrashReporter()
        reporter.setUserId("user_999")
        reporter.setCustomKey("screen", "signup_screen")
        reporter.setCustomKey("app_version", "1.0.0")

        // Non-fatal record should not crash
        reporter.recordNonFatal(
            IllegalArgumentException("Invalid referral format"),
            mapOf("code" to "INVALID")
        )

        // Fatal record
        reporter.recordException(
            RuntimeException("Test crash simulation"),
            mapOf("fatal_flag" to "true")
        )
    }
}
