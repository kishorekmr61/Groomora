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
}
