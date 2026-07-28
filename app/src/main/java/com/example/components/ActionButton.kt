package com.example.first.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun ActionButton(text: String, onClick: () -> Unit) {

    Button(onClick = onClick) {
        Text(text)
    }
}