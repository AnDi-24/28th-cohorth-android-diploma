package ru.practicum.android.diploma.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.presentation.VacancySearchUiState
import ru.practicum.android.diploma.ui.compose.components.SearchField
import ru.practicum.android.diploma.ui.compose.components.VacancyListItem

@Composable
fun MainScreen(
    viewModel: SearchViewModel, navController: NavController
) {
    val uiState by viewModel.uiState

    Column(modifier = Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = viewModel.query,
            onValueChange = { viewModel.query = it },
            label = { "Поиск вакансии" },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )


        Button(
            onClick = { viewModel.searchVacancies() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text("Найти")
        }


        when (uiState) {
            VacancySearchUiState.Idle -> {}
            VacancySearchUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is VacancySearchUiState.Success -> {
                val vacancies = (uiState as VacancySearchUiState.Success).vacancies
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(vacancies) { vacancy ->
                        VacancyListItem(
                            vacancy,
                            onClick = {navController.navigate(FILTER)}
                        )
                        Divider()
                    }
                }
            }
            VacancySearchUiState.Empty -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Вакансий не найдено")
                }
            }
            is VacancySearchUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        (uiState as VacancySearchUiState.Error).message,
                        color = Color.Red
                    )
                }
            }
        }
    }
}

