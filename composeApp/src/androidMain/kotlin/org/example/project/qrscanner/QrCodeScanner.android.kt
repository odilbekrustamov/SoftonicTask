package org.example.project.qrscanner


import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
actual fun QrCodeScanner(
    modifier: Modifier,
    flashlightOn: Boolean,
    onCompletion: (String) -> Unit,
) {
    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted)
            cameraPermissionState.launchPermissionRequest()
    }

    if (cameraPermissionState.status.isGranted) {
        QRCodeComposable(
            modifier = modifier,
            flashlightOn = flashlightOn,
            onCompletion = onCompletion,
        )
    }
}





