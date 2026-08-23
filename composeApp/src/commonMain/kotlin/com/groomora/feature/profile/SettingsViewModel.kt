package com.groomora.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    fun onIntent(intent: SettingsIntent) {
        when (intent) {
            SettingsIntent.LoadPreferences -> loadPreferences()
            is SettingsIntent.UpdateGender -> {
                _state.update { it.copy(preferences = it.preferences.copy(selectedGender = intent.gender)) }
            }
            is SettingsIntent.UpdateLanguage -> {
                _state.update { it.copy(preferences = it.preferences.copy(language = intent.language)) }
            }
            is SettingsIntent.UpdateTheme -> {
                _state.update { it.copy(preferences = it.preferences.copy(theme = intent.theme)) }
            }
            is SettingsIntent.ToggleMarketing -> {
                _state.update { it.copy(preferences = it.preferences.copy(marketingConsent = intent.enabled)) }
            }
        }
    }

    private fun loadPreferences() {
        // Mock loading from a DataStore or Repository
        _state.update { it.copy(isLoading = false) }
    }
}
