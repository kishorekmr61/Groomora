package com.groomora.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.design.*
import com.groomora.design.components.*
import com.groomora.feature.auth.User
import com.groomora.feature.auth.UserGender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val currentUser = state.user

    var name by remember(currentUser) { mutableStateOf(currentUser?.name ?: "") }
    var phone by remember(currentUser) { mutableStateOf(currentUser?.phoneNumber ?: "") }
    var email by remember(currentUser) { mutableStateOf(currentUser?.email ?: "") }
    var profileImageUrl by remember(currentUser) { mutableStateOf(currentUser?.profileImageUrl ?: "") }
    var selectedGender by remember(currentUser) { mutableStateOf(currentUser?.gender ?: UserGender.UNSPECIFIED) }
    
    var showImagePickerDialog by remember { mutableStateOf(false) }
    var customUrlInput by remember { mutableStateOf("") }
    var showCustomUrlField by remember { mutableStateOf(false) }
    var showSavedSnackbar by remember { mutableStateOf(false) }

    val presetAvatars = listOf(
        "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=400&q=80",
        "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=400&q=80",
        "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=400&q=80",
        "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=400&q=80",
        "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=400&q=80",
        "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=400&q=80"
    )

    Scaffold(
        topBar = {
            GroomoraTopAppBar(
                title = "Edit Profile",
                onBack = onBack
            )
        },
        snackbarHost = {
            if (showSavedSnackbar) {
                Snackbar(
                    modifier = Modifier.padding(16.dp),
                    containerColor = Charcoal,
                    contentColor = Color.White
                ) {
                    Text("Profile updated successfully!")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(WarmIvory)
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // 1. Profile Avatar & Change Photo Badge
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier.size(110.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .clip(CircleShape)
                                .background(Charcoal)
                                .border(3.dp, HoneyAmber, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (profileImageUrl.isNotBlank()) {
                                GroomoraImage(
                                    url = profileImageUrl,
                                    contentDescription = "Profile Photo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "Default Avatar",
                                    tint = Color.White,
                                    modifier = Modifier.size(80.dp)
                                )
                            }
                        }

                        // Camera badge edit button
                        IconButton(
                            onClick = { showImagePickerDialog = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(HoneyAmber)
                                .border(2.dp, Color.White, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Change profile photo",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }


                    Spacer(Modifier.height(10.dp))

                    TextButton(
                        onClick = { showImagePickerDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null,
                            tint = HoneyAmber,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = "Change Profile Photo",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = HoneyAmber
                        )
                    }
                }
            }

            // 2. Personal Information Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Personal Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppText
                    )
                }
            }

            // 3. Name Field
            item {
                GroomoraOutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = "Full Name",
                    placeholder = "Enter your full name",
                    leadingIcon = Icons.Default.Person,
                    isError = name.isBlank() && currentUser != null,
                    errorMessage = if (name.isBlank()) "Full name is required" else null
                )
            }

            // 4. Phone Number Field
            item {
                GroomoraOutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = "Phone Number",
                    placeholder = "+91 98765 43210",
                    leadingIcon = Icons.Default.Phone,
                    isError = phone.isBlank() && currentUser != null,
                    errorMessage = if (phone.isBlank()) "Phone number is required" else null
                )
            }

            // 5. Email Address Field
            item {
                GroomoraOutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = "Email Address",
                    placeholder = "name@example.com",
                    leadingIcon = Icons.Default.Email
                )
            }

            // 6. Gender Selector (Mandatory)
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Gender *",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (selectedGender == UserGender.UNSPECIFIED) ErrorRed else AppText
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = "(Mandatory)",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (selectedGender == UserGender.UNSPECIFIED) ErrorRed else MutedText
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        GroomoraFilterChip(
                            label = "Female",
                            isSelected = selectedGender == UserGender.FEMALE,
                            onClick = { selectedGender = UserGender.FEMALE }
                        )
                        GroomoraFilterChip(
                            label = "Male",
                            isSelected = selectedGender == UserGender.MALE,
                            onClick = { selectedGender = UserGender.MALE }
                        )
                    }
                    if (selectedGender == UserGender.UNSPECIFIED) {
                        Text(
                            text = "Please select your gender to save profile changes.",
                            style = MaterialTheme.typography.labelSmall,
                            color = ErrorRed
                        )
                    }
                }
            }

            // 7. Save & Cancel Buttons
            item {
                Spacer(Modifier.height(14.dp))
                GroomoraPrimaryButton(
                    text = "Save Profile Changes",
                    enabled = name.isNotBlank() && phone.isNotBlank() && selectedGender != UserGender.UNSPECIFIED,
                    onClick = {
                        val updated = User(
                            id = currentUser?.id ?: "user_1",
                            name = name.trim(),
                            phoneNumber = phone.trim(),
                            email = email.trim().ifEmpty { null },
                            profileImageUrl = profileImageUrl.trim().ifEmpty { null },
                            gender = selectedGender
                        )
                        viewModel.onIntent(ProfileIntent.UpdateUser(updated))
                        showSavedSnackbar = true
                        onBack()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    height = 48.dp
                )


                Spacer(Modifier.height(10.dp))

                GroomoraOutlinedButton(
                    text = "Cancel",
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth(),
                    height = 48.dp
                )
                Spacer(Modifier.height(24.dp))
            }
        }

        // Avatar Upload / Select Dialog
        if (showImagePickerDialog) {
            AlertDialog(
                onDismissRequest = { showImagePickerDialog = false },
                title = {
                    Text(
                        "Choose Profile Photo",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = AppText
                    )
                },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            "Select from gallery presets or enter a custom photo link:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MutedText
                        )

                        // Preset Avatar Row
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
                        ) {
                            items(presetAvatars) { url ->
                                val isChosen = profileImageUrl == url
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(CircleShape)
                                        .background(Charcoal)
                                        .border(
                                            width = if (isChosen) 3.dp else 1.dp,
                                            color = if (isChosen) HoneyAmber else BorderGray,
                                            shape = CircleShape
                                        )
                                        .clickable {
                                            profileImageUrl = url
                                            showImagePickerDialog = false
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    GroomoraImage(
                                        url = url,
                                        contentDescription = "Avatar preset",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                }
                            }
                        }

                        if (!showCustomUrlField) {
                            TextButton(onClick = { showCustomUrlField = true }) {
                                Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Enter image URL directly")
                            }
                        } else {

                            GroomoraOutlinedTextField(
                                value = customUrlInput,
                                onValueChange = { customUrlInput = it },
                                label = "Photo URL",
                                placeholder = "https://example.com/photo.jpg"
                            )
                        }

                        if (profileImageUrl.isNotBlank()) {
                            TextButton(
                                onClick = {
                                    profileImageUrl = ""
                                    showImagePickerDialog = false
                                },
                                colors = ButtonDefaults.textButtonColors(contentColor = ErrorRed)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(6.dp))
                                Text("Remove current photo")
                            }
                        }
                    }
                },
                confirmButton = {
                    if (showCustomUrlField && customUrlInput.isNotBlank()) {
                        Button(
                            onClick = {
                                profileImageUrl = customUrlInput.trim()
                                showImagePickerDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = HoneyAmber)
                        ) {
                            Text("Use URL")
                        }
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showImagePickerDialog = false }) {
                        Text("Close", color = AppText)
                    }
                },
                containerColor = Color.White
            )
        }
    }
}
