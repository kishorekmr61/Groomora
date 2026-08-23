package com.groomora.feature.support

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SupportViewModel(
    private val supportRepository: SupportRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SupportState())
    val state: StateFlow<SupportState> = _state.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            combine(
                supportRepository.getFaqs(),
                supportRepository.getTickets()
            ) { faqs, tickets ->
                SupportState(
                    isLoading = false,
                    faqs = faqs,
                    tickets = tickets
                )
            }.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun onIntent(intent: SupportIntent) {
        when (intent) {
            SupportIntent.LoadSupportData -> loadData()
            is SupportIntent.SubmitQuery -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    val success = supportRepository.submitTicket(intent.subject, intent.message)
                    if (success) loadData()
                    else _state.update { it.copy(isLoading = false, error = "Failed to submit query") }
                }
            }
        }
    }
}
