package com.groomora.feature.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<AuthState>
    suspend fun login(phoneNumber: String, otp: String): AuthState
    suspend fun signUp(
        name: String,
        phoneNumber: String,
        email: String,
        gender: UserGender,
        password: String,
        referralCode: String? = null
    ): AuthState
    suspend fun logout()
    suspend fun updateProfile(user: User): AuthState
}
