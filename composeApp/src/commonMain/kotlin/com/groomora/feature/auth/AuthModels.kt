package com.groomora.feature.auth

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val phoneNumber: String,
    val email: String? = null,
    val profileImageUrl: String? = null,
    val gender: UserGender = UserGender.UNSPECIFIED
)

enum class UserGender {
    MALE, FEMALE, UNSPECIFIED
}

sealed interface AuthState {
    data object Idle : AuthState
    data object Loading : AuthState
    data class Authenticated(val user: User) : AuthState
    data object Unauthenticated : AuthState
    data class Error(val message: String) : AuthState
}

sealed interface AuthIntent {
    data class Login(val phoneNumber: String, val otp: String) : AuthIntent
    data object Logout : AuthIntent
    data class UpdateProfile(val user: User) : AuthIntent
}
