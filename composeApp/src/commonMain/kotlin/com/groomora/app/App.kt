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
import com.groomora.feature.discovery.GenderSelectionScreen
import com.groomora.feature.discovery.GenderServicesScreen
import com.groomora.feature.booking.BookingScreen
import com.groomora.feature.booking.BookingViewModel
import com.groomora.feature.booking.BookingHistoryScreen
import com.groomora.feature.booking.BookingHistoryViewModel
import com.groomora.feature.shop.ShopDetailsScreen
import com.groomora.feature.shop.ShopDetailsViewModel
import com.groomora.feature.offers.OffersScreen
import com.groomora.feature.offers.OffersViewModel
import com.groomora.feature.loyalty.LoyaltyScreen
import com.groomora.feature.loyalty.LoyaltyViewModel
import com.groomora.feature.loyalty.PaymentScreen
import com.groomora.feature.loyalty.PaymentSuccessScreen
import com.groomora.feature.products.ProductCheckoutScreen
import com.groomora.feature.profile.ProfileScreen
import com.groomora.feature.profile.ProfileViewModel
import com.groomora.feature.profile.EditProfileScreen
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
import com.groomora.feature.beauty.BeautyScreen
import com.groomora.feature.beauty.BeautyViewModel
import com.groomora.feature.notifications.NotificationsScreen
import com.groomora.feature.notifications.NotificationsViewModel
import com.groomora.feature.reviews.ReviewScreen
import com.groomora.feature.reviews.ReviewViewModel
import com.groomora.feature.reviews.ReviewTargetType
import com.groomora.feature.homeservice.HomeServiceScreen
import com.groomora.feature.homeservice.HomeServiceViewModel
import com.groomora.feature.services.ServicesCatalogScreen
import com.groomora.feature.onboarding.OnboardingScreen
import com.groomora.feature.onboarding.SplashScreen

import com.groomora.feature.auth.LoginScreen
import com.groomora.feature.auth.SignUpScreen
import com.groomora.feature.auth.AuthViewModel
import com.groomora.feature.auth.AuthState
import com.groomora.feature.auth.User
import com.groomora.feature.auth.UserGender



import com.groomora.feature.favorites.FavoritesScreen
import com.groomora.feature.favorites.FavoritesViewModel
import com.groomora.feature.support.SupportScreen
import com.groomora.feature.support.SupportViewModel

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.groomora.core.configuration.AppConfig
import com.groomora.core.configuration.UpdateStatus
import com.groomora.core.network.NoInternetScreen
import com.groomora.feature.maintenance.MaintenanceScreen
import com.groomora.feature.update.FlexibleUpdateBottomSheet
import com.groomora.feature.update.ForceUpdateScreen
import kotlinx.coroutines.launch



