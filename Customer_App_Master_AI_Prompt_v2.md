BARBERSHOP CUSTOMER APP — MASTER AI BUILD PROMPT v2
DOCUMENT TYPE: Product + UX + Architecture + Implementation Contract
TARGET: Android + iOS
TECHNOLOGY: Kotlin + Compose Multiplatform (CMP)
ARCHITECTURE: Feature-First Clean Architecture + MVI
STATUS: Build-ready specification. Do not omit, simplify, invent, or silently remove requirements.

1. ROLE
You are a senior mobile architect, product engineer and UX engineer. Build a scalable customer marketplace app for barbershops, salons, beauty parlours, hair stylists, makeup artists, bridal specialists and home-service professionals.

2. SOURCE OF TRUTH
Treat this prompt as the functional source of truth. The supplied visual prototype/reference defines the visual direction: premium cream/ivory background, charcoal/black, warm gold/champagne accents, rounded cards, photography-led discovery, compact bottom navigation and a marketplace-first experience.
If a requirement is not implemented in the first release, create an explicit TODO with the exact feature name; never silently drop it.

3. NON-NEGOTIABLE PRODUCT JOURNEY
Home → Location → Discover/Search → Shop/Professional → Portfolio → Gender/Category → Service or Package → Add-ons → Professional → Date & Time → Location (shop/home) → Offer → Loyalty/Referral → Payment → Confirmation → Booking History → Review → Rebook.

4. CUSTOMER FEATURES — COMPLETE CHECKLIST
A. HOME/DISCOVERY
- Location shown on home screen.
- Current location permission and manual location.
- Saved addresses and default address.
- Search services, shops, professionals, styles and products.
- Nearby list and map view.
- Categories: Hair, Skin, Makeup, Nails, Beard, Grooming, Bridal, Beauty, Home Service and configurable future categories.
- Men, Women and Unisex service paths.
- Top-rated, nearby, trending and personalized recommendations.
- Home-screen promotional banner.
- Configurable special-offer announcement text, image, CTA, schedule, audience, location and deep link.
- Recently viewed/favorites/rebook modules.
B. SHOPS/PROFESSIONALS
- Shop details, status, address, distance, hours, call, directions, share, save.
- Services, packages, gallery/portfolio, professionals, reviews.
- Verified shop/professional indicators.
- Professional profile, skills, categories, portfolio, ratings, availability and service eligibility.
- Portfolio → Discover → Book.
C. SERVICES/PACKAGES
- Individual services.
- Configurable packages.
- Package included services and quantities.
- Package price and savings.
- Duration.
- Gender/category applicability.
- Shop/professional/location applicability.
- Availability.
- Home-service applicability.
- Add-ons.
- Package-specific offers.
- Package-specific loyalty/referral eligibility.
- Package validity, usage limits and booking constraints.
D. BOOKING
- Select service/package.
- Select add-ons.
- Select professional or any available professional.
- Real-time availability.
- Date/time.
- Shop or home service.
- Address validation.
- Travel time and travel fee where applicable.
- Offer application.
- Loyalty redemption.
- Referral eligibility.
- Price breakdown.
- Payment method.
- Confirmation.
- Booking history.
- Reschedule.
- Cancel.
- Refund status.
- Rebook.
E. SPECIAL SERVICES
- Bridal: makeup, hair, saree draping, engagement/pre-bridal packages and configurable bridal packages.
- Home services: service radius, eligible professionals, address, travel fee, ETA/status and on-time/hygiene messaging.
- Beauty parlour: skin, makeup, nails, waxing, facial, treatments and configurable categories.
F. COMMERCE
- Product catalog.
- Categories/search/filter.
- Product details.
- Cart.
- Address.
- Payment.
- Order confirmation.
- Order history.
- Shipping/tracking status.
- Refund/cancellation status.
G. OFFERS
- First-booking.
- Shop-specific.
- Service-specific.
- Package-specific.
- Product offers.
- Seasonal.
- Bridal.
- Home-service.
- Location-specific.
- Loyalty.
- Referral.
- Membership.
- Minimum order/booking value.
- Maximum discount.
- Validity.
- Usage limits.
- Eligibility.
- Stacking rules.
- Home-screen promotion.
H. LOYALTY/REFERRAL/MEMBERSHIP
- Points balance.
- Earn rules.
- Redeem.
- Transaction history.
- Expiry.
- Referral code/link.
- Referral status/rewards.
- Abuse/eligibility handling.
- Membership plans, benefits, price, renewal and status.
I. RETENTION
- Favorites: shops, professionals, services, packages.
- One-tap rebooking.
- Booking reminders.
- Personalized recommendations.
- “Book again” and “due for your next service” journeys.
J. REVIEWS
- Review only after completed eligible booking/order.
- Rating, text and optional media if enabled.
- Verified review marker.
- Review history.
K. NOTIFICATIONS
- Push + in-app notification center.
- Booking confirmation/reminder/reschedule/cancellation.
- Payment.
- Home-service status.
- Offers.
- Loyalty.
- Referral.
- Product/order.
- Recommendations.
- Announcements.
- Operational/maintenance.
- Deep links.
- Read/unread and mark all read.
L. SETTINGS/PREFERENCES
- Account/profile.
- Gender/category preference.
- Preferred services.
- Preferred professionals/shops.
- Home-service preference.
- Saved locations.
- Notification preferences.
- Marketing consent.
- Analytics consent.
- Personalized recommendations.
- Location permission.
- Language.
- Theme: system/light/dark.
- Currency and distance unit.
- Payment methods.
- Privacy/security.
- Data/account controls.
- Help/support.
- Logout/delete account.
- Reset personalization/preferences.
M. SAFETY/TRUST
- Verified professionals/shops.
- Completed-booking review model.
- Report problem.
- Support.
- Secure payments.
- Transparent final price.
- No double-booking.
N. OBSERVABILITY
- Analytics events.
- Funnel/journey tracking.
- Session analytics with privacy-safe implementation.
- Crash reporting.
- Non-fatal errors.
- ANR.
- Performance/network metrics.
- Never record passwords, OTPs, access/refresh tokens, payment credentials or unnecessary precise location.

