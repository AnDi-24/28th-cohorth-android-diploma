package ru.practicum.android.diploma.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.presentation.FavoriteUiState
import ru.practicum.android.diploma.presentation.FavoriteViewModel
import ru.practicum.android.diploma.ui.compose.components.FavoritesEmptyState
import ru.practicum.android.diploma.ui.compose.components.VacancyFailedState
import ru.practicum.android.diploma.ui.compose.components.VacancyListItem
import ru.practicum.android.diploma.ui.theme.Spacing16
import ru.practicum.android.diploma.ui.theme.Spacing4

@Composable
fun FavoriteScreen(navController: NavController) {
    val viewModel: FavoriteViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    when (uiState) {
        is FavoriteUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is FavoriteUiState.Empty -> {
            FavoritesEmptyState()
        }

        is FavoriteUiState.Success -> {
            FavoriteVacanciesList(
                vacancies = (uiState as FavoriteUiState.Success).vacancies,
                navController = navController
            )
        }

        is FavoriteUiState.Error -> {
            VacancyFailedState()
        }
    }
}

@Composable
private fun FavoriteVacanciesList(
    vacancies: List<VacancyDetailsModel>,
    navController: NavController
) {
    if (vacancies.isEmpty()) {
        FavoritesEmptyState()
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing16)
        ) {
            items(
                items = vacancies,
                key = { vacancy -> vacancy.id }
            ) { vacancy ->
                VacancyListItem(
                    vacancy = vacancy,
                    onClick = {
                        navController.navigate("$VACANCY/${vacancy.id}")
                    },
                    modifier = Modifier.padding(vertical = Spacing4)
                )
            }
        }
    }
}
