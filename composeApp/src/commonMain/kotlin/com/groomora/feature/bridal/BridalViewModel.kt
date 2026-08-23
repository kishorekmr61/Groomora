package com.groomora.feature.bridal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BridalViewModel(
    private val bridalRepository: BridalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BridalState())
    val state: StateFlow<BridalState> = _state.asStateFlow()

    init {
        onIntent(BridalIntent.LoadPackages)
    }

    fun onIntent(intent: BridalIntent) {
        when (intent) {
            BridalIntent.LoadPackages -> loadPackages()
        }
    }

    private fun loadPackages() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            bridalRepository.getBridalPackages().collect { packages ->
                _state.update { it.copy(isLoading = false, packages = packages) }
            }
        }
    }
}
