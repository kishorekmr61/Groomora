package com.groomora.feature.bridal

import kotlinx.serialization.Serializable

@Serializable
data class BridalPackage(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val items: List<String>,
    val imageUrl: String? = null,
    val isHomeServiceAvailable: Boolean = true
)

data class BridalState(
    val isLoading: Boolean = false,
    val packages: List<BridalPackage> = emptyList(),
    val error: String? = null
)

sealed interface BridalIntent {
    data object LoadPackages : BridalIntent
}
