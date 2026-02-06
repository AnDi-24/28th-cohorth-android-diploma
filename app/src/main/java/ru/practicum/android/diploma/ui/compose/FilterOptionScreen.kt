package ru.practicum.android.diploma.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.network.models.industries.IndustryModel
import ru.practicum.android.diploma.presentation.FilterOptionViewModel
import ru.practicum.android.diploma.presentation.FilterViewModel
import ru.practicum.android.diploma.presentation.models.IndustryUiState
import ru.practicum.android.diploma.ui.compose.components.GetListFailedEmptyState
import ru.practicum.android.diploma.ui.compose.components.IndustrySearchField
import ru.practicum.android.diploma.ui.compose.components.PositiveButton
import ru.practicum.android.diploma.ui.theme.Typography

@Composable
fun FilterOptionScreen(
    viewModel: FilterOptionViewModel,
    filterViewModel: FilterViewModel,
    navController: NavController
) {
    // Получаем состояние UI
    val filterUiState = viewModel.filterUiState.value
    // Получаем выбранную отрасль
    val selectedIndustry by viewModel.selectedIndustry.collectAsState()
    val filterState by filterViewModel.filterState.collectAsState()

    LaunchedEffect(Unit) {
        // Вызываем метод в ViewModel для проверки актуальных данных
        viewModel.checkAndUpdateFromSharedPrefs()
    }

    LaunchedEffect(filterState.industry) {
        // Если в фильтрах есть отрасль, но в viewModel она не установлена
        if (filterState.industry.isNotEmpty() && filterState.industryName.isNotEmpty()) {
            val currentSelected = selectedIndustry?.id
            if (currentSelected != filterState.industry) {
                // Создаем IndustryModel из сохраненных данных
                val savedIndustry = IndustryModel(
                    id = filterState.industry,
                    name = filterState.industryName
                )
                viewModel.selectedIndustry(savedIndustry)
            }
        } else if (filterState.industry.isEmpty() && selectedIndustry != null) {
            // Если в фильтрах отрасль очищена, но в viewModel еще есть выбранная
            // Сбрасываем выбранную отрасль и поиск
            viewModel.setSearchQuery("")
            viewModel.searchIndustries("")
        }
    }

    // Находим индекс выбранной отрасли для подсветки
    val selectedIndex = remember(filterUiState, selectedIndustry) {
        when (val state = filterUiState) {
            is IndustryUiState.OnSelect -> {
                // Находим индекс выбранной отрасли по ID
                state.industries.indexOfFirst { it.id == selectedIndustry?.id }
            }
            else -> -1
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Поле поиска отраслей
        Box(modifier = Modifier.padding(16.dp)) {
            IndustrySearchField(
                label = stringResource(R.string.request_placeholder),
                viewModel = viewModel
            )
        }

        // Список отраслей
        when (filterUiState) {
            is IndustryUiState.OnSelect -> {
                LazyColumn(
                    modifier = Modifier.weight(1f)
                ) {
                    itemsIndexed(filterUiState.industries) { index, item ->
                        // Проверяем, выбрана ли текущая отрасль
                        val isItemSelected = index == selectedIndex

                        IndustriesItem(
                            item = item,
                            isSelected = isItemSelected,
                            onSelect = {
                                // Выбираем отрасль
                                viewModel.selectedIndustry(item)
                                // Сохраняем в фильтры
                                filterViewModel.updateIndustry(item.id)
                                filterViewModel.updateIndustryName(item.name)
                            }
                        )
                    }
                }
            }

            is IndustryUiState.Selected -> {
                // Если отрасль выбрана, показываем ее и кнопку подтверждения
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Выбранная отрасль
                    IndustriesItem(
                        item = filterUiState.industry,
                        isSelected = true,
                        onSelect = {
                            // При повторном клике очищаем поиск
                            viewModel.searchIndustries("")
                        }
                    )

                    // Кнопка подтверждения выбора
                    PositiveButton(
                        text = R.string.select,
                        onClick = {
                            // Возвращаемся на экран фильтров
                            navController.popBackStack()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            // Состояния ошибок
            is IndustryUiState.Empty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    GetListFailedEmptyState()
                }
            }

            // На случай добавления других состояний
            else -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    GetListFailedEmptyState()
                }
            }
        }
    }
}

@Composable
fun IndustriesItem(
    item: IndustryModel,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clickable { onSelect() }
            .padding(start = 2.dp, end = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            style = Typography.bodyMedium,
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        )
        RadioButton(
            selected = isSelected,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary,
                unselectedColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}
