package com.groomora.core.configuration

import kotlinx.coroutines.flow.Flow

interface ConfigRepository {
    val config: Flow<AppConfig>
    suspend fun fetchConfig()
    fun getFeatureFlag(key: (FeatureFlags) -> Boolean): Flow<Boolean>
}
