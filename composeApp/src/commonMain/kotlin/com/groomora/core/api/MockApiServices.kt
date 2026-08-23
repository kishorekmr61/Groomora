package com.groomora.core.api

import com.groomora.feature.auth.User
import com.groomora.feature.auth.UserGender
import com.groomora.feature.beauty.BeautyCategory
import com.groomora.feature.booking.BookingRecord
import com.groomora.feature.booking.BookingStatus
import com.groomora.feature.booking.PaymentMethodType
import com.groomora.feature.booking.RefundStatus
import com.groomora.feature.bridal.BridalPackage
import com.groomora.feature.discovery.Professional
import com.groomora.feature.discovery.Shop
import com.groomora.feature.homeservice.HomeServiceDetail
import com.groomora.feature.loyalty.LoyaltyProfile
import com.groomora.feature.loyalty.LoyaltyTransaction
import com.groomora.feature.loyalty.TransactionType
import com.groomora.feature.notifications.Notification
import com.groomora.feature.notifications.NotificationType
import com.groomora.feature.offers.Offer
import com.groomora.feature.offers.OfferType
import com.groomora.feature.products.Order
import com.groomora.feature.products.OrderItem
import com.groomora.feature.products.OrderStatus
import com.groomora.feature.products.Product
import com.groomora.feature.reviews.Review
import com.groomora.feature.shop.Service
import com.groomora.feature.shop.ServicePackage
import kotlinx.coroutines.delay

class MockAuthApiService : AuthApiService {
    override suspend fun sendOtp(request: SendOtpRequest): ApiResponse<String> {
        delay(200)
        return ApiResponse(success = true, data = "1234", message = "OTP sent successfully to +91 ${request.phoneNumber}")
    }

    override suspend fun verifyOtp(request: VerifyOtpRequest): ApiResponse<AuthResponseData> {
        delay(300)
        return ApiResponse(
            success = true,
            data = AuthResponseData(
                token = "mock_jwt_token_${request.phoneNumber}",
                userId = "usr_101",
                name = "Groomora User",
                phoneNumber = request.phoneNumber,
                email = "user@groomora.com"
            )
        )
    }

    override suspend fun loginWithPassword(request: LoginPasswordRequest): ApiResponse<AuthResponseData> {
        delay(300)
        return ApiResponse(
            success = true,
            data = AuthResponseData(
                token = "mock_jwt_token_${request.phoneNumber}",
                userId = "usr_101",
                name = "Groomora User",
                phoneNumber = request.phoneNumber,
                email = "user@groomora.com"
            )
        )
    }

    override suspend fun signUp(request: SignUpRequest): ApiResponse<AuthResponseData> {
        delay(350)
        return ApiResponse(
            success = true,
            data = AuthResponseData(
                token = "mock_jwt_token_${request.phoneNumber}",
                userId = "usr_${(1000..9999).random()}",
                name = request.name,
                phoneNumber = request.phoneNumber,
                email = request.email
            ),
            message = "Account created successfully!"
        )
    }


    override suspend fun getProfile(): ApiResponse<User> {
        delay(150)
        return ApiResponse(
            success = true,
            data = User(
                id = "usr_101",
                name = "Groomora User",
                phoneNumber = "9876543210",
                email = "user@groomora.com",
                gender = UserGender.MALE
            )
        )
    }

    override suspend fun updateProfile(user: User): ApiResponse<User> {
        delay(200)
        return ApiResponse(success = true, data = user, message = "Profile updated successfully")
    }

    override suspend fun logout(): ApiResponse<Unit> {
        delay(100)
        return ApiResponse(success = true, data = Unit, message = "Logged out")
    }
}

class MockDiscoveryApiService : DiscoveryApiService {
    private val sampleShops = listOf(
        Shop(
            id = "s1",
            name = "King's Barber Studio",
            rating = 4.8,
            reviewCount = 245,
            address = "Koramangala 4th Block, Bengaluru",
            distance = "0.8 km",
            imageUrl = "https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=600&q=80"
        ),
        Shop(
            id = "s2",
            name = "Bella Beauty Parlour",
            rating = 4.7,
            reviewCount = 198,
            address = "Koramangala 5th Block, Bengaluru",
            distance = "1.2 km",
            imageUrl = "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=600&q=80"
        ),
        Shop(
            id = "s3",
            name = "Urban Fade Barber Lounge",
            rating = 4.9,
            reviewCount = 312,
            address = "Indiranagar 100ft Rd, Bengaluru",
            distance = "2.4 km",
            imageUrl = "https://images.unsplash.com/photo-1503951914875-452162b0f3f1?w=600&q=80"
        )
    )

