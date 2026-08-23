package com.groomora.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.app.DependencyContainer
import com.groomora.design.*
import com.groomora.design.components.*

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onSignUpSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val state by viewModel.authState.collectAsState()

    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(UserGender.MALE) }
    var referralCode by remember { mutableStateOf("") }
    var agreedToTerms by remember { mutableStateOf(true) }

    // Validation errors
    var nameError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }
    var termsError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        DependencyContainer.analyticsManager.logScreenView("signup_screen")
    }

    LaunchedEffect(state) {
        if (state is AuthState.Authenticated) {
            val user = (state as AuthState.Authenticated).user
            DependencyContainer.crashReporter.setUserId(user.id)
            DependencyContainer.crashReporter.setCustomKey("user_phone", user.phoneNumber)
            DependencyContainer.analyticsManager.logEvent(
                "signup_success",
                mapOf("user_id" to user.id, "gender" to user.gender.name)
            )
            onSignUpSuccess()
        } else if (state is AuthState.Error) {
            DependencyContainer.analyticsManager.logEvent(
                "signup_failure",
                mapOf("error" to (state as AuthState.Error).message)
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WarmIvory)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // Brand Insignia
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Charcoal),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "G",
                    color = Champagne,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
            }

            Spacer(Modifier.height(12.dp))

            GroomoraHeadline(
                text = "Join Groomora",
                letterSpacing = 2.sp,
                textAlign = TextAlign.Center
            )
            GroomoraCaption(
                text = "Experience seamless salon & at-home grooming",
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(20.dp))

            // Main Signup Card
            GroomoraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    GroomoraTitle(text = "Create Account")

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

                    // 1. Full Name
                    GroomoraOutlinedTextField(
                        value = fullName,
                        onValueChange = {
                            fullName = it
                            nameError = null
                        },
                        label = "Full Name *",
                        placeholder = "e.g. Rahul Sharma",
                        leadingIcon = Icons.Default.Person,
                        isError = nameError != null,
                        errorMessage = nameError
                    )

                    // 2. Mobile Number
                    GroomoraPhoneField(
                        value = phoneNumber,
                        onValueChange = {
                            phoneNumber = it
                            phoneError = null
                        },
                        isError = phoneError != null,
                        errorMessage = phoneError
                    )

                    // 3. Email Address
                    GroomoraOutlinedTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = null
                        },
                        label = "Email Address *",
                        placeholder = "e.g. rahul@gmail.com",
                        leadingIcon = Icons.Default.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        isError = emailError != null,
                        errorMessage = emailError
                    )

                    // 4. Gender Selector
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        GroomoraCaption(text = "Gender *", fontWeight = FontWeight.Bold, color = AppText)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            UserGender.entries.filter { it != UserGender.UNSPECIFIED }.forEach { gender ->
                                val isSelected = selectedGender == gender
                                Surface(
                                    shape = CircleShape,
                                    color = if (isSelected) HoneyAmber else Color.White,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) HoneyAmber else BorderGray
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedGender = gender }
                                ) {
                                    Text(
                                        text = gender.name.lowercase().replaceFirstChar { it.uppercase() },
                                        style = MaterialTheme.typography.labelMedium,
                                        color = if (isSelected) Color.White else AppText,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(vertical = 10.dp)
                                    )
                                }
                            }
                        }
                    }

                    // 5. Password
                    GroomoraPasswordField(
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = null
                        },
                        label = "Set Password *",
                        isError = passwordError != null,
                        errorMessage = passwordError
                    )

                    // 6. Referral Code (Optional)
                    GroomoraOutlinedTextField(
                        value = referralCode,
                        onValueChange = { referralCode = it.uppercase() },
                        label = "Referral Code (Optional)",
                        placeholder = "e.g. GROOM50"
                    )

                    // 7. Terms & Conditions Checkbox
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = agreedToTerms,
                            onCheckedChange = {
                                agreedToTerms = it
                                termsError = null
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = HoneyAmber,
                                checkmarkColor = Color.White
                            )
                        )
                        Text(
                            text = "I agree to Groomora's Terms & Privacy Policy",
                            style = MaterialTheme.typography.bodySmall,
                            color = AppText
                        )
                    }
                    if (termsError != null) {
                        GroomoraCaption(text = termsError!!, color = ErrorRed)
                    }

                    // 8. Submit Button
                    GroomoraPrimaryButton(
                        text = "Create Account",
                        onClick = {
                            var hasError = false

                            if (fullName.isBlank()) {
                                nameError = "Full name is mandatory"
                                hasError = true
                            } else if (fullName.length < 2) {
                                nameError = "Name must be at least 2 characters"
                                hasError = true
                            }

                            if (phoneNumber.isBlank()) {
                                phoneError = "Mobile number is mandatory"
                                hasError = true
                            } else if (phoneNumber.length < 10) {
                                phoneError = "Please enter a valid 10-digit mobile number"
                                hasError = true
                            }

                            val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
                            if (email.isBlank()) {
                                emailError = "Email address is mandatory"
                                hasError = true
                            } else if (!email.matches(Regex(emailPattern))) {
                                emailError = "Please enter a valid email address"
                                hasError = true
                            }

                            if (password.isBlank()) {
                                passwordError = "Password is mandatory"
                                hasError = true
                            } else if (password.length < 6) {
                                passwordError = "Password must be at least 6 characters"
                                hasError = true
                            }

                            if (!agreedToTerms) {
                                termsError = "Please agree to the Terms of Service"
                                hasError = true
                            }

                            if (!hasError) {
                                DependencyContainer.analyticsManager.logEvent(
                                    "signup_attempt",
                                    mapOf("gender" to selectedGender.name)
                                )
                                viewModel.onIntent(
                                    AuthIntent.SignUp(
                                        name = fullName.trim(),
                                        phoneNumber = phoneNumber.trim(),
                                        email = email.trim(),
                                        gender = selectedGender,
                                        password = password,
                                        referralCode = referralCode.trim().takeIf { it.isNotBlank() }
                                    )
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isLoading = state is AuthState.Loading
                    )

                    // 9. Link to Login
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        GroomoraCaption(text = "Already have an account? ")
                        Text(
                            text = "Log In",
                            style = MaterialTheme.typography.labelMedium,
                            color = HoneyAmber,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable(onClick = onNavigateToLogin)
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}
