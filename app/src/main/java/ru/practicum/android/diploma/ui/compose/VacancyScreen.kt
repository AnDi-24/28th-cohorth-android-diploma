package ru.practicum.android.diploma.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.presentation.VacancyDetailsViewModel
import ru.practicum.android.diploma.ui.compose.components.NoInternetEmptyState
import ru.practicum.android.diploma.ui.compose.components.VacancyDetails
import ru.practicum.android.diploma.ui.compose.components.VacancyNotFoundEmptyState

@Composable
fun VacancyScreen(vacancyId: String?) {
    val viewModel: VacancyDetailsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val currentState = uiState

    LaunchedEffect(vacancyId) {
        if (vacancyId != null) {
            viewModel.searchVacancyDetails(vacancyId)
        }
    }

    when (currentState) {
        VacancyDetailsViewModel.VacancyUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        VacancyDetailsViewModel.VacancyUiState.Idle -> {
        }

        is VacancyDetailsViewModel.VacancyUiState.Success -> {
            val vacancy = currentState.vacancy
            VacancyDetails(vacancy)
        }

        is VacancyDetailsViewModel.VacancyUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currentState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is VacancyDetailsViewModel.VacancyUiState.NoInternet -> {
            NoInternetEmptyState()
        }

        is VacancyDetailsViewModel.VacancyUiState.VacancyNotFound -> {
            VacancyNotFoundEmptyState()
        }
    }
}

