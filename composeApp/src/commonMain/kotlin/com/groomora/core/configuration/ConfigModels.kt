package com.groomora.core.configuration

import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(
    val maintenance: MaintenanceConfig = MaintenanceConfig(),
    val appUpdate: AppUpdateConfig = AppUpdateConfig(),
    val featureFlags: FeatureFlags = FeatureFlags(),
    val categories: List<CategoryConfig> = emptyList()
)

@Serializable
data class MaintenanceConfig(
    val isMaintenanceMode: Boolean = false,
    val title: String = "We'll Be Right Back!",
    val message: String = "Groomora is currently undergoing scheduled maintenance to improve your experience. Please check back shortly.",
    val estimatedEndTime: String? = null
)

@Serializable
data class AppUpdateConfig(
    val minSupportedVersion: String = "0.1.0",
    val latestVersion: String = "0.1.0",
    val forceUpdateTitle: String = "Update Required",
    val forceUpdateMessage: String = "A new version of Groomora is required to continue enjoying seamless booking and salon services.",
    val flexibleUpdateTitle: String = "New Version Available",
    val flexibleUpdateMessage: String = "A fresh update with new features and improvements is now available in the store.",
    val playStoreUrl: String = "https://play.google.com/store/apps/details?id=com.groomora.app",
    val appStoreUrl: String = "https://apps.apple.com/app/groomora/id123456789"
)

@Serializable
data class FeatureFlags(
    val enableBridal: Boolean = true,
    val enableHomeService: Boolean = true,
    val enableLoyalty: Boolean = true,
    val enableProducts: Boolean = true,
    val enableReviews: Boolean = true
)

@Serializable
data class CategoryConfig(
    val id: String,
    val label: String,
    val iconUrl: String? = null
)

sealed interface UpdateStatus {
    data object NoUpdateRequired : UpdateStatus
    data class FlexibleUpdateAvailable(
        val latestVersion: String,
        val title: String,
        val message: String,
        val storeUrl: String
    ) : UpdateStatus
    data class ForceUpdateRequired(
        val minVersion: String,
        val title: String,
        val message: String,
        val storeUrl: String
    ) : UpdateStatus
}

/**
 * Helper to compare semantic versions (e.g. "0.1.0" vs "1.0.0")
 */
fun compareVersions(v1: String, v2: String): Int {
    val parts1 = v1.split(".").map { it.toIntOrNull() ?: 0 }
    val parts2 = v2.split(".").map { it.toIntOrNull() ?: 0 }
    val maxLen = maxOf(parts1.size, parts2.size)

    for (i in 0 until maxLen) {
        val p1 = parts1.getOrElse(i) { 0 }
        val p2 = parts2.getOrElse(i) { 0 }
        if (p1 != p2) {
            return p1.compareTo(p2)
        }
    }
    return 0
}
