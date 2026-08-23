package com.groomora.app

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.groomora.core.navigation.Screen
import com.groomora.design.GroomoraTheme
import com.groomora.feature.home.HomeScreen
import com.groomora.feature.home.HomeViewModel
import com.groomora.feature.discovery.DiscoveryScreen
import com.groomora.feature.discovery.DiscoveryViewModel
import com.groomora.feature.discovery.ProfessionalProfileScreen
import com.groomora.feature.discovery.ProfessionalProfileViewModel
import com.groomora.feature.booking.BookingScreen
import com.groomora.feature.booking.BookingViewModel
import com.groomora.feature.shop.ShopDetailsScreen
import com.groomora.feature.shop.ShopDetailsViewModel
import com.groomora.feature.offers.OffersScreen
import com.groomora.feature.offers.OffersViewModel
import com.groomora.feature.loyalty.LoyaltyScreen
import com.groomora.feature.loyalty.LoyaltyViewModel
import com.groomora.feature.profile.ProfileScreen
import com.groomora.feature.profile.ProfileViewModel
import com.groomora.feature.profile.AddressManagementScreen
import com.groomora.feature.profile.AddressManagementViewModel
import com.groomora.feature.profile.SettingsScreen
import com.groomora.feature.profile.SettingsViewModel
import com.groomora.feature.products.ProductScreen
import com.groomora.feature.products.ProductViewModel
import com.groomora.feature.products.ProductDetailsScreen
import com.groomora.feature.products.CartScreen
import com.groomora.feature.products.OrdersScreen
import com.groomora.feature.products.OrdersViewModel
import com.groomora.feature.bridal.BridalScreen
import com.groomora.feature.bridal.BridalViewModel
import com.groomora.feature.notifications.NotificationsScreen
import com.groomora.feature.notifications.NotificationsViewModel
import com.groomora.feature.reviews.ReviewScreen
import com.groomora.feature.reviews.ReviewViewModel
import com.groomora.feature.reviews.ReviewTargetType
import com.groomora.feature.homeservice.HomeServiceScreen
import com.groomora.feature.homeservice.HomeServiceViewModel
import com.groomora.feature.onboarding.OnboardingScreen
import com.groomora.feature.auth.LoginScreen
import com.groomora.feature.auth.AuthViewModel
import com.groomora.feature.favorites.FavoritesScreen
import com.groomora.feature.favorites.FavoritesViewModel

