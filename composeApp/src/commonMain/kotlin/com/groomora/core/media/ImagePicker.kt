package com.groomora.core.media

import androidx.compose.runtime.Composable

interface ImagePicker {
    fun pickImage()
    fun takePhoto()
}

@Composable
expect fun rememberImagePicker(onImagePicked: (String) -> Unit): ImagePicker
