# Groomora Release Checklist & Audit

## 1. Project & Build Health
- [x] Android target compiles without errors (`./gradlew.bat compileDebugKotlinAndroid`).
- [x] Unit test suite passes cleanly (`./gradlew.bat testDebugUnitTest`).
- [x] Zero unresolved references or missing composable parameters.
- [x] Android SDK: `compileSdk = 35`, `minSdk = 26`, `targetSdk = 35`.
- [x] Kotlin 2.1.0 and Compose Multiplatform 1.7.3 compatibility confirmed.

---

## 2. Navigation & Architecture Verification
- [x] Type-safe routing across 20+ routes with `Screen` sealed hierarchy.
- [x] No circular dependencies between feature modules.
- [x] Clean repository and interface boundaries via `DependencyContainer`.
- [x] MVI pattern maintained in all ViewModels (`StateFlow` + sealed `Intent`s).
- [x] Deep-link ready route serialization.

---

## 3. Customer Feature Completion Audit
- [x] **Home / Discovery**:
  - [x] Location header with GPS / manual address picker.
  - [x] "Due for Grooming" retention module with 1-tap rebook.
  - [x] List View and Interactive Map View toggle with shop marker cards.
  - [x] Category routing (Hair, Beard, Beauty, Bridal, Home Service, Nails, Skin).
  - [x] Promotional banners with deep links to offers.
- [x] **Shops & Stylists**:
  - [x] Verified indicators, ratings, distance, and contact CTAs.
  - [x] Portfolios, professional skill highlights, and service lists.
  - [x] Favorites toggle integration with repository persistence.
- [x] **Booking Engine**:
  - [x] Individual service & multi-service package booking.
  - [x] Real-time slot calendar with morning/afternoon/evening slots.
  - [x] Home-service toggle with dynamic travel fee calculation.
  - [x] Add-on selector with dynamic price updates.
  - [x] Live coupon code validation and loyalty point deduction.
  - [x] Multi-gateway payment selection (UPI, Cards, Net Banking, Pay at Venue).
  - [x] In-app appointment rescheduling and cancellation with reason tracking.
  - [x] **100% Refund Status Timeline** (`INITIATED` $\rightarrow$ `PROCESSING` $\rightarrow$ `REFUNDED`).
- [x] **Specialized Services**:
  - [x] Bridal Lounge: Makeup, Hair, Saree draping, Pre-bridal packages.
  - [x] Beauty Lounge: O3+ Facials, Rica Waxing, Korean Glass Peel, Gel Nails, Hair Spa.
  - [x] Home Services: Service radius validation, ETA status, safety/hygiene messaging.
- [x] **Commerce (Products)**:
  - [x] Product catalog, search, and category filtering.
  - [x] Cart management with free delivery incentives.
  - [x] Interactive checkout flow (address selection, payment method, place order).
  - [x] Order history with courier tracking and order cancellation.
- [x] **Loyalty, Offers & Referral**:
  - [x] Referral links and reward balances.
  - [x] Configurable discount logic and usage limits.
  - [x] Tiered loyalty benefits.
- [x] **Observability & Support**:
  - [x] Privacy-safe event tracking (automatic PII scrubbing).
  - [x] Crash reporting and non-fatal error logging.
  - [x] Help & Support center with FAQ accordions and ticket submission.

---

## 4. Release Status
- **Android Target**: Build-ready for debug/release signing.
- **iOS Target**: Framework configured (`ComposeApp.framework`).
- **Mock Data**: Fully deterministic for offline/demo operation.
