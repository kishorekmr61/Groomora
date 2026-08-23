package com.groomora.di

import com.groomora.core.api.*
import org.koin.dsl.module

val networkModule = module {
    single<AuthApiService> { MockAuthApiService() }
    single<DiscoveryApiService> { MockDiscoveryApiService() }
    single<ShopApiService> { MockShopApiService() }
    single<BookingApiService> { MockBookingApiService() }
    single<OffersApiService> { MockOffersApiService() }
    single<LoyaltyApiService> { MockLoyaltyApiService() }
    single<ProductApiService> { MockProductApiService() }
    single<SpecialServicesApiService> { MockSpecialServicesApiService() }
    single<ReviewApiService> { MockReviewApiService() }
    single<NotificationApiService> { MockNotificationApiService() }
    single<FavoritesApiService> { MockFavoritesApiService() }
}
