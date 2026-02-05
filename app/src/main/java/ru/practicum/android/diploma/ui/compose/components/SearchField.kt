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
) {
    val queryState = viewModel.searchQuery.collectAsStateWithLifecycle()
    var inputValue by rememberSaveable { mutableStateOf(queryState.value) }

    LaunchedEffect(queryState.value) {
        if (queryState.value != inputValue) {
            inputValue = queryState.value
        }
    }

    LaunchedEffect(inputValue) {
        if (inputValue != queryState.value) {
            delay(SEARCH_DEBOUNCE_DELAY)
            viewModel.setSearchQuery(inputValue)
            if (inputValue.isNotEmpty()) {
                behaviorSelector(inputValue, screenTag, viewModel)
            } else {
                viewModel.clearSearch()
            }
        }
    }

    TextField(
        maxLines = 1,
        value = inputValue,
        onValueChange = { newValue ->
            inputValue = newValue
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
                        viewModel.setSearchQuery("")
                        viewModel.clearSearch()
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
    viewModel: SearchViewModel
) {
    if (screenTag == MAIN) viewModel.searchVacancies(inputValue)
    if (screenTag == OPTION) viewModel.searchIndustries(inputValue)
    if (screenTag == FILTER) TODO()
}
