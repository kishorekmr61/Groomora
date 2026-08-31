package com.groomora.core.configuration

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.groomora.app.DependencyContainer
import com.groomora.app.R
import com.groomora.core.util.GroomoraLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

class FirebaseConfigRepository : ConfigRepository {
    private val remoteConfig = FirebaseRemoteConfig.getInstance()
    private val _config = MutableStateFlow(AppConfig())
    override val config: Flow<AppConfig> = _config.asStateFlow()

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    init {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(0) // Set to 0 for immediate updates during testing
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        updateLocalConfig()
    }

    override suspend fun fetchConfig() {
        GroomoraLog.d("FirebaseConfig", "STARTING FETCH - Time: ${System.currentTimeMillis()}")
        remoteConfig.fetch(0).addOnCompleteListener { fetchTask ->
            if (fetchTask.isSuccessful) {
                GroomoraLog.d("FirebaseConfig", "FETCH SUCCESSFUL - Activating...")
                remoteConfig.activate().addOnCompleteListener { activateTask ->
                    GroomoraLog.d("FirebaseConfig", "ACTIVATION COMPLETE - New Data: ${activateTask.result}")
                    updateLocalConfig()
                }
            } else {
                GroomoraLog.e("FirebaseConfig", "FETCH FAILED: ${fetchTask.exception?.message}")
                // Try to activate whatever we have
                remoteConfig.activate().addOnCompleteListener { updateLocalConfig() }
            }
        }
    }

    private fun updateLocalConfig() {
        val configJson = remoteConfig.getString("app_config_json")
        val info = remoteConfig.getInfo()
        val source = remoteConfig.getValue("app_config_json").source
        
        GroomoraLog.d("FirebaseConfig", "Updating Local Config. Source: $source")
        GroomoraLog.d("FirebaseConfig", "Fetch Status: ${info.lastFetchStatus}")
        GroomoraLog.d("FirebaseConfig", "Raw JSON: $configJson")
        
        if (configJson.isNotEmpty()) {
            try {
                val parsed = json.decodeFromString<AppConfig>(configJson)
                _config.value = parsed
                GroomoraLog.d("FirebaseConfig", "Config Parsed Successfully.")
                GroomoraLog.d("FirebaseConfig", "Maintenance Mode: ${parsed.maintenance.isMaintenanceMode}")
                GroomoraLog.d("FirebaseConfig", "Maintenance Title: ${parsed.maintenance.title}")
            } catch (e: Exception) {
                GroomoraLog.e("FirebaseConfig", "Config Parsing Failed: ${e.message}", e)
                DependencyContainer.crashReporter.recordException(e)
            }
        } else {
            GroomoraLog.d("FirebaseConfig", "app_config_json is EMPTY. Check if key matches in console.")
        }
    }

    override fun getFeatureFlag(key: (FeatureFlags) -> Boolean): Flow<Boolean> {
        // Simplified mapping
        return MutableStateFlow(true) 
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
        // Local override not typically used with Remote Config
    }

    override fun setVersionGate(minVersion: String, latestVersion: String) {
        // Local override not typically used with Remote Config
    }
}
