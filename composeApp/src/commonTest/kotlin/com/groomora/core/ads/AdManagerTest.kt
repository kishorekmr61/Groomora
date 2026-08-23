package com.groomora.core.ads

import com.groomora.core.analytics.DefaultAnalyticsManager
import com.groomora.core.crash.FirebaseCrashReporter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AdManagerTest {

    @Test
    fun testAdUnitIdsTestMode() {
        AdUnitIds.USE_TEST_ADS = true
        assertEquals("ca-app-pub-3940256099942544/6300978111", AdUnitIds.Android.getBannerId())
        assertEquals("ca-app-pub-3940256099942544/1033173712", AdUnitIds.Android.getInterstitialId())
        assertEquals("ca-app-pub-3940256099942544/5224354917", AdUnitIds.Android.getRewardedId())
        assertEquals("ca-app-pub-3940256099942544/2247696110", AdUnitIds.Android.getNativeId())

        assertEquals("ca-app-pub-3940256099942544/2934735716", AdUnitIds.iOS.getBannerId())
        assertEquals("ca-app-pub-3940256099942544/4411468910", AdUnitIds.iOS.getInterstitialId())
    }

    @Test
    fun testInterstitialAdLifecycle() {
        val analytics = DefaultAnalyticsManager()
        val crashReporter = FirebaseCrashReporter()
        val adManager = GoogleAdManagerImpl(analytics, crashReporter)
        adManager.initialize()

        var showed = false
        var dismissed = false

        adManager.showInterstitial(
            onAdShowed = { showed = true },
            onDismissed = { dismissed = true }
        )

        assertTrue(showed)
        assertTrue(dismissed)
    }

    @Test
    fun testRewardedAdPayout() {
        val analytics = DefaultAnalyticsManager()
        val crashReporter = FirebaseCrashReporter()
        val adManager = GoogleAdManagerImpl(analytics, crashReporter)
        adManager.initialize()

        var rewardReceived = 0
        var rewardType = ""
        var dismissed = false

        adManager.showRewarded(
            onRewarded = { amount, type ->
                rewardReceived = amount
                rewardType = type
            },
            onDismissed = { dismissed = true }
        )

        assertEquals(50, rewardReceived)
        assertEquals("GroomoraCoins", rewardType)
        assertTrue(dismissed)
    }

    @Test
    fun testAdDisablingToggle() {
        val analytics = DefaultAnalyticsManager()
        val crashReporter = FirebaseCrashReporter()
        val adManager = GoogleAdManagerImpl(analytics, crashReporter)

        adManager.setAdsEnabled(false)
        assertFalse(adManager.isAdsEnabled())

        var rewardReceived = 0
        adManager.showRewarded(
            onRewarded = { amount, _ -> rewardReceived = amount },
            onDismissed = {}
        )
        assertEquals(0, rewardReceived) // No reward when ads disabled
    }
}
