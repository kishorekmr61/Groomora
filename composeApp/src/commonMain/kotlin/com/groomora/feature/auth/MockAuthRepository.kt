package com.groomora.feature.auth

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockAuthRepository : AuthRepository {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    override val authState: Flow<AuthState> = _authState.asStateFlow()

    override suspend fun login(phoneNumber: String, otp: String): AuthState {
        _authState.value = AuthState.Loading
        delay(1500) // Simulate network
        val user = User(
            id = "user_123",
            name = "John Doe",
            phoneNumber = phoneNumber,
            gender = UserGender.MALE
        )
        _authState.value = AuthState.Authenticated(user)
        return _authState.value
    }

    override suspend fun logout() {
        delay(500)
        _authState.value = AuthState.Unauthenticated
    }

    override suspend fun updateProfile(user: User): AuthState {
        delay(1000)
        _authState.value = AuthState.Authenticated(user)
        return _authState.value
    }
}
