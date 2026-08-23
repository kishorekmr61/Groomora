package com.groomora.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.groomora.feature.auth.AuthRepository
import com.groomora.feature.auth.AuthState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()

    init {
        observeAuthState()
    }

    private fun observeAuthState() {
        viewModelScope.launch {
            authRepository.authState.collect { authState ->
                when (authState) {
                    is AuthState.Authenticated -> {
                        _state.update { it.copy(user = authState.user, isLoading = false) }
                    }
                    is AuthState.Unauthenticated -> {
                        _state.update { it.copy(user = null, isLoading = false) }
                    }
                    is AuthState.Loading -> {
                        _state.update { it.copy(isLoading = true) }
                    }
                    else -> {}
                }
            }
        }
    }

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            ProfileIntent.LoadProfile -> observeAuthState()
            is ProfileIntent.UpdateUser -> {
                viewModelScope.launch {
                    authRepository.updateProfile(intent.user)
                }
            }
            ProfileIntent.Logout -> {
                viewModelScope.launch {
                    authRepository.logout()
                }
            }
        }
    }
}
