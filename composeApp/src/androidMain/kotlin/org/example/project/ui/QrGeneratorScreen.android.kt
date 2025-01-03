package org.example.project.ui


import androidx.compose.runtime.Composable
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.dp


@Composable
actual fun QrGeneratorContainer(data: String) {
    if (data.isNotEmpty()) {
        val bitMatrix: BitMatrix = generateQrCode(data)
        val bitmap: Bitmap = bitmapFromMatrix(bitMatrix)

        val painter = BitmapPainter(bitmap.asImageBitmap())

        Image(
            painter = painter,
            contentDescription = "QR Code",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(16.dp)
        )
    } else {
        Text("Please enter valid data for the QR code.")
    }
}

fun generateQrCode(data: String): BitMatrix {
    if (data.isEmpty()) {
        throw IllegalArgumentException("Data for QR code cannot be empty")
    }

    val writer = MultiFormatWriter()
    return writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
}

fun bitmapFromMatrix(matrix: BitMatrix): Bitmap {
    val width = matrix.width
    val height = matrix.height
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

    for (x in 0 until width) {
        for (y in 0 until height) {
            bitmap.setPixel(x, y, if (matrix.get(x, y)) Color.BLACK else Color.WHITE)
        }
    }
    return bitmap
}


