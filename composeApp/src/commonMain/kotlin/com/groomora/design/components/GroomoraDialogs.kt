package com.groomora.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.groomora.design.*

data class GalleryPhoto(
    val title: String,
    val imageUrl: String,
    val description: String? = null,
    val category: String? = null
)

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

@Composable
fun GroomoraPhotoViewerDialog(
    photos: List<GalleryPhoto>,
    initialIndex: Int = 0,
    onDismissRequest: () -> Unit
) {
    if (photos.isEmpty()) return
    var currentIndex by remember { mutableIntStateOf(initialIndex.coerceIn(0, photos.size - 1)) }
    val currentPhoto = photos[currentIndex]

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.95f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
            ) {
                // 1. Top Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentPhoto.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 18.sp
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "Photo ${currentIndex + 1} of ${photos.size}${if (!currentPhoto.category.isNullOrBlank()) " • ${currentPhoto.category}" else ""}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Champagne
                        )
                    }

                    IconButton(
                        onClick = onDismissRequest,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Photo Viewer",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                // 2. High-Res Photo Area
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    GroomoraImage(
                        url = currentPhoto.imageUrl,
                        contentDescription = currentPhoto.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.88f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Charcoal),
                        contentScale = ContentScale.Fit
                    )

                    // Navigation: Previous Button
                    if (photos.size > 1 && currentIndex > 0) {
                        IconButton(
                            onClick = { currentIndex-- },
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(start = 8.dp)
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.65f))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Previous Photo",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Navigation: Next Button
                    if (photos.size > 1 && currentIndex < photos.size - 1) {
                        IconButton(
                            onClick = { currentIndex++ },
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .padding(end = 8.dp)
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.65f))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Next Photo",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                // 3. Caption or description
                if (!currentPhoto.description.isNullOrBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = currentPhoto.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // 4. Thumbnail Dots
                if (photos.size > 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 20.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        photos.forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(if (index == currentIndex) 10.dp else 6.dp)
                                    .clip(CircleShape)
                                    .background(if (index == currentIndex) HoneyAmber else Color.White.copy(alpha = 0.4f))
                                    .clickable { currentIndex = index }
                            )
                        }
                    }
                }
            }
        }
    }
}
