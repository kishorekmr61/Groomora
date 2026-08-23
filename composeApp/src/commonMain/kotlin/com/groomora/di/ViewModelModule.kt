package com.groomora.di

import com.groomora.feature.auth.AuthViewModel
import com.groomora.feature.beauty.BeautyViewModel
import com.groomora.feature.booking.BookingHistoryViewModel
import com.groomora.feature.booking.BookingViewModel
import com.groomora.feature.bridal.BridalViewModel
import com.groomora.feature.discovery.DiscoveryViewModel
import com.groomora.feature.discovery.ProfessionalProfileViewModel
import com.groomora.feature.favorites.FavoritesViewModel
import com.groomora.feature.home.HomeViewModel
import com.groomora.feature.homeservice.HomeServiceViewModel
import com.groomora.feature.loyalty.LoyaltyViewModel
import com.groomora.feature.notifications.NotificationsViewModel
import com.groomora.feature.offers.OffersViewModel
import com.groomora.feature.products.OrdersViewModel
import com.groomora.feature.products.ProductViewModel
import com.groomora.feature.profile.AddressManagementViewModel
import com.groomora.feature.profile.ProfileViewModel
import com.groomora.feature.profile.SettingsViewModel
import com.groomora.feature.reviews.ReviewViewModel
import com.groomora.feature.shop.ShopDetailsViewModel
import com.groomora.feature.support.SupportViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { AuthViewModel(authRepository = get()) }
    factory { HomeViewModel(configRepository = get(), locationRepository = get()) }
    factory { DiscoveryViewModel(discoveryRepository = get()) }
    factory { ProfessionalProfileViewModel(discoveryRepository = get()) }
    factory { ShopDetailsViewModel(shopDetailsRepository = get()) }
    factory { BookingViewModel(bookingRepository = get(), locationRepository = get(), offersRepository = get()) }
    factory { BookingHistoryViewModel(bookingRepository = get()) }
    factory { OffersViewModel(offersRepository = get()) }
    factory { LoyaltyViewModel(loyaltyRepository = get()) }
    factory { ProfileViewModel(authRepository = get()) }
    factory { AddressManagementViewModel(locationRepository = get()) }
    factory { SettingsViewModel() }
    factory { ProductViewModel(productRepository = get()) }
    factory { OrdersViewModel(orderRepository = get()) }
    factory { BridalViewModel(bridalRepository = get()) }
    factory { BeautyViewModel(beautyRepository = get()) }
    factory { NotificationsViewModel(notificationRepository = get()) }
    factory { ReviewViewModel(reviewRepository = get()) }
    factory { HomeServiceViewModel(homeServiceRepository = get()) }
    factory { FavoritesViewModel(favoritesRepository = get()) }
    factory { SupportViewModel(supportRepository = get()) }
}
