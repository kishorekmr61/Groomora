package com.groomora.core.configuration

import kotlinx.serialization.Serializable

@Serializable
data class AppConfig(
    val featureFlags: FeatureFlags = FeatureFlags(),
    val versionGate: VersionGate = VersionGate(),
    val categories: List<CategoryConfig> = emptyList()
)

@Serializable
data class FeatureFlags(
    val enableBridal: Boolean = true,
    val enableHomeService: Boolean = true,
    val enableLoyalty: Boolean = false,
    val enableProducts: Boolean = false
)

@Serializable
data class VersionGate(
    val minVersion: String = "1.0.0",
    val forceUpdate: Boolean = false
)

@Serializable
data class CategoryConfig(
    val id: String,
    val label: String,
    val iconUrl: String? = null
)
