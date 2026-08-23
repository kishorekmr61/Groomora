# Groomora — Known Issues & Next Steps

## Known Behaviors & Technical Notes

1. **Local iOS Native Compilation on Windows Host**:
   - As expected for Kotlin Multiplatform development on Windows OS, native Apple toolchain tasks (`iosArm64`, `iosX64`, `iosSimulatorArm64`) are automatically disabled via Gradle when running on Windows. The shared CMP common codebase is ready for compilation on macOS with Xcode.

2. **Mock Repository Implementation**:
   - All feature modules are backed by deterministic in-memory `Mock*Repository` classes (`MockBookingRepository`, `MockOrderRepository`, `MockBeautyRepository`, etc.). These can be swapped with Ktor HTTP client network implementations without altering any ViewModel, Domain, or Composable UI code.

3. **Map Integration**:
   - The Nearby Map view is rendered using an interactive Compose Multiplatform map surface with simulated GPS coordinates, pins, and floating preview sheets. Real Google Maps / MapLibre CMP SDK can be hooked directly into `DiscoveryScreen.kt` using expect/actual.

4. **Payment Gateway**:
   - The checkout and booking flows simulate live payment settlement across UPI, Card, Net Banking, and Pay at Venue/Home with full status and refund timelines. Production Razorpay/Stripe SDKs can be plugged into `PaymentMethodType`.
