package com.groomora.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    val authState: StateFlow<AuthState> = authRepository.authState
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AuthState.Idle)

    fun onIntent(intent: AuthIntent) {
        viewModelScope.launch {
            when (intent) {
                is AuthIntent.Login -> authRepository.login(intent.phoneNumber, intent.otp)
                is AuthIntent.SignUp -> authRepository.signUp(
                    name = intent.name,
                    phoneNumber = intent.phoneNumber,
                    email = intent.email,
                    gender = intent.gender,
                    password = intent.password,
                    referralCode = intent.referralCode
                )
                AuthIntent.Logout -> authRepository.logout()
                is AuthIntent.UpdateProfile -> authRepository.updateProfile(intent.user)
            }
        }
    }
}
