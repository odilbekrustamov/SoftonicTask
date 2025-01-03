package org.example.project

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import org.example.project.ui.QRKitNav
import org.jetbrains.compose.ui.tooling.preview.Preview



@Composable
@Preview
fun App() {
    Scaffold(
    ) {
        QRKitNav()
    }
}