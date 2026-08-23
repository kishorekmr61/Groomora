package com.groomora.core.configuration

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class MockConfigRepository : ConfigRepository {
    private val _config = MutableStateFlow(
        AppConfig(
            featureFlags = FeatureFlags(
                enableBridal = true,
                enableHomeService = true,
                enableLoyalty = true,
                enableProducts = true
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
        // Mock network delay
        kotlinx.coroutines.delay(500)
    }

    override fun getFeatureFlag(key: (FeatureFlags) -> Boolean): Flow<Boolean> {
        return _config.map { key(it.featureFlags) }
    }
}
