package com.groomora.core.api

/**
 * Centralized API endpoints for the Groomora backend.
 * When the backend is ready, update [BASE_URL] or configure per environment.
 */
object ApiEndpoints {

    /** Default API Base URL (Change to your live backend domain) */
    var BASE_URL: String = "https://api.groomora.com"

    /** Toggle between Mock Data and Live Remote Network Calls */
    var USE_MOCK_API: Boolean = true

    // ==========================================
    // 1. AUTHENTICATION & PROFILE
    // ==========================================
    object Auth {
        const val SEND_OTP = "/api/v1/auth/send-otp"
        const val VERIFY_OTP = "/api/v1/auth/verify-otp"
        const val LOGIN_PASSWORD = "/api/v1/auth/login-password"
        const val PROFILE = "/api/v1/auth/profile"
        const val UPDATE_PROFILE = "/api/v1/auth/profile/update"
        const val LOGOUT = "/api/v1/auth/logout"
    }

    // ==========================================
    // 2. HOME & DISCOVERY
    // ==========================================
    object Home {
        const val FEED = "/api/v1/home/feed"
        const val BANNERS = "/api/v1/home/banners"
        const val TRUST_BADGES = "/api/v1/home/trust-badges"
        const val WORK_GALLERY = "/api/v1/home/work-gallery"
    }

    object Discovery {
        const val SEARCH = "/api/v1/discovery/search"
        const val NEARBY_SHOPS = "/api/v1/discovery/nearby"
        const val CATEGORIES = "/api/v1/discovery/categories"
        const val GENDER_SERVICES = "/api/v1/discovery/gender-services"
        const val PROFESSIONAL_PROFILE = "/api/v1/discovery/professionals"
    }

    // ==========================================
    // 3. SHOPS & SALONS
    // ==========================================
    object Shops {
        const val DETAILS = "/api/v1/shops"
        const val SERVICES = "/api/v1/shops/{id}/services"
        const val STYLISTS = "/api/v1/shops/{id}/stylists"
        const val PACKAGES = "/api/v1/shops/{id}/packages"
        const val GALLERY = "/api/v1/shops/{id}/gallery"
        const val REVIEWS = "/api/v1/shops/{id}/reviews"
    }

    // ==========================================
    // 4. BOOKINGS & APPOINTMENTS
    // ==========================================
    object Booking {
        const val AVAILABILITY_SLOTS = "/api/v1/bookings/availability"
        const val CREATE_BOOKING = "/api/v1/bookings/create"
        const val BOOKING_HISTORY = "/api/v1/bookings/history"
        const val BOOKING_DETAILS = "/api/v1/bookings/{id}"
        const val RESCHEDULE = "/api/v1/bookings/{id}/reschedule"
        const val CANCEL = "/api/v1/bookings/{id}/cancel"
    }

    // ==========================================
    // 5. OFFERS & COUPONS
    // ==========================================
    object Offers {
        const val LIST = "/api/v1/offers"
        const val VALIDATE_COUPON = "/api/v1/offers/validate-coupon"
    }

    // ==========================================
    // 6. LOYALTY & REWARDS
    // ==========================================
    object Loyalty {
        const val SUMMARY = "/api/v1/loyalty/summary"
        const val TRANSACTIONS = "/api/v1/loyalty/transactions"
        const val REDEEM = "/api/v1/loyalty/redeem"
    }

    // ==========================================
    // 7. PRODUCTS & ORDERS (E-COMMERCE)
    // ==========================================
    object Products {
        const val CATALOG = "/api/v1/products"
        const val DETAILS = "/api/v1/products/{id}"
        const val CATEGORIES = "/api/v1/products/categories"
    }

    object Orders {
        const val LIST = "/api/v1/orders"
        const val CREATE = "/api/v1/orders/create"
        const val DETAILS = "/api/v1/orders/{id}"
        const val CANCEL = "/api/v1/orders/{id}/cancel"
        const val TRACKING = "/api/v1/orders/{id}/tracking"
    }

    // ==========================================
    // 8. SPECIAL & HOME SERVICES
    // ==========================================
    object SpecialServices {
        const val BRIDAL_PACKAGES = "/api/v1/bridal/packages"
        const val BEAUTY_SERVICES = "/api/v1/beauty/services"
        const val HOME_SERVICES = "/api/v1/homeservice/categories"
        const val HOME_SERVICE_BOOKING = "/api/v1/homeservice/book"
    }

    // ==========================================
    // 9. REVIEWS & RATINGS
    // ==========================================
    object Reviews {
        const val LIST = "/api/v1/reviews"
        const val SUBMIT = "/api/v1/reviews/create"
    }

    // ==========================================
    // 10. NOTIFICATIONS
    // ==========================================
    object Notifications {
        const val LIST = "/api/v1/notifications"
        const val MARK_READ = "/api/v1/notifications/{id}/read"
        const val MARK_ALL_READ = "/api/v1/notifications/read-all"
    }

    // ==========================================
    // 11. FAVORITES
    // ==========================================
    object Favorites {
        const val LIST = "/api/v1/favorites"
        const val TOGGLE = "/api/v1/favorites/toggle"
    }

    // ==========================================
    // 12. CUSTOMER SUPPORT
    // ==========================================
    object Support {
        const val FAQS = "/api/v1/support/faqs"
        const val TICKETS = "/api/v1/support/tickets"
        const val CREATE_TICKET = "/api/v1/support/tickets/create"
    }

    // ==========================================
    // 13. REMOTE CONFIG & APP UPDATES
    // ==========================================
    object Config {
        const val REMOTE_CONFIG = "/api/v1/config/remote-config"
        const val VERSION_CHECK = "/api/v1/config/version-check"
    }
}
