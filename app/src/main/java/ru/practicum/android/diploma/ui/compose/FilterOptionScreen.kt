package ru.practicum.android.diploma.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.practicum.android.diploma.ui.theme.Spacing16

@Composable
fun FilterOptionScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(Spacing16),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Фильтр по Отрасли или Месту работы")
    }
}
