package com.groomora.feature.discovery

import kotlinx.serialization.Serializable

@Serializable
data class ProfessionalDetail(
    val id: String,
    val name: String,
    val role: String,
    val rating: Double,
    val reviewCount: Int,
    val imageUrl: String,
    val bio: String,
    val skills: List<String>,
    val portfolioImages: List<String>,
    val yearsOfExperience: Int,
    val isVerified: Boolean = true
)

data class ProfessionalProfileState(
    val isLoading: Boolean = false,
    val professional: ProfessionalDetail? = null,
    val error: String? = null
)

sealed interface ProfessionalProfileIntent {
    data class LoadProfile(val id: String) : ProfessionalProfileIntent
}
