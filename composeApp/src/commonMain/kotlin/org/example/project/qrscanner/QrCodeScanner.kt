package org.example.project.qrscanner

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
expect fun QrCodeScanner(modifier: Modifier, flashlightOn: Boolean, onCompletion: (String) -> Unit)

