package com.groomora.app

import com.groomora.core.configuration.ConfigRepository
import com.groomora.core.configuration.MockConfigRepository
import com.groomora.core.location.LocationRepository
import com.groomora.core.location.MockLocationRepository
import com.groomora.core.geo.GeoRulesEngine
import com.groomora.feature.auth.AuthRepository
import com.groomora.feature.auth.MockAuthRepository
import com.groomora.feature.discovery.DiscoveryRepository
import com.groomora.feature.discovery.MockDiscoveryRepository
import com.groomora.feature.shop.ShopDetailsRepository
import com.groomora.feature.shop.MockShopDetailsRepository
import com.groomora.feature.booking.BookingRepository
import com.groomora.feature.booking.MockBookingRepository
import com.groomora.feature.offers.OffersRepository
import com.groomora.feature.offers.MockOffersRepository
import com.groomora.feature.loyalty.LoyaltyRepository
import com.groomora.feature.loyalty.MockLoyaltyRepository
import com.groomora.feature.products.ProductRepository
import com.groomora.feature.products.MockProductRepository
import com.groomora.feature.products.OrderRepository
import com.groomora.feature.products.MockOrderRepository
import com.groomora.feature.bridal.BridalRepository
import com.groomora.feature.bridal.MockBridalRepository
import com.groomora.feature.notifications.NotificationRepository
import com.groomora.feature.notifications.MockNotificationRepository
import com.groomora.feature.reviews.ReviewRepository
import com.groomora.feature.reviews.MockReviewRepository
import com.groomora.feature.homeservice.HomeServiceRepository
import com.groomora.feature.homeservice.MockHomeServiceRepository
import com.groomora.feature.favorites.FavoritesRepository
import com.groomora.feature.favorites.MockFavoritesRepository

/**
 * Simple dependency container for the Groomora App.
 * In a production app, this would be replaced by a DI framework like Koin or Hilt.
 */
object DependencyContainer {
    val configRepository: ConfigRepository by lazy { MockConfigRepository() }
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
    val notificationRepository: NotificationRepository by lazy { MockNotificationRepository() }
    val reviewRepository: ReviewRepository by lazy { MockReviewRepository() }
    val homeServiceRepository: HomeServiceRepository by lazy { MockHomeServiceRepository() }
    val favoritesRepository: FavoritesRepository by lazy { MockFavoritesRepository() }
}
