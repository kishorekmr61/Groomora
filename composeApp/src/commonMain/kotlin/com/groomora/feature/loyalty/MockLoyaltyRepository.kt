package com.groomora.feature.loyalty

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockLoyaltyRepository : LoyaltyRepository {
    override fun getLoyaltyProfile(): Flow<LoyaltyProfile?> = flow {
        delay(500)
        emit(
            LoyaltyProfile(
                pointsBalance = 1250,
                memberLevel = "Gold",
                pointsToNextLevel = 750,
                referralCode = "GROOM1250"
            )
        )
    }

    override fun getTransactions(): Flow<List<LoyaltyTransaction>> = flow {
        delay(700)
        emit(
            listOf(
                LoyaltyTransaction("t1", TransactionType.EARNED, 100, "Oct 20, 2024", "Haircut at The Golden Scissor"),
                LoyaltyTransaction("t2", TransactionType.EARNED, 500, "Oct 15, 2024", "Referral Bonus"),
                LoyaltyTransaction("t3", TransactionType.REDEEMED, 200, "Oct 10, 2024", "Discount on Beard Trim")
            )
        )
    }

    override fun getMembershipPlans(): Flow<List<MembershipPlan>> = flow {
        delay(400)
        emit(
            listOf(
                MembershipPlan("m1", "Silver", 499.0, listOf("5% extra points", "Priority booking"), 6),
                MembershipPlan("m2", "Gold", 999.0, listOf("10% extra points", "Free head massage monthly", "Priority booking"), 12)
            )
        )
    }

    override suspend fun redeemPoints(points: Int): Boolean {
        delay(1000)
        return true
    }

    override suspend fun joinMembership(planId: String): Boolean {
        delay(1500)
        return true
    }
}
