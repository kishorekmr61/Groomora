package com.groomora.app

import com.groomora.core.analytics.AnalyticsManager
import com.groomora.core.analytics.DefaultAnalyticsManager
import com.groomora.core.api.*
import com.groomora.core.configuration.ConfigRepository
import com.groomora.core.configuration.MockConfigRepository
import com.groomora.core.crash.CrashReporter
import com.groomora.core.crash.DefaultCrashReporter
import com.groomora.core.geo.GeoRulesEngine
import com.groomora.core.location.LocationRepository
import com.groomora.core.location.MockLocationRepository
import com.groomora.feature.auth.AuthRepository
import com.groomora.feature.auth.MockAuthRepository
import com.groomora.feature.beauty.BeautyRepository
import com.groomora.feature.beauty.MockBeautyRepository
import com.groomora.feature.booking.BookingRepository
import com.groomora.feature.booking.MockBookingRepository
import com.groomora.feature.bridal.BridalRepository
import com.groomora.feature.bridal.MockBridalRepository
import com.groomora.feature.discovery.DiscoveryRepository
import com.groomora.feature.discovery.MockDiscoveryRepository
import com.groomora.feature.favorites.FavoritesRepository
import com.groomora.feature.favorites.MockFavoritesRepository
import com.groomora.feature.homeservice.HomeServiceRepository
import com.groomora.feature.homeservice.MockHomeServiceRepository
import com.groomora.feature.loyalty.LoyaltyRepository
import com.groomora.feature.loyalty.MockLoyaltyRepository
import com.groomora.feature.notifications.NotificationRepository
import com.groomora.feature.notifications.MockNotificationRepository
import com.groomora.feature.offers.OffersRepository
import com.groomora.feature.offers.MockOffersRepository
import com.groomora.feature.products.MockOrderRepository
import com.groomora.feature.products.MockProductRepository
import com.groomora.feature.products.OrderRepository
import com.groomora.feature.products.ProductRepository
import com.groomora.feature.reviews.MockReviewRepository
import com.groomora.feature.reviews.ReviewRepository
import com.groomora.feature.shop.MockShopDetailsRepository
import com.groomora.feature.shop.ShopDetailsRepository
import com.groomora.feature.support.MockSupportRepository
import com.groomora.feature.support.SupportRepository

/**
 * Dependency container for the Groomora Customer App.
 */
object DependencyContainer {
    val analyticsManager: AnalyticsManager by lazy { DefaultAnalyticsManager() }
    val networkConnectivityManager: com.groomora.core.network.NetworkConnectivityManager by lazy { com.groomora.core.network.DefaultNetworkConnectivityManager() }
    val crashReporter: CrashReporter by lazy { DefaultCrashReporter() }
    val configRepository: ConfigRepository by lazy { MockConfigRepository() }

    // ==========================================
    // CENTRALIZED API SERVICES LAYER
    // ==========================================
    val authApiService: AuthApiService by lazy { MockAuthApiService() }
    val discoveryApiService: DiscoveryApiService by lazy { MockDiscoveryApiService() }
    val shopApiService: ShopApiService by lazy { MockShopApiService() }
    val bookingApiService: BookingApiService by lazy { MockBookingApiService() }
    val offersApiService: OffersApiService by lazy { MockOffersApiService() }
    val loyaltyApiService: LoyaltyApiService by lazy { MockLoyaltyApiService() }
    val productApiService: ProductApiService by lazy { MockProductApiService() }
    val specialServicesApiService: SpecialServicesApiService by lazy { MockSpecialServicesApiService() }
    val reviewApiService: ReviewApiService by lazy { MockReviewApiService() }
    val notificationApiService: NotificationApiService by lazy { MockNotificationApiService() }
    val favoritesApiService: FavoritesApiService by lazy { MockFavoritesApiService() }

    // ==========================================
    // DOMAIN REPOSITORIES
    // ==========================================
    val locationRepository: LocationRepository by lazy { MockLocationRepository() }
    val geoRulesEngine: GeoRulesEngine by lazy { GeoRulesEngine() }
    val authRepository: AuthRepository by lazy { MockAuthRepository() }
    val discoveryRepository: DiscoveryRepository by lazy { MockDiscoveryRepository() }
    val shopDetailsRepository: ShopDetailsRepository by lazy { MockShopDetailsRepository() }
    val bookingRepository: BookingRepository by lazy { MockBookingRepository() }
    val offersRepository: OffersRepository by lazy { MockOffersRepository() }
    val loyaltyRepository: LoyaltyRepository by lazy { MockLoyaltyRepository() }
    val productRepository: ProductRepository by lazy { MockProductRepository() }
    val orderRepository: OrderRepository by lazy { MockOrderRepository() }
    val bridalRepository: BridalRepository by lazy { MockBridalRepository() }
    val beautyRepository: BeautyRepository by lazy { MockBeautyRepository() }
    val notificationRepository: NotificationRepository by lazy { MockNotificationRepository() }
    val reviewRepository: ReviewRepository by lazy { MockReviewRepository() }
    val homeServiceRepository: HomeServiceRepository by lazy { MockHomeServiceRepository() }
    val favoritesRepository: FavoritesRepository by lazy { MockFavoritesRepository() }
    val supportRepository: SupportRepository by lazy { MockSupportRepository() }
}
