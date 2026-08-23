package com.groomora.feature.loyalty

import kotlinx.coroutines.flow.Flow

interface LoyaltyRepository {
    fun getLoyaltyProfile(): Flow<LoyaltyProfile?>
    fun getTransactions(): Flow<List<LoyaltyTransaction>>
    fun getMembershipPlans(): Flow<List<MembershipPlan>>
    suspend fun redeemPoints(points: Int): Boolean
    suspend fun joinMembership(planId: String): Boolean
}