    override suspend fun getNearbyShops(latitude: Double, longitude: Double, categoryId: String?): ApiResponse<List<Shop>> {
        delay(250)
        return ApiResponse(success = true, data = sampleShops)
    }

    override suspend fun searchShopsAndServices(query: String): ApiResponse<List<Shop>> {
        delay(200)
        val filtered = sampleShops.filter { it.name.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true) }
        return ApiResponse(success = true, data = filtered)
    }

    override suspend fun getProfessionalProfile(professionalId: String): ApiResponse<Professional> {
        delay(200)
        return ApiResponse(
            success = true,
            data = Professional(
                id = professionalId,
                name = "Alex Rivera",
                role = "Master Barber",
                rating = 4.9,
                imageUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=400&q=80",
                specialization = listOf("Fade Specialist", "Beard Sculpting", "Hot Towel Shave")
            )
        )
    }
}

class MockShopApiService : ShopApiService {
    override suspend fun getShopDetails(shopId: String): ApiResponse<Shop> {
        delay(200)
        return ApiResponse(
            success = true,
            data = Shop(
                id = shopId,
                name = "King's Barber Studio",
                rating = 4.8,
                reviewCount = 245,
                address = "Koramangala 4th Block, Bengaluru",
                distance = "0.8 km",
                imageUrl = "https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=600&q=80"
            )
        )
    }

    override suspend fun getShopServices(shopId: String): ApiResponse<List<Service>> {
        delay(200)
        return ApiResponse(
            success = true,
            data = listOf(
                Service("ser1", "Haircut & Styling", 299.0, "30 min", "Master cut with hair wash and blow dry", "hair"),
                Service("ser2", "Beard Trim & Shape", 199.0, "25 min", "Razor sharp detailing with beard oil massage", "beard"),
                Service("ser3", "Deep Scalp Hair Spa", 599.0, "50 min", "Anti-dandruff and nourishing scalp treatment", "hair"),
                Service("ser4", "Detan Face Clean Up", 499.0, "40 min", "Brightening facial cleanse and pore extraction", "skin")
            )
        )
    }

    override suspend fun getShopPackages(shopId: String): ApiResponse<List<ServicePackage>> {
        delay(150)
        return ApiResponse(
            success = true,
            data = listOf(
                ServicePackage(
                    id = "pkg1",
                    name = "Gentleman Grooming Combo",
                    description = "Haircut + Beard Trim + Detan Clean Up + Hair Spa",
                    price = 999.0,
                    originalPrice = 1596.0,
                    services = listOf("Haircut", "Beard Trim", "Face Clean Up", "Hair Spa")
                )
            )
        )
    }

    override suspend fun getShopStylists(shopId: String): ApiResponse<List<Professional>> {
        delay(150)
        return ApiResponse(
            success = true,
            data = listOf(
                Professional("pro1", "Arjun", "Senior Stylist", 4.8, "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&q=80", listOf("Hair Spa", "Fade Cut")),
                Professional("pro2", "Rahul", "Master Stylist", 4.7, "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&q=80", listOf("Beard Grooming", "Keratin")),
                Professional("pro3", "Vikram", "Expert Stylist", 4.9, "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=400&q=80", listOf("Facials", "Styling"))
            )
        )
    }
}

class MockBookingApiService : BookingApiService {
    private val bookings = mutableListOf(
        BookingRecord(
            id = "BK-8021",
            title = "Haircut & Styling",
            shopName = "King's Barber Studio",
            date = "Oct 25, 2024",
            time = "10:30 AM",
            status = BookingStatus.CONFIRMED,
            totalAmount = 299.0,
            canReschedule = true,
            canCancel = true,
            canReview = false,
            canRebook = true,
            professionalName = "Arjun",
            isHomeService = false,
            paymentMethod = PaymentMethodType.UPI
        ),
        BookingRecord(
            id = "BK-7890",
            title = "Gentleman Grooming Combo",
            shopName = "King's Barber Studio",
            date = "Sep 15, 2024",
            time = "02:00 PM",
            status = BookingStatus.COMPLETED,
            totalAmount = 999.0,
            canReschedule = false,
            canCancel = false,
            canReview = true,
            canRebook = true,
            professionalName = "Alex Rivera",
            isHomeService = false,
            paymentMethod = PaymentMethodType.CREDIT_DEBIT_CARD
        )
    )

