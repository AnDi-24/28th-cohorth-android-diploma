package ru.practicum.android.diploma.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MainScreen() {
    Text(
        text = "Экран 1 — Главная",
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        fontSize = 20.sp,
        textAlign = TextAlign.Center
    )
}
