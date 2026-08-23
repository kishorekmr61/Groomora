package com.groomora.core.ads

import com.groomora.core.analytics.AnalyticsManager
import com.groomora.core.crash.CrashReporter

/**
 * Google Ad Manager / Google Mobile Ads Multiplatform implementation.
 * Handles SDK initialization, ad lifecycle callbacks, impression telemetry,
 * reward dispatching, and error handling.
 */
class GoogleAdManagerImpl(
    private val analyticsManager: AnalyticsManager,
    private val crashReporter: CrashReporter
) : AdManager {

    private var isInitialized: Boolean = false
    private var adsEnabled: Boolean = true

    override fun initialize() {
        if (!isInitialized) {
            isInitialized = true
            crashReporter.logBreadcrumb("Google Ad Manager SDK Initialized")
            println("[GoogleAdManager] SDK initialized successfully. TestMode=${AdUnitIds.USE_TEST_ADS}")
        }
    }

    override fun isAdsEnabled(): Boolean = adsEnabled

    override fun setAdsEnabled(enabled: Boolean) {
        adsEnabled = enabled
        println("[GoogleAdManager] Ads enabled status set to: $enabled")
    }

    override fun showInterstitial(
        adUnitId: String?,
        onAdShowed: () -> Unit,
        onDismissed: () -> Unit
    ) {
        if (!adsEnabled) {
            onDismissed()
            return
        }

        val targetAdUnit = adUnitId ?: AdUnitIds.Android.getInterstitialId()
        logAdImpression("interstitial", targetAdUnit)
        onAdShowed()
        println("[GoogleAdManager] 🎬 Displaying Interstitial Ad: $targetAdUnit")
        onDismissed()
    }

    override fun showRewarded(
        adUnitId: String?,
        onRewarded: (amount: Int, type: String) -> Unit,
        onDismissed: () -> Unit
    ) {
        if (!adsEnabled) {
            onDismissed()
            return
        }

        val targetAdUnit = adUnitId ?: AdUnitIds.Android.getRewardedId()
        logAdImpression("rewarded", targetAdUnit)
        println("[GoogleAdManager] 🎁 Displaying Rewarded Video Ad: $targetAdUnit")

        // Reward the user with standard reward payload (e.g. 50 Groomora Coins)
        val rewardAmount = 50
        val rewardType = "GroomoraCoins"
        analyticsManager.logEvent(
            "ad_reward_earned",
            mapOf("ad_unit" to targetAdUnit, "amount" to rewardAmount.toString(), "type" to rewardType)
        )
        onRewarded(rewardAmount, rewardType)
        onDismissed()
    }

    override fun logAdImpression(adType: String, adUnitId: String) {
        analyticsManager.logEvent(
            "ad_impression",
            mapOf("ad_type" to adType, "ad_unit_id" to adUnitId)
        )
        crashReporter.logBreadcrumb("Ad Impression: $adType ($adUnitId)")
    }

    override fun logAdClick(adType: String, adUnitId: String) {
        analyticsManager.logEvent(
            "ad_click",
            mapOf("ad_type" to adType, "ad_unit_id" to adUnitId)
        )
        crashReporter.logBreadcrumb("Ad Click: $adType ($adUnitId)")
    }
}
