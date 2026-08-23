package com.groomora.feature.auth

import com.groomora.app.DependencyContainer
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockAuthRepository : AuthRepository {
    private val _authState = MutableStateFlow<AuthState>(
        AuthState.Authenticated(
            User(
                id = "user_default_1",
                name = "Alex Morgan",
                phoneNumber = "+91 98765 43210",
                email = "alex.morgan@groomora.com",
                profileImageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&q=80",
                gender = UserGender.FEMALE
            )
        )
    )
    override val authState: Flow<AuthState> = _authState.asStateFlow()


    override suspend fun login(phoneNumber: String, otp: String): AuthState {
        if (!DependencyContainer.networkConnectivityManager.isConnected.value) {
            val errorState = AuthState.Error("No internet connection. Please check your network to sign in.")
            _authState.value = errorState
            return errorState
        }
        _authState.value = AuthState.Loading
        delay(800)
        val user = User(
            id = "user_123",
            name = "Groomora User",
            phoneNumber = phoneNumber,
            email = "user@groomora.com",
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
        if (!DependencyContainer.networkConnectivityManager.isConnected.value) {
            val errorState = AuthState.Error("No internet connection. Please check your network to create an account.")
            _authState.value = errorState
            return errorState
        }
        _authState.value = AuthState.Loading
        delay(800)
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

    override suspend fun updateProfile(user: User): AuthState {
        if (!DependencyContainer.networkConnectivityManager.isConnected.value) {
            val errorState = AuthState.Error("No internet connection. Please check your network to update profile.")
            _authState.value = errorState
            return errorState
        }
        _authState.value = AuthState.Authenticated(user)
        return _authState.value
    }

    override suspend fun logout() {
        delay(200)
        _authState.value = AuthState.Unauthenticated
    }
}
