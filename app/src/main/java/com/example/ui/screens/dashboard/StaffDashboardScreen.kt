package com.example.first.screens.dashboard

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.asImageBitmap
import com.example.first.utils.generateQRCode

@Composable
fun StaffDashboardScreen() {

    var destination by remember { mutableStateOf("") }
    var fare by remember { mutableStateOf("") }
    var qrBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Title
        Text(
            text = "🚌 Staff Panel",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Destination Input
        OutlinedTextField(
            value = destination,
            onValueChange = { destination = it },
            label = { Text("Enter Destination") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Fare Input
        OutlinedTextField(
            value = fare,
            onValueChange = { fare = it },
            label = { Text("Enter Fare") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Generate QR Button
        Button(
            onClick = {
                val data = "Bus:42|From:City|To:$destination|Fare:$fare"
                qrBitmap = generateQRCode(data)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Generate QR")
        }

        Spacer(modifier = Modifier.height(30.dp))

        // QR + Info Centered
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            qrBitmap?.let {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "QR Code",
                        modifier = Modifier.size(250.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Destination: $destination",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Fare: ₹$fare",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}