5. CONFIGURATION-DRIVEN BEHAVIOR
Do not hardcode business availability or feature behavior. The customer app must consume a configuration model supporting:
- Feature flags.
- Region/platform targeting.
- Maintenance.
- Minimum/latest version and force update.
- Category/service availability.
- Packages.
- Offers.
- Loyalty/referral.
- Notification campaigns.
- Geo rules.
- Business rules.
Cache the last known safe configuration. Fail closed for unsafe business rules. Backend is authoritative for price, eligibility, availability and final booking/payment state.

6. LOCATION/GEO
Create LocationManager + LocationRepository + GeoRulesEngine.
Hierarchy: country → state → city → zone → pincode → radius.
Use it for discovery, service availability, home-service eligibility, travel fee, offers, regional features, pricing rules and maintenance scope.
Do not continuously collect precise location without user permission/need.

7. ARCHITECTURE
Use a modular CMP project.
core modules: core-network, core-database, core-security, core-location, core-geo, core-configuration, core-analytics, core-crash, core-performance, core-notifications, core-designsystem, core-navigation, core-logging.
feature modules: auth, onboarding, home, discovery, nearby, shops, professionals, services, packages, portfolio, booking, availability, home-service, bridal, beauty, offers, loyalty, referral, membership, products, cart, orders, favorites, reviews, notifications, profile, settings, support.
Each feature: data / domain / presentation.
MVI: User Intent → ViewModel/Reducer → UseCase → Repository → DataSource → State/Effect → Compose UI.
No business logic in Composables. No feature-to-feature data-source coupling. Depend on interfaces.

8. NAVIGATION
Define typed navigation routes and deep links for:
home, nearby, shop, professional, service, package, booking, offers, loyalty, referral, membership, product, cart, order, favorites, notifications, settings, support, review.
Deep links must be safe and authorization-aware.

9. DESIGN SYSTEM
Use centralized design tokens only.
Base:
Charcoal #1C1C1C
Near Black #111111
Warm Gold #C9A227
Champagne #E5C76B
Warm Ivory #FAF8F3
White #FFFFFF
Text #202020
Muted #6B6B6B
Success #2E7D5B
Error #B23A48
Divider #E5E2DC
Category accents: Barbershop charcoal+gold; Hair/Spa plum+champagne; Beauty rose+champagne; Bridal burgundy+gold; Home Service teal+charcoal; Products charcoal+gold; Loyalty gold; Offers burgundy.
Use accessible contrast, light/dark theme, dynamic text scaling and reusable components.

10. DATA/NETWORK
Start with mock repositories and deterministic mock data. Define API interfaces/DTOs/mappers so real backend can replace mocks without UI/domain rewrite. Add loading/empty/error/offline states for every remote screen.

11. PRICING/BOOKING RULE
Client displays an estimated breakdown but server/backend will eventually return authoritative quote, eligibility, slot lock and final amount. Prevent duplicate submission with idempotency keys. Never trust client-side price or availability.

12. TESTING
Unit tests for use cases, reducers, validators, mappers and repositories. UI tests for critical journeys. Test permissions, deep links, offline, configuration changes, maintenance, version gates, booking races, package rules, offers, loyalty/referral and payment states.

13. DELIVERY ORDER
Phase 1: project skeleton + design system + navigation + mock data.
Phase 2: auth/onboarding + location + configuration.
Phase 3: home/discovery/nearby/shop/professional/portfolio.
Phase 4: services/packages/add-ons/availability/booking.
Phase 5: offers/loyalty/referral/membership.
Phase 6: home services/bridal/beauty.
Phase 7: products/cart/orders.
Phase 8: notifications/settings/reviews/support.
Phase 9: analytics/session/crash/performance.
Phase 10: hardening, accessibility, offline/error states, tests and release readiness.

14. OUTPUT CONTRACT FOR THE AI CODING AGENT
Before coding, output:
- Assumptions.
- Requirements checklist with every item above.
- Module dependency graph.
- Folder tree.
- Navigation graph.
- Domain model list.
- MVI state/intent/effect for each feature.
- Configuration schema.
- Geo rule model.
- Package/availability/offer/loyalty/referral models.
Then implement phase-by-phase. After each phase, run/describe compilation and tests, list changed files and list remaining checklist items. Never claim a feature is complete unless it has UI + domain + data contract + navigation + states + tests where applicable.