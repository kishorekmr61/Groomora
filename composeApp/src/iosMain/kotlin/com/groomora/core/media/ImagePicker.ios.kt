package com.groomora.core.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

class IosImagePicker : ImagePicker {
    override fun pickImage() {
        // To implement real iOS picking, we would use UIImagePickerController
        // which requires handling delegates and platform UI.
        println("[iOS] Image picking not yet fully bridged")
    }

    override fun takePhoto() {
        println("[iOS] Camera taking not yet fully bridged")
    }
}

@Composable
actual fun rememberImagePicker(onImagePicked: (String) -> Unit): ImagePicker {
    return remember { IosImagePicker() }
}
