package com.groomora.di

import com.groomora.core.analytics.AnalyticsManager
import com.groomora.core.analytics.DefaultAnalyticsManager
import com.groomora.core.configuration.ConfigRepository
import com.groomora.core.configuration.MockConfigRepository
import com.groomora.core.crash.CrashReporter
import com.groomora.core.crash.FirebaseCrashReporter
import com.groomora.core.geo.GeoRulesEngine
import com.groomora.core.location.LocationRepository
import com.groomora.core.location.MockLocationRepository
import com.groomora.core.network.DefaultNetworkConnectivityManager
import com.groomora.core.network.NetworkConnectivityManager
import com.groomora.core.ads.AdManager
import com.groomora.core.ads.GoogleAdManagerImpl
import org.koin.dsl.module

val appModule = module {
    single<AnalyticsManager> { DefaultAnalyticsManager() }
    single<CrashReporter> { FirebaseCrashReporter() }
    single<NetworkConnectivityManager> { DefaultNetworkConnectivityManager() }
    single<ConfigRepository> { MockConfigRepository() }
    single<LocationRepository> { MockLocationRepository() }
    single { GeoRulesEngine() }
    single<AdManager> { GoogleAdManagerImpl(analyticsManager = get(), crashReporter = get()) }
}

