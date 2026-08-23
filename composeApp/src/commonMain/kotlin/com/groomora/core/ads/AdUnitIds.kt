package com.groomora.core.ads

/**
 * Google Ad Manager & AdMob Ad Unit IDs.
 * Includes Google's official sample/test ad units for development and verification,
 * with slots for live production IDs.
 */
object AdUnitIds {

    // Toggle between Google Test Ads and Production Ads
    var USE_TEST_ADS: Boolean = true

    object Android {
        // Google Official Sample Ad Units for Android
        const val TEST_BANNER = "ca-app-pub-3940256099942544/6300978111"
        const val TEST_INTERSTITIAL = "ca-app-pub-3940256099942544/1033173712"
        const val TEST_REWARDED = "ca-app-pub-3940256099942544/5224354917"
        const val TEST_NATIVE = "ca-app-pub-3940256099942544/2247696110"

        // Production Ad Units (Replace with your GAM / AdMob IDs)
        var PROD_BANNER = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY"
        var PROD_INTERSTITIAL = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY"
        var PROD_REWARDED = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY"
        var PROD_NATIVE = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY"

        fun getBannerId(): String = if (USE_TEST_ADS) TEST_BANNER else PROD_BANNER
        fun getInterstitialId(): String = if (USE_TEST_ADS) TEST_INTERSTITIAL else PROD_INTERSTITIAL
        fun getRewardedId(): String = if (USE_TEST_ADS) TEST_REWARDED else PROD_REWARDED
        fun getNativeId(): String = if (USE_TEST_ADS) TEST_NATIVE else PROD_NATIVE
    }

    object iOS {
        // Google Official Sample Ad Units for iOS
        const val TEST_BANNER = "ca-app-pub-3940256099942544/2934735716"
        const val TEST_INTERSTITIAL = "ca-app-pub-3940256099942544/4411468910"
        const val TEST_REWARDED = "ca-app-pub-3940256099942544/1712485313"
        const val TEST_NATIVE = "ca-app-pub-3940256099942544/3986624511"

        // Production Ad Units (Replace with your GAM / AdMob IDs)
        var PROD_BANNER = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY"
        var PROD_INTERSTITIAL = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY"
        var PROD_REWARDED = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY"
        var PROD_NATIVE = "ca-app-pub-XXXXXXXXXXXXXXXX/YYYYYYYYYY"

        fun getBannerId(): String = if (USE_TEST_ADS) TEST_BANNER else PROD_BANNER
        fun getInterstitialId(): String = if (USE_TEST_ADS) TEST_INTERSTITIAL else PROD_INTERSTITIAL
        fun getRewardedId(): String = if (USE_TEST_ADS) TEST_REWARDED else PROD_REWARDED
        fun getNativeId(): String = if (USE_TEST_ADS) TEST_NATIVE else PROD_NATIVE
    }
}
