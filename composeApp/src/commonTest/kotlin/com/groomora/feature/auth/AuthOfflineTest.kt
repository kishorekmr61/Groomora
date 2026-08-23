package com.groomora.feature.auth

import com.groomora.app.DependencyContainer
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class AuthOfflineTest {

    @BeforeTest
    fun setUp() {
        DependencyContainer.networkConnectivityManager.setConnected(true)
    }

    @AfterTest
    fun tearDown() {
        DependencyContainer.networkConnectivityManager.setConnected(true)
    }

    @Test
    fun login_whenOffline_returnsError() = runBlocking {
        val repo = MockAuthRepository()
        DependencyContainer.networkConnectivityManager.setConnected(false)

        val result = repo.login("9876543210", "1234")
        assertTrue(result is AuthState.Error, "Login when offline should return AuthState.Error")
        assertTrue((result as AuthState.Error).message.contains("No internet", ignoreCase = true))
    }

    @Test
    fun signUp_whenOffline_returnsError() = runBlocking {
        val repo = MockAuthRepository()
        DependencyContainer.networkConnectivityManager.setConnected(false)

        val result = repo.signUp(
            name = "Test User",
            phoneNumber = "9876543210",
            email = "test@example.com",
            gender = UserGender.MALE,
            password = "password123",
            referralCode = null
        )
        assertTrue(result is AuthState.Error, "Sign-up when offline should return AuthState.Error")
        assertTrue((result as AuthState.Error).message.contains("No internet", ignoreCase = true))
    }

    @Test
    fun login_whenOnline_succeeds() = runBlocking {
        val repo = MockAuthRepository()
        DependencyContainer.networkConnectivityManager.setConnected(true)

        val result = repo.login("9876543210", "1234")
        assertTrue(result is AuthState.Authenticated, "Login when online should succeed")
    }
}
