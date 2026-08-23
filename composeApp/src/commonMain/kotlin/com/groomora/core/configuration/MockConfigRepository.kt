package com.groomora.core.configuration

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class MockConfigRepository : ConfigRepository {
    private val _config = MutableStateFlow(
        AppConfig(
            maintenance = MaintenanceConfig(
                isMaintenanceMode = false,
                title = "We'll Be Right Back!",
                message = "Groomora is currently undergoing scheduled maintenance. Please check back shortly.",
                estimatedEndTime = "Expected back online by 2:00 PM"
            ),
            appUpdate = AppUpdateConfig(
                minSupportedVersion = "0.1.0",
                latestVersion = "0.1.0",
                forceUpdateTitle = "Update Required",
                forceUpdateMessage = "A new version of Groomora is required to continue enjoying seamless booking and salon services.",
                flexibleUpdateTitle = "New Version Available",
                flexibleUpdateMessage = "A fresh update with new features and improvements is now available.",
                playStoreUrl = "https://play.google.com/store/apps/details?id=com.groomora.app",
                appStoreUrl = "https://apps.apple.com/app/groomora/id123456789"
            ),
            featureFlags = FeatureFlags(
                enableBridal = true,
                enableHomeService = true,
                enableLoyalty = true,
                enableProducts = true,
                enableReviews = true
            ),
            categories = listOf(
                CategoryConfig("hair", "Hair", null),
                CategoryConfig("beard", "Beard", null),
                CategoryConfig("makeup", "Makeup", null),
                CategoryConfig("bridal", "Bridal", null),
                CategoryConfig("home", "Home Service", null)
            )
        )
    )

    override val config: Flow<AppConfig> = _config.asStateFlow()

    override suspend fun fetchConfig() {
        // Simulates remote config fetch over network
        kotlinx.coroutines.delay(300)
    }

    override fun getFeatureFlag(key: (FeatureFlags) -> Boolean): Flow<Boolean> {
        return _config.map { key(it.featureFlags) }
    }

    override fun checkUpdateStatus(currentVersion: String, isIos: Boolean): UpdateStatus {
        val currentConfig = _config.value.appUpdate
        val storeUrl = if (isIos) currentConfig.appStoreUrl else currentConfig.playStoreUrl

        // Check force update: current < minSupported
        if (compareVersions(currentVersion, currentConfig.minSupportedVersion) < 0) {
            return UpdateStatus.ForceUpdateRequired(
                minVersion = currentConfig.minSupportedVersion,
                title = currentConfig.forceUpdateTitle,
                message = currentConfig.forceUpdateMessage,
                storeUrl = storeUrl
            )
        }

        // Check flexible update: current < latest
        if (compareVersions(currentVersion, currentConfig.latestVersion) < 0) {
            return UpdateStatus.FlexibleUpdateAvailable(
                latestVersion = currentConfig.latestVersion,
                title = currentConfig.flexibleUpdateTitle,
                message = currentConfig.flexibleUpdateMessage,
                storeUrl = storeUrl
            )
        }

        return UpdateStatus.NoUpdateRequired
    }

    override fun setMaintenanceMode(
        enabled: Boolean,
        title: String?,
        message: String?,
        estimatedEnd: String?
    ) {
        val current = _config.value
        _config.value = current.copy(
            maintenance = current.maintenance.copy(
                isMaintenanceMode = enabled,
                title = title ?: current.maintenance.title,
                message = message ?: current.maintenance.message,
                estimatedEndTime = estimatedEnd ?: current.maintenance.estimatedEndTime
            )
        )
    }

    override fun setVersionGate(minVersion: String, latestVersion: String) {
        val current = _config.value
        _config.value = current.copy(
            appUpdate = current.appUpdate.copy(
                minSupportedVersion = minVersion,
                latestVersion = latestVersion
            )
        )
    }
}
