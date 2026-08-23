package com.groomora.di

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
import com.groomora.feature.notifications.MockNotificationRepository
import com.groomora.feature.notifications.NotificationRepository
import com.groomora.feature.offers.MockOffersRepository
import com.groomora.feature.offers.OffersRepository
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
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> { MockAuthRepository() }
    single<DiscoveryRepository> { MockDiscoveryRepository() }
    single<ShopDetailsRepository> { MockShopDetailsRepository() }
    single<BookingRepository> { MockBookingRepository() }
    single<OffersRepository> { MockOffersRepository() }
    single<LoyaltyRepository> { MockLoyaltyRepository() }
    single<ProductRepository> { MockProductRepository() }
    single<OrderRepository> { MockOrderRepository() }
    single<BridalRepository> { MockBridalRepository() }
    single<BeautyRepository> { MockBeautyRepository() }
    single<NotificationRepository> { MockNotificationRepository() }
    single<ReviewRepository> { MockReviewRepository() }
    single<HomeServiceRepository> { MockHomeServiceRepository() }
    single<FavoritesRepository> { MockFavoritesRepository() }
    single<SupportRepository> { MockSupportRepository() }
}
