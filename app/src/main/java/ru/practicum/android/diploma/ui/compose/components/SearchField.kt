package ru.practicum.android.diploma.ui.compose.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.ui.compose.FILTER
import ru.practicum.android.diploma.ui.compose.MAIN
import ru.practicum.android.diploma.ui.compose.OPTION
import ru.practicum.android.diploma.ui.theme.InputFieldHeight
import ru.practicum.android.diploma.ui.theme.Spacing8

private const val SEARCH_DEBOUNCE_DELAY = 2000L

@Composable
fun SearchField(
    label: String,
    viewModel: SearchViewModel,
    screenTag: String,
    // Новые параметры для разделения запросов
    query: String = "", // Текущее значение поля (передается извне)
    onQueryChange: (String) -> Unit = { viewModel.setVacancySearchQuery(it) }, // Обработчик изменения
    onSearch: (String) -> Unit = { viewModel.searchVacancies(it) } // Обработчик поиска
) {

    // Используем переданное значение или получаем из соответствующего StateFlow
    val queryState = when (screenTag) {
        MAIN -> viewModel.vacancySearchQuery.collectAsStateWithLifecycle()
        OPTION -> viewModel.industrySearchQuery.collectAsStateWithLifecycle()
        else -> mutableStateOf(query) // Для других экранов используем переданное значение
    }

    // Локальное состояние поля ввода
    var inputValue by rememberSaveable {
        mutableStateOf(if (query.isNotEmpty()) query else queryState.value)
    }

    // Для отслеживания, нужно ли выполнять поиск
    var shouldSearch by rememberSaveable { mutableStateOf(false) }

    // Синхронизируем с внешним состоянием
    LaunchedEffect(queryState.value) {
        if (queryState.value != inputValue) {
            inputValue = queryState.value
        }
    }

    // Синхронизируем с переданным query
    LaunchedEffect(query) {
        if (query.isNotEmpty() && query != inputValue) {
            inputValue = query
        }
    }

    // Дебаунс для поиска - только для MAIN экрана
    LaunchedEffect(inputValue) {
        if (screenTag == MAIN && inputValue.isNotEmpty()) {
            // Сбрасываем таймер при каждом изменении
            shouldSearch = false
            delay(SEARCH_DEBOUNCE_DELAY)
            shouldSearch = true
            // Вызываем поиск после задержки
            onSearch(inputValue)
        }
    }

//    // Дебаунс для поиска
//    LaunchedEffect(inputValue) {
//        if (inputValue != queryState.value && screenTag != FILTER) {
//            delay(SEARCH_DEBOUNCE_DELAY)
//            // Вызываем обработчик изменения запроса
//            onQueryChange(inputValue)
//            if (inputValue.isNotEmpty()) {
//                behaviorSelector(inputValue, screenTag, viewModel, onSearch)
//            } else {
//                viewModel.clearSearch()
//            }
//        }
//    }

    TextField(
        maxLines = 1,
        value = inputValue,
        onValueChange = { newValue ->
            inputValue = newValue
            onQueryChange(newValue)

            // Для OPTION экрана фильтруем сразу
            if (screenTag == OPTION) {
                viewModel.searchIndustries(newValue)
            }

            // Для MAIN экрана - очищаем если пусто
            if (screenTag == MAIN && newValue.isEmpty()) {
                viewModel.clearSearch()
            }
        },
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .height(InputFieldHeight)
            .fillMaxWidth()
            .padding(vertical = Spacing8),
        placeholder = { Text(label) },
        label = shouldBeLabel(screenTag),
        trailingIcon = {
            if (inputValue.isNotEmpty()) {
                IconButton(
                    onClick = {
                        inputValue = ""
                        // Очищаем соответствующий запрос
                        onQueryChange("")
                        when (screenTag) {
                            MAIN -> viewModel.clearSearch()
                            OPTION -> viewModel.searchIndustries("")
                        }
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close),
                        tint = colorResource(R.color.black_universal),
                        contentDescription = "Очистить"
                    )
                }
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    tint = colorResource(R.color.black_universal),
                    contentDescription = "Поиск"
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(R.color.light_grey),
            unfocusedContainerColor = colorResource(R.color.light_grey),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

private fun shouldBeLabel(tag: String): (@Composable () -> Unit)? {
    return if (tag == FILTER) {
        { Text("Ожидаемая зарплата") }
    } else {
        null
    }
}

private fun behaviorSelector(
    inputValue: String,
    screenTag: String,
    viewModel: SearchViewModel,
    onSearch: (String) -> Unit = { viewModel.searchVacancies(it) }
) {
    when (screenTag) {
        MAIN -> onSearch(inputValue) // Используем переданный обработчик поиска
        OPTION -> viewModel.searchIndustries(inputValue)
        FILTER -> TODO()
    }
}
