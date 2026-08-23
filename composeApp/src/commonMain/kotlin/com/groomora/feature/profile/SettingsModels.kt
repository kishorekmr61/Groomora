package com.groomora.feature.profile

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val selectedGender: String? = null,
    val preferredCategories: List<String> = emptyList(),
    val language: String = "English",
    val theme: AppTheme = AppTheme.SYSTEM,
    val currency: String = "INR",
    val distanceUnit: String = "km",
    val marketingConsent: Boolean = true,
    val analyticsConsent: Boolean = true
)

enum class AppTheme {
    SYSTEM, LIGHT, DARK
}

data class SettingsState(
    val isLoading: Boolean = false,
    val preferences: UserPreferences = UserPreferences(),
    val error: String? = null
)

sealed interface SettingsIntent {
    data object LoadPreferences : SettingsIntent
    data class UpdateGender(val gender: String?) : SettingsIntent
    data class UpdateLanguage(val language: String) : SettingsIntent
    data class UpdateTheme(val theme: AppTheme) : SettingsIntent
    data class ToggleMarketing(val enabled: Boolean) : SettingsIntent
}
