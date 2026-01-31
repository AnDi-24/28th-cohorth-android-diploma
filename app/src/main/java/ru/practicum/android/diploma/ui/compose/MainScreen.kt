package ru.practicum.android.diploma.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.presentation.VacancySearchUiState
import ru.practicum.android.diploma.ui.compose.components.SearchField
import ru.practicum.android.diploma.ui.compose.components.VacancyListItem
import ru.practicum.android.diploma.ui.theme.Typography


@Composable
fun MainScreen(
    viewModel: SearchViewModel,
    navController: NavController
) {
    val colors = MaterialTheme.colorScheme
    val uiState by viewModel.uiState

    Column(modifier = Modifier.padding(16.dp)) {
        SearchField(
            label = "Поиск вакансии",
            viewModel = viewModel
        )

        when (uiState) {
            VacancySearchUiState.Idle -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Введите запрос для поиска вакансий",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
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
                                    text = "Найдено ${vacancies.size} вакансий",
                                    style = Typography.bodyMedium,
                                    color = colors.onPrimary
                                )
                            }
                        }
                    }
                }
            }

            VacancySearchUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "По вашему запросу ничего не найдено",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            is VacancySearchUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = (uiState as VacancySearchUiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