    override suspend fun getAvailableTimeSlots(date: String, professionalId: String?): ApiResponse<List<String>> {
        delay(150)
        return ApiResponse(
            success = true,
            data = listOf("09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "02:00 PM", "03:00 PM", "04:30 PM", "06:00 PM")
        )
    }

    override suspend fun createBooking(request: CreateBookingRequest): ApiResponse<BookingRecord> {
        delay(400)
        val newBooking = BookingRecord(
            id = "BK-${(1000..9999).random()}",
            title = "Haircut & Styling",
            shopName = "King's Barber Studio",
            date = request.date,
            time = request.timeSlot,
            status = BookingStatus.CONFIRMED,
            totalAmount = 299.0,
            canReschedule = true,
            canCancel = true,
            canReview = false,
            canRebook = true,
            professionalName = "Arjun",
            isHomeService = false,
            paymentMethod = PaymentMethodType.UPI
        )
        bookings.add(0, newBooking)
        return ApiResponse(success = true, data = newBooking, message = "Appointment booked successfully!")
    }

    override suspend fun getBookingHistory(): ApiResponse<List<BookingRecord>> {
        delay(200)
        return ApiResponse(success = true, data = bookings.toList())
    }

    override suspend fun rescheduleBooking(request: RescheduleBookingRequest): ApiResponse<BookingRecord> {
        delay(300)
        val index = bookings.indexOfFirst { it.id == request.bookingId }
        val updated = if (index >= 0) {
            val item = bookings[index].copy(date = request.newDate, time = request.newTimeSlot)
            bookings[index] = item
            item
        } else {
            BookingRecord(
                id = request.bookingId,
                title = "Haircut",
                shopName = "King's Barber Studio",
                professionalName = null,
                date = request.newDate,
                time = request.newTimeSlot,
                isHomeService = false,
                totalAmount = 299.0,
                paymentMethod = PaymentMethodType.UPI,
                status = BookingStatus.CONFIRMED
            )
        }
        return ApiResponse(success = true, data = updated, message = "Rescheduled successfully")
    }

    override suspend fun cancelBooking(request: CancelBookingRequest): ApiResponse<BookingRecord> {
        delay(300)
        val index = bookings.indexOfFirst { it.id == request.bookingId }
        val updated = if (index >= 0) {
            val item = bookings[index].copy(
                status = BookingStatus.CANCELLED,
                refundStatus = RefundStatus.REFUNDED,
                cancellationReason = request.reason
            )
            bookings[index] = item
            item
        } else {
            BookingRecord(
                id = request.bookingId,
                title = "Haircut",
                shopName = "King's Barber Studio",
                professionalName = null,
                date = "Oct 25, 2024",
                time = "10:30 AM",
                isHomeService = false,
                status = BookingStatus.CANCELLED,
                totalAmount = 299.0,
                paymentMethod = PaymentMethodType.UPI,
                refundStatus = RefundStatus.REFUNDED,
                cancellationReason = request.reason
            )
        }
        return ApiResponse(success = true, data = updated, message = "Booking cancelled and 100% refund initiated")
    }
}

class MockOffersApiService : OffersApiService {
    override suspend fun getActiveOffers(): ApiResponse<List<Offer>> {
        delay(150)
        return ApiResponse(
            success = true,
            data = listOf(
                Offer("off1", "FIRST50", "Flat 50% Off on First Booking", "Valid on all services", 0.50, OfferType.PERCENTAGE, "2024-12-31", 0.0, 200.0),
                Offer("off2", "GROOM100", "Flat ₹100 Off on Combos", "Min order ₹499", 100.0, OfferType.FLAT, "2024-12-31", 499.0, 100.0),
                Offer("off3", "FESTIVE20", "20% Festive Cashback", "Valid across all top salons", 0.20, OfferType.PERCENTAGE, "2024-12-31", 0.0, 300.0)
            )
        )
    }

    override suspend fun validateCoupon(request: ValidateCouponRequest): ApiResponse<ValidateCouponResponseData> {
        delay(200)
        val code = request.couponCode.uppercase()
        return if (code == "FIRST50" || code == "GROOM100" || code == "FESTIVE20") {
            val discount = if (code == "FIRST50") (request.cartAmount * 0.5).coerceAtMost(200.0) else 100.0
            ApiResponse(
                success = true,
                data = ValidateCouponResponseData(
                    isValid = true,
                    code = code,
                    discountAmount = discount,
                    finalAmount = request.cartAmount - discount,
                    message = "Coupon applied! You saved ₹${discount.toInt()}"
                )
            )
        } else {
            ApiResponse(
                success = false,
                data = ValidateCouponResponseData(
                    isValid = false,
                    code = code,
                    discountAmount = 0.0,
                    finalAmount = request.cartAmount,
                    message = "Invalid or expired coupon code"
                ),
                errorCode = "INVALID_COUPON"
            )
        }
    }
}

class MockLoyaltyApiService : LoyaltyApiService {
    private var profile = LoyaltyProfile(
        pointsBalance = 450,
        memberLevel = "Silver",
        pointsToNextLevel = 50,
        referralCode = "GROOM450"
    )

