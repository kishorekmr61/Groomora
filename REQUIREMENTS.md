# Groomora Customer App - Requirements Checklist

## A. HOME / DISCOVERY
- [x] Location shown on home screen
- [x] Current location permission handling (Mocked)
- [x] Manual location selection
- [x] Saved addresses and default address management
- [x] Search (Services, Shops, Professionals, Styles, Products)
- [x] Nearby list view
- [x] Nearby map view (Interactive map pins & bottom shop preview sheet)
- [x] Categories: Hair, Skin, Makeup, Nails, Beard, Grooming, Bridal, Beauty, Home Service
- [x] Gender-specific paths (Men, Women, Unisex)
- [x] Recommendations (Top-rated, Nearby, Trending, Personalized)
- [x] Home-screen promotional banners
- [x] Configurable special-offer announcements (Text, Image, CTA, Schedule)
- [x] Recently viewed / Favorites / Rebook modules ("Due for grooming" & "Book Again")

## B. SHOPS & PROFESSIONALS
- [x] Shop details (Status, Address, Distance, Hours)
- [x] Actions: Call, Directions, Share, Save (Mocked CTAs)
- [x] Services & Packages listings
- [x] Gallery / Portfolio view (Implemented Professional Portfolio)
- [x] Professional listings per shop
- [x] Reviews and Ratings
- [x] Verified shop/professional indicators
- [x] Professional profile (Skills, Portfolio, Availability)

## C. SERVICES & PACKAGES
- [x] Individual service selection
- [x] Configurable packages (Multiple services/quantities)
- [x] Package savings display
- [x] Duration and Gender applicability
- [x] Home-service eligibility for services
- [x] Add-on selection logic
- [x] Package-specific offers and validity rules

## D. BOOKING ENGINE
- [x] Service/Package selection flow
- [x] Add-on selection
- [x] Professional selection (Specific vs. Any Available)
- [x] Real-time availability calendar (Date/Time)
- [x] Location toggle (Shop vs. Home Service)
- [x] Address validation for home services (Mocked)
- [x] Travel time and Travel fee calculation (Mocked)
- [x] Offer/Coupon application (Live validation, discount rules & removal)
- [x] Loyalty points redemption (UI ready, dynamic points calculation in VM)
- [x] Price breakdown (Base + Add-ons + Fee - Discount - Loyalty)
- [x] Payment method selection (UPI, Credit/Debit Card, Net Banking, Pay at Venue)
- [x] Booking confirmation and History (Tracking ID, status, and details)
- [x] Reschedule and Cancellation flow (Modal dialogs & state updates)
- [x] Refund status tracking (Initiated -> Processing -> Refunded)
- [x] One-tap rebooking

## E. SPECIALIZED SERVICES
- [x] Bridal: Makeup, Hair, Saree draping, Pre-bridal packages
- [x] Home Services: Service radius validation, ETA status, Hygiene messaging
- [x] Beauty Parlour: Waxing, Facials, Treatments, Nail Art, Hair Spa & Curated Bundles

## F. COMMERCE (PRODUCTS)
- [x] Product catalog and Search/Filter
- [x] Product details and Gallery
- [x] Cart management
- [x] Checkout flow (Address, Payment method, Price Breakdown, Place Order)
- [x] Order history and Shipping tracking (Tracking ID, Status badges, and Cancellation flow with refunds)

## G. OFFERS & PROMOTIONS
- [x] Offer types: First-booking, Shop-specific, Seasonal, Bridal, Referral
- [x] Minimum order value and Maximum discount logic
- [x] Usage limits and Stacking rules
- [x] Location-based offer targeting

## H. LOYALTY, REFERRAL & MEMBERSHIP
- [x] Points balance and Transaction history
- [x] Earn and Redeem rules
- [x] Referral code sharing and Reward tracking
- [x] Membership plans, Benefits, and Renewal status

## I. RETENTION & REVIEWS
- [x] Favorites (Shops, Professionals, Services)
- [x] Booking reminders & "Due for your next service" journeys
- [x] Review system (Post-completion only)
- [x] Verified review markers

## J. SETTINGS & PREFERENCES
- [x] Profile management
- [x] Gender and Category preferences
- [x] Notification preferences (Push + In-app)
- [x] Theme selection (System/Light/Dark)
- [x] Language and Currency units
- [x] Account deletion / Data controls

## K. SYSTEM & OBSERVABILITY
- [x] Configuration-driven behavior (Feature Flags)
- [x] Location/Geo rules engine
- [x] Notifications Center
- [x] Help & Support (FAQ + Tickets)
- [x] Analytics (Funnels, Journey tracking, Privacy-safe event logging)
- [x] Crash reporting and Performance monitoring (CrashReporter abstraction)
- [x] Offline / Error state handling (Visual error banners and indicators)
- [x] Secure payment integration (Multi-gateway mock selector & settlement status)
