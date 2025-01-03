package org.example.project.qrscanner

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitView
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGAffineTransformMakeScale
import platform.CoreImage.CIContext
import platform.CoreImage.CIFilter
import platform.CoreImage.createCGImage
import platform.CoreImage.filterWithName
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.Foundation.dataUsingEncoding
import platform.Foundation.setValue
import platform.UIKit.UIImage
import platform.UIKit.UIImageView
import platform.UIKit.UIViewContentMode

@Composable
actual fun QrCodeGenerator(modifier: Modifier, data: String) {
    UIKitView(
        factory = {
            val imageView = UIImageView().apply {
                contentMode = UIViewContentMode.UIViewContentModeScaleAspectFit
            }
            imageView
        },
        update = { imageView ->
            if (data.isNotEmpty()) {
                imageView.image = generateQrCode(data)
            } else {
                imageView.image = null
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp)
    )
}


@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun generateQrCode(data: String): UIImage? {
    val nsData = NSString.create(string = data).dataUsingEncoding(NSUTF8StringEncoding)
    val qrFilter = CIFilter.filterWithName("CIQRCodeGenerator") ?: return null

    qrFilter.setValue(nsData, forKey = "inputMessage")
    qrFilter.setValue("Q", forKey = "inputCorrectionLevel")

    val ciImage = qrFilter.outputImage ?: return null
    val transform = CGAffineTransformMakeScale(10.0, 10.0)
    val scaledImage = ciImage.imageByApplyingTransform(transform)

    val ciContext = CIContext.context()
    val cgImage = ciContext.createCGImage(scaledImage, fromRect = scaledImage.extent)
    return UIImage.imageWithCGImage(cgImage)
}

