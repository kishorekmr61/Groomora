package com.groomora.design.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.groomora.design.*

@Composable
fun GroomoraOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String? = null,
    placeholder: String? = null,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    prefix: @Composable (() -> Unit)? = null,
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    maxLines: Int = 1
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    if (isError && errorMessage != null) {
                        error(errorMessage)
                    }
                },
            label = label?.let { { Text(it) } },
            placeholder = placeholder?.let { { Text(it, color = MutedText) } },
            prefix = prefix,
            leadingIcon = leadingIcon?.let {
                { Icon(it, contentDescription = null, tint = MutedText) }
            },
            trailingIcon = trailingIcon,
            isError = isError,
            enabled = enabled,
            singleLine = singleLine,
            maxLines = maxLines,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            visualTransformation = visualTransformation,
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color(0xFFF7F5F0),
                focusedBorderColor = HoneyAmber,
                unfocusedBorderColor = BorderGray,
                errorBorderColor = ErrorRed,
                focusedLabelColor = HoneyAmber,
                unfocusedLabelColor = MutedText,
                errorLabelColor = ErrorRed
            )
        )
        if (isError && errorMessage != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall,
                color = ErrorRed,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun GroomoraPhoneField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Mobile Number *",
    placeholder: String = "Enter 10-digit number",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    GroomoraOutlinedTextField(
        value = value,
        onValueChange = { input ->
            val digits = input.filter { it.isDigit() }
            if (digits.length <= 10) onValueChange(digits)
        },
        label = label,
        placeholder = placeholder,
        prefix = { Text("+91 ", fontWeight = FontWeight.Bold, color = AppText) },
        isError = isError,
        errorMessage = errorMessage,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
        enabled = enabled,
        modifier = modifier
    )
}

@Composable
fun GroomoraOtpField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 4,
    label: String = "4-Digit OTP *",
    placeholder: String = "• • • •",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = { input ->
                val digits = input.filter { it.isDigit() }
                if (digits.length <= length) onValueChange(digits)
            },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    if (isError && errorMessage != null) {
                        error(errorMessage)
                    }
                },
            label = { Text(label) },
            placeholder = { Text(placeholder, color = MutedText, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
            isError = isError,
            enabled = enabled,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Center,
                letterSpacing = 8.sp,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = AppText
            ),
            shape = MaterialTheme.shapes.medium,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = HoneyAmber,
                unfocusedBorderColor = BorderGray,
                errorBorderColor = ErrorRed
            )
        )
        if (isError && errorMessage != null) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall,
                color = ErrorRed,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun GroomoraPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Password *",
    placeholder: String = "Enter your password",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    GroomoraOutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        isError = isError,
        errorMessage = errorMessage,
        enabled = enabled,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            TextButton(
                onClick = { isPasswordVisible = !isPasswordVisible },
                modifier = Modifier
                    .defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                    .semantics {
                        role = Role.Button
                        contentDescription = if (isPasswordVisible) "Hide password" else "Show password"
                    }
            ) {
                Text(
                    text = if (isPasswordVisible) "Hide" else "Show",
                    color = HoneyAmber,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = modifier
    )
}

@Composable
fun GroomoraSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String = "Search for services, salons, barbers...",
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector = Icons.Default.Search,
    showClearButton: Boolean = true,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = if (onClick != null) {
            modifier
                .fillMaxWidth()
                .clickable(
                    role = Role.Button,
                    onClickLabel = placeholder,
                    onClick = onClick
                )
        } else {
            modifier.fillMaxWidth()
        },
        enabled = enabled && onClick == null,
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MutedText
            )
        },
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = "Search",
                tint = MutedText
            )
        },
        trailingIcon = {
            if (showClearButton && query.isNotEmpty()) {
                IconButton(
                    onClick = { onQueryChange("") },
                    modifier = Modifier.defaultMinSize(minWidth = 48.dp, minHeight = 48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search text",
                        tint = MutedText
                    )
                }
            }
        },
        singleLine = true,
        shape = CircleShape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedBorderColor = HoneyAmber,
            unfocusedBorderColor = BorderGray,
            disabledBorderColor = BorderGray
        )
    )
}
