package com.groomora.core.api

import com.groomora.feature.auth.User
import com.groomora.feature.beauty.BeautyCategory
import com.groomora.feature.booking.BookingRecord
import com.groomora.feature.bridal.BridalPackage
import com.groomora.feature.discovery.Professional
import com.groomora.feature.discovery.Shop
import com.groomora.feature.homeservice.HomeServiceDetail
import com.groomora.feature.loyalty.LoyaltyProfile
import com.groomora.feature.loyalty.LoyaltyTransaction
import com.groomora.feature.notifications.Notification
import com.groomora.feature.offers.Offer
import com.groomora.feature.products.Order
import com.groomora.feature.products.Product
import com.groomora.feature.reviews.Review
import com.groomora.feature.shop.Service
import com.groomora.feature.shop.ServicePackage

// ==========================================
// 1. AUTH API SERVICE
// ==========================================
interface AuthApiService {
    suspend fun sendOtp(request: SendOtpRequest): ApiResponse<String>
    suspend fun verifyOtp(request: VerifyOtpRequest): ApiResponse<AuthResponseData>
    suspend fun loginWithPassword(request: LoginPasswordRequest): ApiResponse<AuthResponseData>
    suspend fun signUp(request: SignUpRequest): ApiResponse<AuthResponseData>
    suspend fun getProfile(): ApiResponse<User>
    suspend fun updateProfile(user: User): ApiResponse<User>
    suspend fun logout(): ApiResponse<Unit>
}


// ==========================================
// 2. DISCOVERY & HOME API SERVICE
// ==========================================
interface DiscoveryApiService {
    suspend fun getNearbyShops(latitude: Double, longitude: Double, categoryId: String? = null): ApiResponse<List<Shop>>
    suspend fun searchShopsAndServices(query: String): ApiResponse<List<Shop>>
    suspend fun getProfessionalProfile(professionalId: String): ApiResponse<Professional>
}

// ==========================================
// 3. SHOP & SALON API SERVICE
// ==========================================
interface ShopApiService {
    suspend fun getShopDetails(shopId: String): ApiResponse<Shop>
    suspend fun getShopServices(shopId: String): ApiResponse<List<Service>>
    suspend fun getShopPackages(shopId: String): ApiResponse<List<ServicePackage>>
    suspend fun getShopStylists(shopId: String): ApiResponse<List<Professional>>
}

// ==========================================
// 4. BOOKING API SERVICE
// ==========================================
interface BookingApiService {
    suspend fun getAvailableTimeSlots(date: String, professionalId: String? = null): ApiResponse<List<String>>
    suspend fun createBooking(request: CreateBookingRequest): ApiResponse<BookingRecord>
    suspend fun getBookingHistory(): ApiResponse<List<BookingRecord>>
    suspend fun rescheduleBooking(request: RescheduleBookingRequest): ApiResponse<BookingRecord>
    suspend fun cancelBooking(request: CancelBookingRequest): ApiResponse<BookingRecord>
}

// ==========================================
// 5. OFFERS & COUPONS API SERVICE
// ==========================================
interface OffersApiService {
    suspend fun getActiveOffers(): ApiResponse<List<Offer>>
    suspend fun validateCoupon(request: ValidateCouponRequest): ApiResponse<ValidateCouponResponseData>
}

// ==========================================
// 6. LOYALTY & REWARDS API SERVICE
// ==========================================
interface LoyaltyApiService {
    suspend fun getLoyaltyProfile(): ApiResponse<LoyaltyProfile>
    suspend fun getLoyaltyTransactions(): ApiResponse<List<LoyaltyTransaction>>
    suspend fun redeemPoints(points: Int): ApiResponse<LoyaltyProfile>
}

// ==========================================
// 7. PRODUCTS & ORDERS API SERVICE
// ==========================================
interface ProductApiService {
    suspend fun getProducts(categoryId: String? = null): ApiResponse<List<Product>>
    suspend fun getProductDetails(productId: String): ApiResponse<Product>
    suspend fun getOrders(): ApiResponse<List<Order>>
    suspend fun cancelOrder(orderId: String, reason: String): ApiResponse<Order>
}

// ==========================================
// 8. SPECIAL & HOME SERVICES API SERVICE
// ==========================================
interface SpecialServicesApiService {
    suspend fun getBridalPackages(): ApiResponse<List<BridalPackage>>
    suspend fun getBeautyCategories(): ApiResponse<List<BeautyCategory>>
    suspend fun getHomeServiceDetails(addressId: String): ApiResponse<HomeServiceDetail>
}

// ==========================================
// 9. REVIEWS API SERVICE
// ==========================================
interface ReviewApiService {
    suspend fun getReviews(targetId: String, type: String): ApiResponse<List<Review>>
    suspend fun submitReview(request: SubmitReviewRequest): ApiResponse<Review>
}

// ==========================================
// 10. NOTIFICATIONS API SERVICE
// ==========================================
interface NotificationApiService {
    suspend fun getNotifications(): ApiResponse<List<Notification>>
    suspend fun markAsRead(notificationId: String): ApiResponse<Unit>
    suspend fun markAllAsRead(): ApiResponse<Unit>
}

// ==========================================
// 11. FAVORITES API SERVICE
// ==========================================
interface FavoritesApiService {
    suspend fun getFavoriteShops(): ApiResponse<List<Shop>>
    suspend fun toggleFavoriteShop(shopId: String): ApiResponse<Boolean>
}
