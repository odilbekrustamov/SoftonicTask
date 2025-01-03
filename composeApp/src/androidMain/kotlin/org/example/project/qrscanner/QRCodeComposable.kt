package org.example.project.qrscanner

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


@Composable
fun QRCodeComposable(
    modifier: Modifier,
    flashlightOn: Boolean,
    onCompletion: (String) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    var camera by remember { mutableStateOf<Camera?>(null) }

    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        }
    )

    LaunchedEffect(Unit) {
        launcher.launch(Manifest.permission.CAMERA)
    }

    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
        ProcessCameraProvider.getInstance(context)

    DisposableEffect(cameraProviderFuture) {
        onDispose {
            cameraProviderFuture.get().unbindAll()
        }
    }

    if (hasCamPermission) {

        val lensCamera = CameraSelector.LENS_FACING_BACK

        Box(modifier = modifier) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { androidViewContext ->
                    PreviewView(androidViewContext).apply {
                        this.scaleType = PreviewView.ScaleType.FILL_CENTER
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    }
                },
                update = { previewView ->
                    val cameraSelector: CameraSelector = CameraSelector.Builder()
                        .requireLensFacing(lensCamera)
                        .build()
                    val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()

                    cameraProviderFuture.addListener({
                        preview = Preview.Builder()
                            .build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }
                        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

                        val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor, QRCodeAnalyzer { result ->
                                    result?.let { onCompletion(it) }
                                })
                            }

                        val imageCapture = ImageCapture.Builder()
                            .setFlashMode(ImageCapture.FLASH_MODE_ON)
                            .build()

                        try {
                            cameraProvider.unbindAll()
                            camera = cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageCapture,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e("qr code", e.message ?: "")
                        }

                        if (camera?.cameraInfo?.hasFlashUnit() == true) {
                            camera?.cameraControl?.enableTorch(flashlightOn)
                        }
                    }, ContextCompat.getMainExecutor(context))
                }
            )

            OverlayView()
        }
    }
}

@Composable
fun OverlayView(
) {
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
