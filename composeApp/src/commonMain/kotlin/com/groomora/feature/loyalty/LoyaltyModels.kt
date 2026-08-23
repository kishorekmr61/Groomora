package com.groomora.feature.loyalty

import kotlinx.serialization.Serializable

@Serializable
data class LoyaltyProfile(
    val pointsBalance: Int,
    val memberLevel: String,
    val pointsToNextLevel: Int,
    val referralCode: String
)

@Serializable
data class LoyaltyTransaction(
    val id: String,
    val type: TransactionType,
    val points: Int,
    val date: String,
    val description: String
)

enum class TransactionType {
    EARNED, REDEEMED, EXPIRED, REFERRAL_BONUS
}

@Serializable
data class MembershipPlan(
    val id: String,
    val name: String,
    val price: Double,
    val benefits: List<String>,
    val durationMonths: Int
)

data class LoyaltyState(
    val isLoading: Boolean = false,
    val profile: LoyaltyProfile? = null,
    val transactions: List<LoyaltyTransaction> = emptyList(),
    val membershipPlans: List<MembershipPlan> = emptyList(),
    val error: String? = null
)
