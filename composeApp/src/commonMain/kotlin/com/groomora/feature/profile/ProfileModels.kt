package com.groomora.feature.profile

import com.groomora.feature.auth.User
import kotlinx.serialization.Serializable

data class ProfileState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

sealed interface ProfileIntent {
    data object LoadProfile : ProfileIntent
    data class UpdateUser(val user: User) : ProfileIntent
    data object Logout : ProfileIntent
}
