package com.groomora.core.ads

/**
 * Lifecycle state for individual ad requests.
 */
sealed interface AdState {
    data object Idle : AdState
    data object Loading : AdState
    data object Ready : AdState
    data class Displayed(val impressionTime: Long) : AdState
    data class Error(val message: String) : AdState
}

/**
 * Cross-platform Google Ad Manager interface.
 */
interface AdManager {
    fun initialize()
    fun isAdsEnabled(): Boolean
    fun setAdsEnabled(enabled: Boolean)
    fun showInterstitial(
        adUnitId: String? = null,
        onAdShowed: () -> Unit = {},
        onDismissed: () -> Unit = {}
    )
    fun showRewarded(
        adUnitId: String? = null,
        onRewarded: (amount: Int, type: String) -> Unit,
        onDismissed: () -> Unit = {}
    )
    fun logAdImpression(adType: String, adUnitId: String)
    fun logAdClick(adType: String, adUnitId: String)
}
