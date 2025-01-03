package org.example.project.qrscanner


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusNotDetermined
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceDiscoverySession
import platform.AVFoundation.AVCaptureDevicePositionBack
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInDualCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInDualWideCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInDuoCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInUltraWideCamera
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInWideAngleCamera
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType

private sealed interface CameraAccess {
    data object Undefined : CameraAccess
    data object Denied : CameraAccess
    data object Authorized : CameraAccess
}

private val deviceTypes = listOf(
    AVCaptureDeviceTypeBuiltInWideAngleCamera,
    AVCaptureDeviceTypeBuiltInDualWideCamera,
    AVCaptureDeviceTypeBuiltInDualCamera,
    AVCaptureDeviceTypeBuiltInUltraWideCamera,
    AVCaptureDeviceTypeBuiltInDuoCamera
)

@Composable
actual fun QrCodeScanner(
    modifier: Modifier,
    flashlightOn: Boolean,
    onCompletion: (String) -> Unit,
) {
    var cameraAccess: CameraAccess by remember { mutableStateOf(CameraAccess.Undefined) }
    LaunchedEffect(Unit) {
        when (AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)) {
            AVAuthorizationStatusAuthorized -> {
                cameraAccess = CameraAccess.Authorized
            }

            AVAuthorizationStatusDenied, AVAuthorizationStatusRestricted -> {
                cameraAccess = CameraAccess.Denied
            }

            AVAuthorizationStatusNotDetermined -> {
                AVCaptureDevice.requestAccessForMediaType(
                    mediaType = AVMediaTypeVideo
                ) { success ->
                    cameraAccess = if (success) CameraAccess.Authorized else CameraAccess.Denied
                }
            }
        }
    }
    Box(
        modifier.fillMaxSize().background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        when (cameraAccess) {
            CameraAccess.Undefined -> {
                // Waiting for the user to accept permission
            }

            CameraAccess.Denied -> {
                Text("Camera access denied", color = Color.White)
            }

            CameraAccess.Authorized -> {
                AuthorizedCamera(flashlightOn = flashlightOn, onCompletion)
            }
        }
    }
}

@Composable
private fun BoxScope.AuthorizedCamera(
    flashlightOn: Boolean,
    onQrCodeScanned: (String) -> Unit,
) {
    val cameraPosition = AVCaptureDevicePositionBack

    val camera: AVCaptureDevice? = remember {
        AVCaptureDeviceDiscoverySession.discoverySessionWithDeviceTypes(
            deviceTypes = deviceTypes,
            mediaType = AVMediaTypeVideo,
            position = cameraPosition
        ).devices.firstOrNull() as? AVCaptureDevice
    }

    if (camera != null) {
        RealDeviceCamera(
            camera = camera,
            flashlightOn = flashlightOn,
            onQrCodeScanned = onQrCodeScanned
        )
    }

    OverlayView()
}

@Composable
fun OverlayView() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithContent {

                drawContent()

                val scanAreaWidth = size.width * 0.65f
                val scanAreaHeight = scanAreaWidth

                val left = (size.width - scanAreaWidth) / 2
                val top = (size.height - scanAreaHeight) / 3

                drawRect(
                    color = Color(0x88000000),
                    topLeft = Offset.Zero,
                    size = Size(size.width, top)
                )
                drawRect(
                    color = Color(0x88000000),
                    topLeft = Offset(left + scanAreaWidth, top),
                    size = Size(size.width - (left + scanAreaWidth), scanAreaHeight)
                )
                drawRect(
                    color = Color(0x88000000),
                    topLeft = Offset(0f, top + scanAreaHeight),
                    size = Size(size.width, size.height - (top + scanAreaHeight))
                )
                drawRect(
                    color = Color(0x88000000),
                    topLeft = Offset(0f, top),
                    size = Size(left, scanAreaHeight)
                )

                val borderStroke = 2.dp.toPx()
                drawRect(
                    color = Color.White,
                    topLeft = Offset(left, top),
                    size = Size(scanAreaWidth, scanAreaHeight),
                    style = Stroke(width = borderStroke)
                )
            }
    )
}
