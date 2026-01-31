package ru.practicum.android.diploma.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.presentation.VacancySearchUiState
import ru.practicum.android.diploma.ui.compose.components.NoInternetEmptyState
import ru.practicum.android.diploma.ui.compose.components.SearchField
import ru.practicum.android.diploma.ui.compose.components.SearchScreenEmptyState
import ru.practicum.android.diploma.ui.compose.components.VacancyFailedState
import ru.practicum.android.diploma.ui.compose.components.VacancyListItem
import ru.practicum.android.diploma.ui.theme.Typography


@Composable
fun MainScreen(
    viewModel: SearchViewModel,
    navController: NavController
) {
    val colors = MaterialTheme.colorScheme
    val uiState by viewModel.uiState
    val resources = LocalContext.current.resources

    Column(modifier = Modifier.padding(16.dp)) {
        SearchField(
            label = stringResource(R.string.request_placeholder),
            viewModel = viewModel
        )

        when (uiState) {
            VacancySearchUiState.Idle -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    SearchScreenEmptyState()
                }
            }

            VacancySearchUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is VacancySearchUiState.Success -> {
                val vacancies = (uiState as VacancySearchUiState.Success).vacancies
                val totalFound = (uiState as VacancySearchUiState.Success).totalFound
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 40.dp)
                    ) {
                        items(vacancies) { vacancy ->
                            VacancyListItem(
                                vacancy,
                                onClick = { navController.navigate("vacancy/${vacancy.id}") }
                            )
                        }
                    }
                    if (vacancies.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(27.dp)
                                    .width(184.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .border(
                                        width = 1.dp,
                                        color = colors.primary,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .background(colors.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Найдено ${
                                        resources.getQuantityString(
                                            R.plurals.vacancies_count,
                                            totalFound,
                                            totalFound
                                        )
                                    }",
                                    style = Typography.bodyMedium,
                                    color = colors.onPrimary
                                )
                            }
                        }
                    }
                }
            }

            is VacancySearchUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    VacancyFailedState()
                }
            }

            is VacancySearchUiState.UnknownError -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    VacancyFailedState()
                }
            }

            is VacancySearchUiState.NetworkError -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    NoInternetEmptyState()
                }
            }
        }
    }
}

