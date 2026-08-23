# Groomora Architecture Documentation

## Overview
Groomora is a multiplatform customer marketplace application for barbershops, beauty salons, spas, bridal artists, and home-service professionals built using **Kotlin Multiplatform (KMP)** and **Compose Multiplatform (CMP)** for Android and iOS.

---

## 1. Architectural Style
- **Pattern**: Feature-First Clean Architecture + MVI (Model-View-Intent) unidirectional data flow.
- **Unidirectional Flow**:
  $$\text{User Intent} \longrightarrow \text{ViewModel} \longrightarrow \text{Repository / UseCase} \longrightarrow \text{StateFlow<State>} \longrightarrow \text{Compose UI}$$

---

## 2. Directory & Module Structure

```
composeApp/src/commonMain/kotlin/com/groomora/
├── app/
│   ├── App.kt                      # NavHost, application shell, route registry
│   └── DependencyContainer.kt      # Central DI service container
├── core/
│   ├── analytics/                  # Privacy-safe funnel and journey tracking (AnalyticsManager)
│   ├── configuration/              # Feature flags, versioning, remote config (ConfigRepository)
│   ├── crash/                      # Non-fatal error reporting and breadcrumbs (CrashReporter)
│   ├── geo/                        # Hierarchical geo rules and zone engine (GeoRulesEngine)
│   ├── location/                   # User location, addresses, permissions (LocationRepository)
│   └── navigation/                 # Type-safe Serializable navigation routes (Screen)
├── design/
│   └── GroomoraTheme.kt            # Luxury design tokens (Charcoal, Warm Gold, Champagne, Warm Ivory)
└── feature/
    ├── auth/                       # Phone OTP login, auth state
    ├── onboarding/                 # Splash and onboarding walkthrough
    ├── home/                       # Retention carousels, rebooking, categories, banners
    ├── discovery/                  # Shop & stylist discovery, List & Map view, profile
    ├── shop/                       # Shop details, services, packages, reviews
    ├── booking/                    # Booking engine, slot calendar, add-ons, payments, history, reschedule/cancel
    ├── bridal/                     # Specialized bridal packages and artists
    ├── beauty/                     # Specialized beauty parlour lounge, facials, waxing, nail art
    ├── homeservice/                # Hygiene messaging, travel fee, radius validation
    ├── offers/                     # Coupon engine, seasonal deals, promo cards
    ├── loyalty/                    # Points balance, tier benefits, referral rewards
    ├── products/                   # Commerce catalog, product details, cart, checkout, orders
    ├── favorites/                  # Saved shops, stylists, and services
    ├── reviews/                    # Verified post-completion ratings and feedback
    ├── notifications/              # In-app notification center
    ├── profile/                    # User account, settings, address management
    └── support/                    # FAQ accordions and helpdesk ticketing
```

---

## 3. Navigation Graph
Typed navigation routes via `kotlinx.serialization.Serializable`:
- `Screen.Onboarding` $\rightarrow$ `Screen.Login` $\rightarrow$ `Screen.Home`
- `Screen.Discovery(categoryId)` $\rightarrow$ `Screen.ShopDetails(shopId)` $\rightarrow$ `Screen.ProfessionalProfile(professionalId)`
- `Screen.Booking(serviceId, packageId)` $\rightarrow$ `Screen.BookingHistory`
- `Screen.Beauty` & `Screen.Bridal` & `Screen.HomeService` $\rightarrow$ `Screen.Booking`
- `Screen.Products` $\rightarrow$ `Screen.ProductDetails(productId)` $\rightarrow$ `Screen.Cart` $\rightarrow$ `Screen.Orders`
- `Screen.Profile` $\rightarrow$ `Screen.Settings`, `Screen.AddressManagement`, `Screen.Favorites`, `Screen.Support`, `Screen.BookingHistory`

---

## 4. Design System Tokens
- **Primary Charcoal**: `#1C1C1C`
- **Near Black**: `#111111`
- **Warm Gold**: `#C9A227`
- **Champagne**: `#E5C76B`
- **Warm Ivory**: `#FAF8F3`
- **Text Primary**: `#202020`
- **Success Green**: `#2E7D5B`
- **Alert Error**: `#B23A48`
- **Border Divider**: `#E5E2DC`

---

## 5. Security & Observability Guarantees
- **Backend Authority**: Final price, slot reservation, and coupon eligibility calculated with idempotency keys.
- **Privacy Sanitization**: `AnalyticsManager` automatically strips PII (`password`, `token`, `otp`, `card_number`).
- **Resilience**: Every remote feature screen supports Loading, Empty, and Error states with retry capability.
