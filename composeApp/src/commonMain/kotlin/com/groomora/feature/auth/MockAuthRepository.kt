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
        delay(1000)
        val user = User(
            id = "user_123",
            name = "Groomora User",
            phoneNumber = phoneNumber,
            gender = UserGender.MALE
        )
        _authState.value = AuthState.Authenticated(user)
        return _authState.value
    }

    override suspend fun signUp(
        name: String,
        phoneNumber: String,
        email: String,
        gender: UserGender,
        password: String,
        referralCode: String?
    ): AuthState {
        _authState.value = AuthState.Loading
        delay(1200)
        val user = User(
            id = "user_${(1000..9999).random()}",
            name = name,
            phoneNumber = phoneNumber,
            email = email,
            gender = gender
        )
        _authState.value = AuthState.Authenticated(user)
        return _authState.value
    }

    override suspend fun logout() {
        delay(300)
        _authState.value = AuthState.Unauthenticated
    }

    override suspend fun updateProfile(user: User): AuthState {
        delay(800)
        _authState.value = AuthState.Authenticated(user)
        return _authState.value
    }
}
