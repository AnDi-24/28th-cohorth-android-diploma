package ru.practicum.android.diploma.ui.compose

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.presentation.FavoriteViewModel

@Composable
fun MainScreen(navController: NavController) {


    val viewModel: FavoriteViewModel = koinViewModel()
    val vacancies by viewModel.vacanciesFlow.collectAsStateWithLifecycle()
    Log.d("VacancyScreen ROOM LOGIC", "Количество вакансий: ${vacancies.size}")


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Экран 1 — Главная")
        Button(onClick = {
            navController.navigate(VACANCY)
        }) {
            Text("Вакансия")
        }
        Button(onClick = {
            navController.navigate(FILTER)
        }) {
            Text("Фильтры")
        }
    }
}
