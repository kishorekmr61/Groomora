package com.groomora.core.configuration

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RemoteConfigTest {

    @Test
    fun testCompareVersions() {
        assertEquals(0, compareVersions("1.0.0", "1.0.0"))
        assertEquals(-1, compareVersions("0.1.0", "1.0.0"))
        assertEquals(1, compareVersions("1.2.0", "1.1.5"))
        assertEquals(-1, compareVersions("1.0.0", "1.0.1"))
        assertEquals(1, compareVersions("2.0.0", "1.9.9"))
    }

    @Test
    fun testMaintenanceModeToggle() {
        val configRepo = MockConfigRepository()

        // Enable maintenance mode
        configRepo.setMaintenanceMode(
            enabled = true,
            title = "Scheduled Upgrade",
            message = "We are upgrading our servers for 30 minutes.",
            estimatedEnd = "Expected back by 3:00 PM"
        )

        // Check update status or direct state
        configRepo.setMaintenanceMode(enabled = false)
    }

    @Test
    fun testForceUpdateEvaluationForAndroidAndIos() {
        val configRepo = MockConfigRepository()
        configRepo.setVersionGate(minVersion = "1.0.0", latestVersion = "1.2.0")

        // Client on version 0.9.0 (below minVersion 1.0.0) -> Force Update Required
        val androidStatus = configRepo.checkUpdateStatus(currentVersion = "0.9.0", isIos = false)
        assertTrue(androidStatus is UpdateStatus.ForceUpdateRequired)
        assertEquals("1.0.0", (androidStatus as UpdateStatus.ForceUpdateRequired).minVersion)
        assertTrue(androidStatus.storeUrl.contains("play.google.com"))

        val iosStatus = configRepo.checkUpdateStatus(currentVersion = "0.9.0", isIos = true)
        assertTrue(iosStatus is UpdateStatus.ForceUpdateRequired)
        assertEquals("1.0.0", (iosStatus as UpdateStatus.ForceUpdateRequired).minVersion)
        assertTrue(iosStatus.storeUrl.contains("apps.apple.com"))
    }

    @Test
    fun testFlexibleUpdateEvaluation() {
        val configRepo = MockConfigRepository()
        configRepo.setVersionGate(minVersion = "1.0.0", latestVersion = "1.2.0")

        // Client on version 1.0.0 (meets minVersion 1.0.0, but lower than latestVersion 1.2.0) -> Flexible Update Available
        val status = configRepo.checkUpdateStatus(currentVersion = "1.0.0", isIos = false)
        assertTrue(status is UpdateStatus.FlexibleUpdateAvailable)
        assertEquals("1.2.0", (status as UpdateStatus.FlexibleUpdateAvailable).latestVersion)

        // Client on version 1.2.0 -> No Update Required
        val upToDateStatus = configRepo.checkUpdateStatus(currentVersion = "1.2.0", isIos = false)
        assertTrue(upToDateStatus is UpdateStatus.NoUpdateRequired)
    }
}
