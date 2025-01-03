package org.example.project.qrscanner

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun QrGenerator(
    modifier: Modifier,
    data: String
) {

    QrCodeGenerator(modifier, data)
}