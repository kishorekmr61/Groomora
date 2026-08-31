package com.groomora.core.configuration

import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    val config: Flow<AppConfig>
    suspend fun fetchConfig()
    fun getFeatureFlag(key: (FeatureFlags) -> Boolean): Flow<Boolean>
    fun checkUpdateStatus(currentVersion: String, isIos: Boolean): UpdateStatus
    fun setMaintenanceMode(enabled: Boolean, title: String? = null, message: String? = null, estimatedEnd: String? = null)
    fun setVersionGate(minVersion: String, latestVersion: String)
}

expect fun createConfigRepository(): ConfigRepository
