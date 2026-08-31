package com.groomora.core.configuration

import com.groomora.core.util.GroomoraLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class IosConfigRepository : ConfigRepository {
    private val _config = MutableStateFlow(AppConfig())
    override val config: Flow<AppConfig> = _config.asStateFlow()

    override suspend fun fetchConfig() {
        GroomoraLog.d("Config-iOS", "iOS Remote Config not yet bridged to live SDK - using local defaults")
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

    override fun setMaintenanceMode(enabled: Boolean, title: String?, message: String?, estimatedEnd: String?) {
    }

    override fun setVersionGate(minVersion: String, latestVersion: String) {
    }
}

actual fun createConfigRepository(): ConfigRepository = IosConfigRepository()