    private val transactions = mutableListOf(
        LoyaltyTransaction("tx1", TransactionType.EARNED, 50, "2 days ago", "Booking at King's Barber"),
        LoyaltyTransaction("tx2", TransactionType.REFERRAL_BONUS, 100, "1 week ago", "Referral bonus")
    )

    override suspend fun getLoyaltyProfile(): ApiResponse<LoyaltyProfile> {
        delay(150)
        return ApiResponse(success = true, data = profile)
    }

    override suspend fun getLoyaltyTransactions(): ApiResponse<List<LoyaltyTransaction>> {
        delay(150)
        return ApiResponse(success = true, data = transactions.toList())
    }

    override suspend fun redeemPoints(points: Int): ApiResponse<LoyaltyProfile> {
        delay(200)
        profile = profile.copy(pointsBalance = profile.pointsBalance - points)
        return ApiResponse(success = true, data = profile, message = "Redeemed $points Groomora points!")
    }
}

class MockProductApiService : ProductApiService {
    private val products = listOf(
        Product("p1", "Matte Hair Styling Clay", "Groomora", 399.0, 499.0, "https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?w=400&q=80", "Strong hold matte finish clay for all hair types", "Hair"),
        Product("p2", "Organic Beard Growth Oil", "Groomora", 499.0, 699.0, "https://images.unsplash.com/photo-1621605815971-fbc98d665033?w=400&q=80", "Infused with Redensyl, Vitamin E, and Argan oil", "Beard"),
        Product("p3", "Activated Charcoal Face Scrub", "Groomora", 299.0, 399.0, "https://images.unsplash.com/photo-1512290900672-1f02e71d4793?w=400&q=80", "Deep pore detox and blackhead removal scrub", "Skin")
    )

    private val orders = mutableListOf(
        Order(
            id = "ORD-4921",
            items = listOf(OrderItem("p1", "Matte Hair Styling Clay", 1, 399.0)),
            totalAmount = 399.0,
            status = OrderStatus.SHIPPED,
            date = "Oct 20, 2024",
            deliveryAddress = "Flat 402, Green Glen Layout, Bellandur, Bengaluru",
            paymentMethod = "Credit Card",
            trackingNumber = "BLR-EXP-992384"
        )
    )

    override suspend fun getProducts(categoryId: String?): ApiResponse<List<Product>> {
        delay(200)
        return ApiResponse(success = true, data = products)
    }

    override suspend fun getProductDetails(productId: String): ApiResponse<Product> {
        delay(150)
        val product = products.find { it.id == productId } ?: products.first()
        return ApiResponse(success = true, data = product)
    }

    override suspend fun getOrders(): ApiResponse<List<Order>> {
        delay(200)
        return ApiResponse(success = true, data = orders.toList())
    }

    override suspend fun cancelOrder(orderId: String, reason: String): ApiResponse<Order> {
        delay(250)
        val index = orders.indexOfFirst { it.id == orderId }
        val updated = if (index >= 0) {
            val item = orders[index].copy(status = OrderStatus.CANCELLED, cancellationReason = reason)
            orders[index] = item
            item
        } else {
            orders.first()
        }
        return ApiResponse(success = true, data = updated, message = "Order cancelled")
    }
}

class MockSpecialServicesApiService : SpecialServicesApiService {
    override suspend fun getBridalPackages(): ApiResponse<List<BridalPackage>> {
        delay(200)
        return ApiResponse(
            success = true,
            data = listOf(
                BridalPackage("bp1", "Royal Bridal Glam Package", "Full HD Bridal Makeup, Hair Styling, Saree Draping & Pre-bridal glow kit", 14999.0, listOf("HD Makeup", "Hair Styling", "Saree Draping"), "https://images.unsplash.com/photo-1610030469983-98e550d6193c?w=800&q=80"),
                BridalPackage("bp2", "Engagement & Reception Look", "Party HD Makeup with hair setting and jewelry assistance", 7999.0, listOf("HD Makeup", "Hair Setting"), "https://images.unsplash.com/photo-1583939003579-730e3918a45a?w=800&q=80")
            )
        )
    }

    override suspend fun getBeautyCategories(): ApiResponse<List<BeautyCategory>> {
        delay(150)
        return ApiResponse(
            success = true,
            data = listOf(
                BeautyCategory("cat_facials", "Signature Facials", "icon_facial", "Revitalizing and hydrating facial care"),
                BeautyCategory("cat_mani_pedi", "Mani-Pedi Spa", "icon_mani_pedi", "Relaxing foot and hand care therapies")
            )
        )
    }

