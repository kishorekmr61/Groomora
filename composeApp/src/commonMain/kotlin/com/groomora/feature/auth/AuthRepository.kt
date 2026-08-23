package com.groomora.feature.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authState: Flow<AuthState>
    suspend fun login(phoneNumber: String, otp: String): AuthState
    suspend fun logout()
    suspend fun updateProfile(user: User): AuthState
}
