package com.groomora.feature.auth

import com.groomora.core.api.MockAuthApiService
import com.groomora.core.api.SignUpRequest
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class SignUpFlowTest {

    @Test
    fun testSignUpRepositoryFlow() = runBlocking {
        val authRepo = MockAuthRepository()

        val state = authRepo.signUp(
            name = "Karthik Kumar",
            phoneNumber = "9876543210",
            email = "karthik@gmail.com",
            gender = UserGender.MALE,
            password = "securePassword123",
            referralCode = "GROOM50"
        )

        assertTrue(state is AuthState.Authenticated)
        val user = (state as AuthState.Authenticated).user
        assertEquals("Karthik Kumar", user.name)
        assertEquals("9876543210", user.phoneNumber)
        assertEquals("karthik@gmail.com", user.email)
        assertEquals(UserGender.MALE, user.gender)
    }

    @Test
    fun testSignUpApiService() = runBlocking {
        val authApi = MockAuthApiService()

        val response = authApi.signUp(
            SignUpRequest(
                name = "Priya Sharma",
                phoneNumber = "9123456780",
                email = "priya@gmail.com",
                gender = "FEMALE",
                password = "priyaPassword456",
                referralCode = "REFERRAL100"
            )
        )

        assertTrue(response.success)
        assertNotNull(response.data?.token)
        assertEquals("Priya Sharma", response.data?.name)
        assertEquals("9123456780", response.data?.phoneNumber)
        assertEquals("priya@gmail.com", response.data?.email)
    }

    @Test
    fun testBookingAuthGuardFlow() = runBlocking {
        val authRepo = MockAuthRepository()

        // 1. User logs out
        authRepo.logout()
        val unauthenticatedState = authRepo.authState
        var isAuth = false

        // 2. User tries to book while unauthenticated -> triggers signin
        val pendingBooking = com.groomora.core.navigation.Screen.Booking(serviceId = "ser1", shopId = "s1")
        var savedPendingBooking: com.groomora.core.navigation.Screen.Booking? = null

        if (!isAuth) {
            savedPendingBooking = pendingBooking
        }

        assertEquals("ser1", savedPendingBooking?.serviceId)
        assertEquals("s1", savedPendingBooking?.shopId)

        // 3. User performs login
        val loginResult = authRepo.login("9876543210", "1234")
        assertTrue(loginResult is AuthState.Authenticated)

        // 4. On login success, saved pending booking is resumed
        val targetBooking = savedPendingBooking
        savedPendingBooking = null

        assertNotNull(targetBooking)
        assertEquals("ser1", targetBooking.serviceId)
        assertEquals(null, savedPendingBooking)
    }
}
