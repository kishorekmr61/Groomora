package com.groomora.design.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.groomora.design.*

@Composable
fun GroomoraConfirmationDialog(
    title: String,
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier,
    message: String? = null,
    confirmButtonText: String = "Confirm",
    dismissButtonText: String = "Cancel",
    isDestructive: Boolean = false,
    content: (@Composable ColumnScope.() -> Unit)? = null
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        title = {
            GroomoraTitle(text = title)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                if (message != null) {
                    GroomoraBody(text = message, color = MutedText)
                }
                content?.invoke(this)
            }
        },
        confirmButton = {
            if (isDestructive) {
                Button(
                    onClick = onConfirm,
                    shape = MaterialTheme.shapes.small,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorRed,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(confirmButtonText, fontWeight = FontWeight.Bold)
                }
            } else {
                GroomoraPrimaryButton(
                    text = confirmButtonText,
                    onClick = onConfirm,
                    height = 38.dp
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(dismissButtonText, color = MutedText, fontWeight = FontWeight.Medium)
            }
        },
        shape = MaterialTheme.shapes.large,
        containerColor = Color.White
    )
}
