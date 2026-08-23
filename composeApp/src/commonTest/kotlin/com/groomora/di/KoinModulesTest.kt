package com.groomora.di

import com.groomora.core.analytics.AnalyticsManager
import com.groomora.core.api.AuthApiService
import com.groomora.core.api.BookingApiService
import com.groomora.core.configuration.ConfigRepository
import com.groomora.core.crash.CrashReporter
import com.groomora.core.location.LocationRepository
import com.groomora.feature.auth.AuthRepository
import com.groomora.feature.booking.BookingRepository
import com.groomora.feature.discovery.DiscoveryRepository
import com.groomora.feature.offers.OffersRepository
import com.groomora.feature.products.ProductRepository
import com.groomora.feature.shop.ShopDetailsRepository
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class KoinModulesTest : KoinTest {

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(
                appModule,
                networkModule,
                repositoryModule,
                viewModelModule
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun testCoreServicesResolution() {
        val analytics: AnalyticsManager = get()
        assertNotNull(analytics)

        val crashReporter: CrashReporter = get()
        assertNotNull(crashReporter)

        val configRepo: ConfigRepository = get()
        assertNotNull(configRepo)

        val locationRepo: LocationRepository = get()
        assertNotNull(locationRepo)
    }

    @Test
    fun testApiServicesResolution() {
        val authApi: AuthApiService = get()
        assertNotNull(authApi)

        val bookingApi: BookingApiService = get()
        assertNotNull(bookingApi)
    }

    @Test
    fun testRepositoryResolution() {
        val authRepo: AuthRepository = get()
        assertNotNull(authRepo)

        val bookingRepo: BookingRepository = get()
        assertNotNull(bookingRepo)

        val discoveryRepo: DiscoveryRepository = get()
        assertNotNull(discoveryRepo)

        val shopRepo: ShopDetailsRepository = get()
        assertNotNull(shopRepo)

        val offersRepo: OffersRepository = get()
        assertNotNull(offersRepo)

        val productRepo: ProductRepository = get()
        assertNotNull(productRepo)
    }
}
