package org.example.project.qrscanner


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun QrScanner(
    modifier: Modifier,
    flashlightOn: Boolean,
    onCompletion: (String) -> Unit,
) {

    QrCodeScanner(modifier, flashlightOn, onCompletion)
}