package com.groomora.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.app.DependencyContainer
import com.groomora.design.*
import com.groomora.design.components.*
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit = {}
) {
    val state by viewModel.authState.collectAsState()
    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }
    var isPasswordLogin by remember { mutableStateOf(false) }

    // Validation error states
    var phoneError by remember { mutableStateOf<String?>(null) }
    var otpError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Resend OTP timer
    var resendCountdown by remember { mutableIntStateOf(30) }
    var canResend by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        DependencyContainer.analyticsManager.logScreenView("login_screen")
    }

    LaunchedEffect(isOtpSent) {
        if (isOtpSent) {
            resendCountdown = 30
            canResend = false
            while (resendCountdown > 0) {
                delay(1000)
                resendCountdown -= 1
            }
            canResend = true
        }
    }

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) {
            val user = (state as AuthState.Authenticated).user
            DependencyContainer.crashReporter.setUserId(user.id)
            DependencyContainer.crashReporter.setCustomKey("user_phone", user.phoneNumber)
            DependencyContainer.analyticsManager.logEvent(
                "login_success",
                mapOf("method" to if (isPasswordLogin) "password" else "otp")
            )
            onLoginSuccess()
        } else if (state is AuthState.Error) {
            DependencyContainer.analyticsManager.logEvent(
                "login_failure",
                mapOf("error" to (state as AuthState.Error).message)
            )
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Brand Logo & Title
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(Charcoal),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    color = Champagne,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                )
            }

            Spacer(Modifier.height(16.dp))

            GroomoraHeadline(
                text = "GROOMORA",
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center
            )
            GroomoraCaption(
                text = "Book Your Style • Salon & Home Services",
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            // Main Login Card
            GroomoraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    GroomoraTitle(
                        text = when {
                            isOtpSent -> "Verify OTP"
                            isPasswordLogin -> "Login with Password"
                            else -> "Welcome Back"
                        }
                    )

                    // Error Message Banner from Server
                    if (state is AuthState.Error) {
                        Surface(
                            shape = MaterialTheme.shapes.small,
                            color = ErrorRed.copy(alpha = 0.1f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            GroomoraCaption(
                                text = (state as AuthState.Error).message,
                                color = ErrorRed,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }

                    if (!isOtpSent) {
                        // Reusable GroomoraPhoneField with validation
                        GroomoraPhoneField(
                            value = phoneNumber,
                            onValueChange = {
                                phoneNumber = it
                                phoneError = null
                            },
                            isError = phoneError != null,
                            errorMessage = phoneError
                        )

                        // Reusable GroomoraPasswordField with toggle & validation
                        if (isPasswordLogin) {
                            GroomoraPasswordField(
                                value = password,
                                onValueChange = {
                                    password = it
                                    passwordError = null
                                },
                                isError = passwordError != null,
                                errorMessage = passwordError
                            )
                        }
                    } else {
                        // Reusable GroomoraOtpField with centered letter spacing & validation
                        GroomoraCaption(
                            text = "Enter the 4-digit verification code sent to +91 $phoneNumber"
                        )

                        GroomoraOtpField(
                            value = otp,
                            onValueChange = {
                                otp = it
                                otpError = null
                            },
                            isError = otpError != null,
                            errorMessage = otpError
                        )

                        // Resend OTP countdown
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GroomoraCaption(
                                text = if (!canResend) "Resend code in ${resendCountdown}s" else "Didn't receive OTP?"
                            )
                            if (canResend) {
                                Text(
                                    text = "Resend OTP",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = HoneyAmber,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable {
                                        resendCountdown = 30
                                        canResend = false
                                        DependencyContainer.analyticsManager.logEvent("resend_otp_clicked")
                                    }
                                )
                            }
                        }
                    }

                    // Primary Action Button
                    GroomoraPrimaryButton(
                        text = when {
                            isOtpSent -> "Verify & Continue"
                            isPasswordLogin -> "Sign In"
                            else -> "Get OTP"
                        },
                        onClick = {
                            if (!isOtpSent) {
                                when {
                                    phoneNumber.isBlank() -> {
                                        phoneError = "Mobile number is mandatory"
                                    }
                                    phoneNumber.length < 10 -> {
                                        phoneError = "Please enter a valid 10-digit mobile number"
                                    }
                                    isPasswordLogin -> {
                                        if (password.isBlank()) {
                                            passwordError = "Password is mandatory"
                                        } else if (password.length < 6) {
                                            passwordError = "Password must be at least 6 characters"
                                        } else {
                                            viewModel.onIntent(AuthIntent.Login(phoneNumber, password))
                                        }
                                    }
                                    else -> {
                                        isOtpSent = true
                                        DependencyContainer.analyticsManager.logEvent(
                                            "login_otp_sent",
                                            mapOf("phone" to phoneNumber.take(3) + "****")
                                        )
                                    }
                                }
                            } else {
                                when {
                                    otp.isBlank() -> {
                                        otpError = "OTP is mandatory"
                                    }
                                    otp.length < 4 -> {
                                        otpError = "Please enter the complete 4-digit OTP"
                                    }
                                    else -> {
                                        DependencyContainer.analyticsManager.logEvent("login_attempt")
                                        viewModel.onIntent(AuthIntent.Login(phoneNumber, otp))
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isLoading = state is AuthState.Loading
                    )

                    // Secondary Mode Switchers
                    if (!isOtpSent) {
                        TextButton(
                            onClick = {
                                isPasswordLogin = !isPasswordLogin
                                phoneError = null
                                passwordError = null
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isPasswordLogin) "Login with OTP instead" else "Login with Password",
                                color = AppText,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    } else {
                        TextButton(
                            onClick = {
                                isOtpSent = false
                                otp = ""
                                otpError = null
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Change Mobile Number",
                                color = MutedText,
                                style = MaterialTheme.typography.labelMedium
                            )
                        }
                    }

                    // Don't have an account? Sign Up
                    Row(

                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GroomoraCaption(text = "Don't have an account? ")
                        Text(
                            text = "Sign Up",
                            style = MaterialTheme.typography.labelMedium,
                            color = HoneyAmber,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable(onClick = onNavigateToSignUp)
                        )
                    }
                }
            }


            Spacer(Modifier.height(20.dp))

            // Footer Terms & Conditions
            GroomoraCaption(
                text = "By signing in, you agree to Groomora's\nTerms of Service & Privacy Policy.",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