@Composable
fun App() {
    GroomoraTheme {
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()
        val isNetworkConnected by DependencyContainer.networkConnectivityManager.isConnected.collectAsState()
        val appConfig by DependencyContainer.configRepository.config.collectAsState(initial = AppConfig())

        // App version check (0.1.0 default client version)
        val currentAppVersion = "0.1.0"
        val updateStatus = remember(appConfig) {
            DependencyContainer.configRepository.checkUpdateStatus(currentAppVersion, isIos = false)
        }
        var showFlexibleUpdateDialog by remember { mutableStateOf(true) }

        val authState by DependencyContainer.authRepository.authState.collectAsState(
            initial = AuthState.Authenticated(
                User(
                    id = "user_default_1",
                    name = "Alex Morgan",
                    phoneNumber = "+91 98765 43210",
                    email = "alex.morgan@groomora.com",
                    profileImageUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&q=80",
                    gender = UserGender.FEMALE
                )
            )
        )
        var pendingBookingScreen by remember { mutableStateOf<Screen.Booking?>(null) }

        val navigateToBooking: (Screen.Booking) -> Unit = { bookingRoute ->
            if (authState is AuthState.Authenticated) {
                navController.navigate(bookingRoute)
            } else {
                pendingBookingScreen = bookingRoute
                navController.navigate(Screen.Login)
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            when {
                !isNetworkConnected -> {
                    NoInternetScreen(
                        onRetry = {
                            scope.launch {
                                DependencyContainer.configRepository.fetchConfig()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                appConfig.maintenance.isMaintenanceMode -> {
                    MaintenanceScreen(
                        maintenanceConfig = appConfig.maintenance,
                        onRefresh = {
                            scope.launch {
                                DependencyContainer.configRepository.fetchConfig()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }


                updateStatus is UpdateStatus.ForceUpdateRequired -> {
                    ForceUpdateScreen(
                        updateInfo = updateStatus,
                        modifier = Modifier.weight(1f)
                    )
                }

                else -> {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash,
                        modifier = Modifier.weight(1f)
                    ) {


            composable<Screen.Splash> {
                SplashScreen(
                    onSplashFinished = {
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.Splash) { inclusive = true }
                        }
                    }
                )
            }
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
                        val targetBooking = pendingBookingScreen
                        if (targetBooking != null) {
                            pendingBookingScreen = null
                            navController.popBackStack()
                            navController.navigate(targetBooking)
                        } else {
                            val previousBackStack = navController.previousBackStackEntry
                            if (previousBackStack != null && previousBackStack.destination.route?.contains("Login") == false && previousBackStack.destination.route?.contains("Onboarding") == false) {
                                navController.popBackStack()
                            } else {
                                navController.navigate(Screen.Home) {
                                    popUpTo(Screen.Login) { inclusive = true }
                                }
                            }
                        }
                    },
                    onNavigateToSignUp = {
                        navController.navigate(Screen.SignUp)
                    }
                )
            }
            composable<Screen.SignUp> {
                val authViewModel: AuthViewModel = viewModel {
                    AuthViewModel(authRepository = DependencyContainer.authRepository)
                }
                SignUpScreen(
                    viewModel = authViewModel,
                    onSignUpSuccess = {
                        val targetBooking = pendingBookingScreen
                        if (targetBooking != null) {
                            pendingBookingScreen = null
                            navController.popBackStack()
                            navController.navigate(targetBooking)
                        } else {
                            val previousBackStack = navController.previousBackStackEntry
                            if (previousBackStack != null && previousBackStack.destination.route?.contains("SignUp") == false && previousBackStack.destination.route?.contains("Onboarding") == false) {
                                navController.popBackStack()
                            } else {
                                navController.navigate(Screen.Home) {
                                    popUpTo(Screen.SignUp) { inclusive = true }
                                }
                            }
                        }
                    },
                    onNavigateToLogin = {
                        navController.popBackStack()
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
                    onNavigateToServices = { category ->
                        navController.navigate(Screen.Services(initialCategory = category))
                    },
                    onNavigateToAddresses = {
                        navController.navigate(Screen.AddressManagement)
                    },
                    onNavigateToOffers = { navController.navigate(Screen.Offers) },
                    onNavigateToProducts = { navController.navigate(Screen.Products) },
                    onNavigateToProfile = { navController.navigate(Screen.Profile) },
                    onNavigateToLoyalty = { navController.navigate(Screen.Loyalty) },
                    onNavigateToBridal = { navController.navigate(Screen.Bridal) },
                    onNavigateToBeauty = { navController.navigate(Screen.Beauty) },
                    onNavigateToHomeService = { navController.navigate(Screen.HomeService) },
                    onNavigateToNotifications = { navController.navigate(Screen.Notifications) },
                    onNavigateToBookingHistory = { navController.navigate(Screen.BookingHistory) },
                    onNavigateToBooking = { serviceId ->
                        navigateToBooking(Screen.Booking(serviceId = serviceId))
                    },
                    onNavigateToGenderSelection = {
                        navController.navigate(Screen.GenderSelection)
                    }
                )
            }
            composable<Screen.Services> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.Services>()
                ServicesCatalogScreen(
                    initialCategory = route.initialCategory,
                    onFindSalons = { serviceIds ->
                        navController.navigate(Screen.Discovery(serviceIds = serviceIds))
                    },
                    onNavigateToHome = { navController.navigate(Screen.Home) },
                    onNavigateToBookings = { navController.navigate(Screen.BookingHistory) },
                    onNavigateToOffers = { navController.navigate(Screen.Offers) },
                    onNavigateToWallet = { navController.navigate(Screen.Loyalty) },
                    onNavigateToProfile = { navController.navigate(Screen.Profile) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.GenderSelection> {
                GenderSelectionScreen(
                    onNavigateToGenderServices = { gender ->
                        navController.navigate(Screen.GenderServices(gender = gender))
                    },
                    onNavigateToDiscovery = { categoryId ->
                        navController.navigate(Screen.Discovery(categoryId = categoryId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.GenderServices> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.GenderServices>()
                GenderServicesScreen(
                    gender = route.gender,
                    onNavigateToDiscovery = { categoryId ->
                        navController.navigate(Screen.Discovery(categoryId = categoryId))
                    },
                    onNavigateToBooking = { serviceId ->
                        navigateToBooking(Screen.Booking(serviceId = serviceId))
                    },
                    onBack = { navController.popBackStack() }
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
                    serviceIds = route.serviceIds,
                    viewModel = discoveryViewModel,
                    onNavigateToShop = { shopId ->
                        if (route.serviceIds.isNotEmpty()) {
                            navigateToBooking(
                                Screen.Booking(
                                    serviceIds = route.serviceIds,
                                    shopId = shopId
                                )
                            )
                        } else {
                            navController.navigate(Screen.ShopDetails(shopId))
                        }
                    },
                    onNavigateToHome = { navController.navigate(Screen.Home) },
                    onNavigateToBookings = { navController.navigate(Screen.BookingHistory) },
                    onNavigateToOffers = { navController.navigate(Screen.Offers) },
                    onNavigateToWallet = { navController.navigate(Screen.Loyalty) },
                    onNavigateToProfile = { navController.navigate(Screen.Profile) },
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
                        navigateToBooking(Screen.Booking(serviceId = serviceId))
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
                        navigateToBooking(Screen.Booking(serviceId = serviceId))
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
                BookingScreen(
                    serviceId = route.serviceId,
                    packageId = route.packageId,
                    serviceIds = route.serviceIds,
                    shopId = route.shopId,
                    viewModel = bookingViewModel,
                    onNavigateToAddresses = { navController.navigate(Screen.AddressManagement) },
                    onNavigateToHistory = { navController.navigate(Screen.BookingHistory) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.BookingHistory> {
                val bookingHistoryViewModel: BookingHistoryViewModel = viewModel {
                    BookingHistoryViewModel(
                        bookingRepository = DependencyContainer.bookingRepository
                    )
                }
                BookingHistoryScreen(
                    viewModel = bookingHistoryViewModel,
                    onNavigateToReview = { targetId ->
                        navController.navigate(Screen.Reviews(targetId = targetId, type = ReviewTargetType.SHOP.name))
                    },
                    onNavigateToRebook = { serviceId ->
                        navController.navigate(Screen.Booking(serviceId = serviceId))
                    },
                    onNavigateToHome = { navController.navigate(Screen.Home) },
                    onNavigateToOffers = { navController.navigate(Screen.Offers) },
                    onNavigateToWallet = { navController.navigate(Screen.Loyalty) },
                    onNavigateToProfile = { navController.navigate(Screen.Profile) },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.Offers> {
                val offersViewModel: OffersViewModel = viewModel {
                    OffersViewModel(
                        offersRepository = DependencyContainer.offersRepository
                    )
                }
                OffersScreen(
                    viewModel = offersViewModel,
                    onNavigateToHome = { navController.navigate(Screen.Home) },
                    onNavigateToBookings = { navController.navigate(Screen.BookingHistory) },
                    onNavigateToOffers = {},
                    onNavigateToWallet = { navController.navigate(Screen.Loyalty) },
                    onNavigateToProfile = { navController.navigate(Screen.Profile) },
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
                    onNavigateToHome = { navController.navigate(Screen.Home) },
                    onNavigateToBookings = { navController.navigate(Screen.BookingHistory) },
                    onNavigateToOffers = { navController.navigate(Screen.Offers) },
                    onNavigateToWallet = {},
                    onNavigateToProfile = { navController.navigate(Screen.Profile) },
                    onNavigateToPayment = { planId, price ->
                        navController.navigate(Screen.Payment(planId, price))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.Payment> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.Payment>()
                PaymentScreen(
                    planId = route.planId,
                    amount = route.amount,
                    address = route.address,
                    onPaymentComplete = {
                        navController.navigate(Screen.PaymentSuccess) {
                            popUpTo(Screen.Home) { inclusive = false }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.PaymentSuccess> {
                PaymentSuccessScreen(
                    onDone = {
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.Home) { inclusive = true }
                        }
                    }
                )
            }
            composable<Screen.ProductCheckout> {
                val productViewModel: ProductViewModel = viewModel {
                    ProductViewModel(
                        productRepository = DependencyContainer.productRepository
                    )
                }
                ProductCheckoutScreen(
                    viewModel = productViewModel,
                    onNavigateToPayment = { amount, address ->
                        navController.navigate(Screen.Payment(planId = "Product Order", amount = amount, address = address))
                    },
                    onAddNewAddress = {
                        navController.navigate(Screen.AddressManagement)
                    },
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
                    onNavigateToBookingHistory = { navController.navigate(Screen.BookingHistory) },
                    onNavigateToAddresses = { navController.navigate(Screen.AddressManagement) },
                    onNavigateToFavorites = { navController.navigate(Screen.Favorites) },
                    onNavigateToSupport = { navController.navigate(Screen.Support) },
                    onNavigateToEditProfile = { navController.navigate(Screen.EditProfile) },
                    onNavigateToHome = { navController.navigate(Screen.Home) },
                    onNavigateToOffers = { navController.navigate(Screen.Offers) },
                    onNavigateToWallet = { navController.navigate(Screen.Loyalty) },
                    onLogout = {
                        navController.navigate(Screen.Login) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.EditProfile> {
                val profileViewModel: ProfileViewModel = viewModel {
                    ProfileViewModel(
                        authRepository = DependencyContainer.authRepository
                    )
                }
                EditProfileScreen(
                    viewModel = profileViewModel,
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
                    onNavigateToCheckout = { navController.navigate(Screen.ProductCheckout) },
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
                        navigateToBooking(Screen.Booking(packageId = packageId))
                    },
                    onBack = { navController.popBackStack() }
                )
            }
            composable<Screen.Beauty> {
                val beautyViewModel: BeautyViewModel = viewModel {
                    BeautyViewModel(
                        beautyRepository = DependencyContainer.beautyRepository
                    )
                }
                BeautyScreen(
                    viewModel = beautyViewModel,
                    onNavigateToBooking = { serviceId ->
                        navigateToBooking(Screen.Booking(serviceId = serviceId))
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
                    onNavigateToBooking = { packageId ->
                        navigateToBooking(Screen.Booking(packageId = packageId))
                    },
                    onBack = { navController.popBackStack() }
                )

            }
            composable<Screen.Support> {
                val supportViewModel: SupportViewModel = viewModel {
                    SupportViewModel(
                        supportRepository = DependencyContainer.supportRepository
                    )
                }
                SupportScreen(
                    viewModel = supportViewModel,
                    onBack = { navController.popBackStack() }
                )
            }
        }

        if (showFlexibleUpdateDialog && updateStatus is UpdateStatus.FlexibleUpdateAvailable) {
            FlexibleUpdateBottomSheet(
                updateInfo = updateStatus,
                onDismiss = { showFlexibleUpdateDialog = false }
            )
        }
    }
}
}
}
}




