package com.groomora.core.media

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

class AndroidImagePicker(
    private val galleryLauncher: androidx.activity.result.ActivityResultLauncher<PickVisualMediaRequest>,
    private val cameraLauncher: androidx.activity.result.ActivityResultLauncher<Uri>,
    private val tempUri: Uri
) : ImagePicker {
    override fun pickImage() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun takePhoto() {
        cameraLauncher.launch(tempUri)
    }
}

@Composable
actual fun rememberImagePicker(onImagePicked: (String) -> Unit): ImagePicker {
    val context = LocalContext.current
    
    // Create a stable temp file and URI
    val tempFile = remember {
        File(context.cacheDir, "temp_profile_image.jpg")
    }
    
    val tempUri = remember {
        FileProvider.getUriForFile(
            context,
            "com.groomora.app.fileprovider",
            tempFile
        )
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { onImagePicked(it.toString()) }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            onImagePicked(tempUri.toString())
        }
    }

    return remember(galleryLauncher, cameraLauncher, tempUri) {
        AndroidImagePicker(galleryLauncher, cameraLauncher, tempUri)
    }
}