@Composable
fun App() {
    GroomoraTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screen.Onboarding
        ) {
            composable<Screen.Onboarding> {
                OnboardingScreen(
                    onFinish = {
                        navController.navigate(Screen.Login) {
                            popUpTo(Screen.Onboarding) { inclusive = true }
                        }
                    }
                )
            }
            composable<Screen.Login> {
                val authViewModel: AuthViewModel = viewModel {
                    AuthViewModel(authRepository = DependencyContainer.authRepository)
                }
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.Login) { inclusive = true }
                        }
                    }
                )
            }
            composable<Screen.Home> {
                val homeViewModel: HomeViewModel = viewModel {
                    HomeViewModel(
                        configRepository = DependencyContainer.configRepository,
                        locationRepository = DependencyContainer.locationRepository
                    )
                }
                HomeScreen(
                    viewModel = homeViewModel,
                    onNavigateToDiscovery = { categoryId ->
                        navController.navigate(Screen.Discovery(categoryId = categoryId))
                    },
                    onNavigateToOffers = { navController.navigate(Screen.Offers) },
                    onNavigateToProducts = { navController.navigate(Screen.Products) },
                    onNavigateToProfile = { navController.navigate(Screen.Profile) },
                    onNavigateToLoyalty = { navController.navigate(Screen.Loyalty) },
                    onNavigateToBridal = { navController.navigate(Screen.Bridal) },
                    onNavigateToHomeService = { navController.navigate(Screen.HomeService) },
                    onNavigateToNotifications = { navController.navigate(Screen.Notifications) }
                )
            }
            composable<Screen.Discovery> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.Discovery>()
                val discoveryViewModel: DiscoveryViewModel = viewModel {
                    DiscoveryViewModel(
                        discoveryRepository = DependencyContainer.discoveryRepository
                    )
                }
                DiscoveryScreen(
                    categoryId = route.categoryId,
                    viewModel = discoveryViewModel,
                    onNavigateToShop = { shopId ->
                        navController.navigate(Screen.ShopDetails(shopId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.ShopDetails> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.ShopDetails>()
                val shopDetailsViewModel: ShopDetailsViewModel = viewModel {
                    ShopDetailsViewModel(
                        shopDetailsRepository = DependencyContainer.shopDetailsRepository
                    )
                }
                ShopDetailsScreen(
                    shopId = route.shopId,
                    viewModel = shopDetailsViewModel,
                    onNavigateToBooking = { serviceId ->
                        navController.navigate(Screen.Booking(serviceId = serviceId))
                    },
                    onNavigateToReviews = { id ->
                        navController.navigate(Screen.Reviews(targetId = id, type = ReviewTargetType.SHOP.name))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.ProfessionalProfile> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.ProfessionalProfile>()
                val professionalProfileViewModel: ProfessionalProfileViewModel = viewModel {
                    ProfessionalProfileViewModel(
                        discoveryRepository = DependencyContainer.discoveryRepository
                    )
                }
                ProfessionalProfileScreen(
                    professionalId = route.professionalId,
                    viewModel = professionalProfileViewModel,
                    onNavigateToBooking = { serviceId ->
                        navController.navigate(Screen.Booking(serviceId = serviceId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.Booking> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.Booking>()
                val bookingViewModel: BookingViewModel = viewModel {
                    BookingViewModel(
                        bookingRepository = DependencyContainer.bookingRepository,
                        locationRepository = DependencyContainer.locationRepository,
                        offersRepository = DependencyContainer.offersRepository
                    )
                }
//                BookingScreen(
//                    serviceId = route.serviceId ?: "",
//                    viewModel = bookingViewModel,
//                    onNavigateToBooking = { serviceId ->
//                        navController.navigate(Screen.Booking(serviceId = serviceId))
//                    },
//                    onBack = { navController.popBackStack() }
//                )
            }
            composable<Screen.Offers> {
                val offersViewModel: OffersViewModel = viewModel {
                    OffersViewModel(
                        offersRepository = DependencyContainer.offersRepository
                    )
                }
                OffersScreen(
                    viewModel = offersViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.Loyalty> {
                val loyaltyViewModel: LoyaltyViewModel = viewModel {
                    LoyaltyViewModel(
                        loyaltyRepository = DependencyContainer.loyaltyRepository
                    )
                }
                LoyaltyScreen(
                    viewModel = loyaltyViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.Profile> {
                val profileViewModel: ProfileViewModel = viewModel {
                    ProfileViewModel(
                        authRepository = DependencyContainer.authRepository
                    )
                }
                ProfileScreen(
                    viewModel = profileViewModel,
                    onNavigateToSettings = { navController.navigate(Screen.Settings) },
                    onNavigateToOrders = { navController.navigate(Screen.Orders) },
                    onNavigateToAddresses = { navController.navigate(Screen.AddressManagement) },
                    onNavigateToFavorites = { navController.navigate(Screen.Favorites) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.Settings> {
                val settingsViewModel: SettingsViewModel = viewModel {
                    SettingsViewModel()
                }
                SettingsScreen(
                    viewModel = settingsViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.AddressManagement> {
                val addressViewModel: AddressManagementViewModel = viewModel {
                    AddressManagementViewModel(
                        locationRepository = DependencyContainer.locationRepository
                    )
                }
                AddressManagementScreen(
                    viewModel = addressViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.Favorites> {
                val favoritesViewModel: FavoritesViewModel = viewModel {
                    FavoritesViewModel(
                        favoritesRepository = DependencyContainer.favoritesRepository
                    )
                }
                FavoritesScreen(
                    viewModel = favoritesViewModel,
                    onNavigateToShop = { shopId ->
                        navController.navigate(Screen.ShopDetails(shopId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.Products> {
                val productViewModel: ProductViewModel = viewModel {
                    ProductViewModel(
                        productRepository = DependencyContainer.productRepository
                    )
                }
                ProductScreen(
                    viewModel = productViewModel,
                    onNavigateToProductDetails = { productId ->
                        navController.navigate(Screen.ProductDetails(productId))
                    },
                    onNavigateToCart = { navController.navigate(Screen.Cart) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.ProductDetails> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.ProductDetails>()
                val productViewModel: ProductViewModel = viewModel {
                    ProductViewModel(
                        productRepository = DependencyContainer.productRepository
                    )
                }
                ProductDetailsScreen(
                    productId = route.productId,
                    viewModel = productViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.Cart> {
                val productViewModel: ProductViewModel = viewModel {
                    ProductViewModel(
                        productRepository = DependencyContainer.productRepository
                    )
                }
                CartScreen(
                    viewModel = productViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.Bridal> {
                val bridalViewModel: BridalViewModel = viewModel {
                    BridalViewModel(
                        bridalRepository = DependencyContainer.bridalRepository
                    )
                }
                BridalScreen(
                    viewModel = bridalViewModel,
                    onNavigateToBooking = { packageId ->
                        navController.navigate(Screen.Booking(packageId = packageId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.Orders> {
                val ordersViewModel: OrdersViewModel = viewModel {
                    OrdersViewModel(
                        orderRepository = DependencyContainer.orderRepository
                    )
                }
                OrdersScreen(
                    viewModel = ordersViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.Notifications> {
                val notificationsViewModel: NotificationsViewModel = viewModel {
                    NotificationsViewModel(
                        notificationRepository = DependencyContainer.notificationRepository
                    )
                }
                NotificationsScreen(
                    viewModel = notificationsViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.Reviews> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.Reviews>()
                val reviewViewModel: ReviewViewModel = viewModel {
                    ReviewViewModel(
                        reviewRepository = DependencyContainer.reviewRepository
                    )
                }
                ReviewScreen(
                    targetId = route.targetId,
                    type = ReviewTargetType.valueOf(route.type),
                    viewModel = reviewViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.HomeService> {
                val homeServiceViewModel: HomeServiceViewModel = viewModel {
                    HomeServiceViewModel(
                        homeServiceRepository = DependencyContainer.homeServiceRepository
                    )
                }
                HomeServiceScreen(
                    viewModel = homeServiceViewModel,
                    onNavigateToDiscovery = { category ->
                        navController.navigate(Screen.Discovery(categoryId = category))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
