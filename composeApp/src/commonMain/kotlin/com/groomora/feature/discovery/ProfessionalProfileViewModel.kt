package com.groomora.feature.discovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfessionalProfileViewModel(
    private val discoveryRepository: DiscoveryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfessionalProfileState())
    val state: StateFlow<ProfessionalProfileState> = _state.asStateFlow()

    fun onIntent(intent: ProfessionalProfileIntent) {
        when (intent) {
            is ProfessionalProfileIntent.LoadProfile -> loadProfile(intent.id)
        }
    }

    private fun loadProfile(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val professional = discoveryRepository.getProfessionalDetail(id)
            _state.update { it.copy(isLoading = false, professional = professional) }
        }
    }
}
