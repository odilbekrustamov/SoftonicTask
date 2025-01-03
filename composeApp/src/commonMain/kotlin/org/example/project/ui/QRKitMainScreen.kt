package org.example.project.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import network.chaintech.sdpcomposemultiplatform.sdp
import network.chaintech.sdpcomposemultiplatform.ssp


@Composable
fun QRKitMainScreen(onNavigate: (String) -> Unit) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .background(Color.White)
                .padding(16.sdp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
                    .padding(top = 20.sdp),
                horizontalArrangement = Arrangement.spacedBy(12.sdp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .background(
                            color = Color(0xFF0B2447),
                            shape = RoundedCornerShape(18.sdp)
                        )
                        .clickable {
                            onNavigate(AppScreen.QRGenerator.route)
                        }
                ) {

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.sdp, top = 12.sdp)
                    ) {
                        Text(
                            text = "QR Code",
                            modifier = Modifier,
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ),
                            fontSize = 16.ssp
                        )

                        Text(
                            text = "Generator",
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Normal
                            ),
                            fontSize = 25.ssp
                        )

                    }
                }

                Box(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                        .background(
                            color = Color(0xFF327D6A),
                            shape = RoundedCornerShape(18.sdp)
                        )
                        .clickable {
                            onNavigate(AppScreen.QRScanner.route)
                        }
                ) {

                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 16.sdp, top = 12.sdp)
                    ) {
                        Text(
                            text = "QR Code",
                            modifier = Modifier,
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ),
                            fontSize = 16.ssp
                        )

                        Text(
                            text = "Scanner",
                            modifier = Modifier
                                .fillMaxWidth(),
                            style = TextStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Normal
                            ),
                            fontSize = 25.ssp
                        )

                    }
                }
            }
        }
    }
}