    override suspend fun getHomeServiceDetails(addressId: String): ApiResponse<HomeServiceDetail> {
        delay(150)
        return ApiResponse(
            success = true,
            data = HomeServiceDetail(
                estimatedArrivalTime = "35-45 mins",
                hygieneProtocols = listOf("Single-use sterilized kits", "Vaccinated & background-checked stylists", "Post-service cleanup"),
                safetyMeasures = listOf("Contactless OTP verification", "Real-time stylist GPS tracking"),
                travelFee = 50.0,
                serviceRadiusKm = 10.0
            )
        )
    }
}

class MockReviewApiService : ReviewApiService {
    private val reviews = mutableListOf(
        Review("r1", "usr_201", "Rohit Sharma", null, 5, "Best fade haircut I've gotten in Koramangala. Master barber Alex took real care.", "2 days ago", null, isVerified = true),
        Review("r2", "usr_202", "Priya Nair", null, 5, "Excellent hair spa and facial session. Very clean and hygienic environment.", "1 week ago", null, isVerified = true),
        Review("r3", "usr_203", "Karthik V.", null, 4, "Great service, on time, and very polite stylists. Highly recommended!", "2 weeks ago", null, isVerified = true)
    )

    override suspend fun getReviews(targetId: String, type: String): ApiResponse<List<Review>> {
        delay(150)
        return ApiResponse(success = true, data = reviews.toList())
    }

    override suspend fun submitReview(request: SubmitReviewRequest): ApiResponse<Review> {
        delay(250)
        val newReview = Review(
            id = "r${reviews.size + 1}",
            userId = "usr_101",
            userName = "You",
            userImageUrl = null,
            rating = request.rating,
            comment = request.comment,
            date = "Just now",
            isVerified = true
        )
        reviews.add(0, newReview)
        return ApiResponse(success = true, data = newReview, message = "Thank you for your review!")
    }
}

class MockNotificationApiService : NotificationApiService {
    private val notifications = mutableListOf(
        Notification("n1", "Booking Confirmed! 🎉", "Your haircut at King's Barber Studio is confirmed for Oct 25, 10:30 AM.", "10 min ago", isRead = false, type = NotificationType.BOOKING),
        Notification("n2", "Festive 50% Off Offer", "Use code FIRST50 on your next booking and save up to ₹200.", "2 hours ago", isRead = false, type = NotificationType.OFFER)
    )

    override suspend fun getNotifications(): ApiResponse<List<Notification>> {
        delay(150)
        return ApiResponse(success = true, data = notifications.toList())
    }

    override suspend fun markAsRead(notificationId: String): ApiResponse<Unit> {
        val index = notifications.indexOfFirst { it.id == notificationId }
        if (index >= 0) {
            notifications[index] = notifications[index].copy(isRead = true)
        }
        return ApiResponse(success = true, data = Unit)
    }

    override suspend fun markAllAsRead(): ApiResponse<Unit> {
        for (i in notifications.indices) {
            notifications[i] = notifications[i].copy(isRead = true)
        }
        return ApiResponse(success = true, data = Unit)
    }
}

class MockFavoritesApiService : FavoritesApiService {
    private val favoriteShops = mutableListOf(
        Shop(
            id = "s1",
            name = "King's Barber Studio",
            rating = 4.8,
            reviewCount = 245,
            address = "Koramangala 4th Block, Bengaluru",
            distance = "0.8 km",
            imageUrl = "https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=600&q=80"
        )
    )

    override suspend fun getFavoriteShops(): ApiResponse<List<Shop>> {
        delay(150)
        return ApiResponse(success = true, data = favoriteShops.toList())
    }

    override suspend fun toggleFavoriteShop(shopId: String): ApiResponse<Boolean> {
        delay(150)
        val exists = favoriteShops.any { it.id == shopId }
        if (exists) {
            favoriteShops.removeAll { it.id == shopId }
            return ApiResponse(success = true, data = false, message = "Removed from favorites")
        } else {
            favoriteShops.add(
                Shop(
                    id = shopId,
                    name = "King's Barber Studio",
                    rating = 4.8,
                    reviewCount = 245,
                    address = "Koramangala 4th Block",
                    distance = "0.8 km",
                    imageUrl = "https://images.unsplash.com/photo-1585747860715-2ba37e788b70?w=600&q=80"
                )
            )
            return ApiResponse(success = true, data = true, message = "Added to favorites")
        }
    }
}
