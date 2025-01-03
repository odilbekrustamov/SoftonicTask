package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CopyAll
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.launch
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp
import org.example.project.qrscanner.QrScanner

@Composable
fun QrScannerView(onNavigate: (String) -> Unit) {
    var qrCodeURL by remember { mutableStateOf("") }
    var flashlightOn by remember { mutableStateOf(false) }
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .background(Color(0xFF1D1C22))
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Box(
                modifier =  Modifier.fillMaxSize() ,
                contentAlignment = Alignment.Center
            ) {
                QrScanner(
                    modifier = Modifier,
                    flashlightOn = flashlightOn,
                    onCompletion = {
                        qrCodeURL = it
                    },
                )
            }

            Column(
                modifier = Modifier
                    .padding(start = 20.sdp, end = 20.sdp, top = 20.sdp, bottom = 150.sdp)
                    .align(Alignment.BottomCenter)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFF9F9F9),
                            shape = RoundedCornerShape(25.sdp)
                        )
                        .height(35.sdp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 4.sdp, horizontal = 16.sdp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.sdp)
                    ) {
                        Icon(
                            imageVector = if (flashlightOn) Icons.Filled.FlashOn else Icons.Filled.FlashOff,
                            "flash",
                            modifier = Modifier
                                .size(20.sdp)
                                .clickable {
                                    flashlightOn = !flashlightOn
                                }
                        )
                    }
                }
            }

            if (qrCodeURL.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 14.sdp)
                        .padding(bottom = 22.sdp)
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = qrCodeURL,
                        modifier = Modifier
                            .padding(end = 8.sdp)
                            .weight(1f),
                        fontSize = 12.ssp,
                        color = Color.White,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis
                    )

                    Icon(
                        Icons.Filled.CopyAll,
                        "CopyAll",
                        modifier = Modifier.size(20.sdp).clickable {
                            clipboardManager.setText(AnnotatedString((qrCodeURL)))
                            scope.launch {
                                snackbarHostState.showSnackbar(message = "Copied")
                            }
                        },
                        tint = Color.White
                    )
                }
            }

            Column {
                Row(
                    modifier = Modifier.padding(start = 14.sdp, end = 14.sdp, top = 20.sdp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "QRScanner",
                        modifier = Modifier.weight(1f),
                        fontSize = 18.ssp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    )

                    Icon(
                        Icons.Filled.Close,
                        "close",
                        modifier = Modifier.size(20.sdp).clickable {
                            onNavigate(AppConstants.BACK_CLICK_ROUTE)
                        },
                        tint = Color.White
                    )
                }
            }
        }
    }
}