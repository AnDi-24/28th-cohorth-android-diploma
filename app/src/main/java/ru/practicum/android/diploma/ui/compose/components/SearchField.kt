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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.delay
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.ui.theme.InputFieldHeight
import ru.practicum.android.diploma.ui.theme.Spacing8

private const val SEARCH_DEBOUNCE_DELAY = 2000L

@Composable
fun SearchField(
    label: String,
    viewModel: SearchViewModel
) {
    var query by rememberSaveable { mutableStateOf("") }
    var inputValue by remember { mutableStateOf(query) }

    LaunchedEffect(inputValue) {
        if (inputValue != query) {
            delay(SEARCH_DEBOUNCE_DELAY)
            query = inputValue
            if (inputValue.isNotEmpty()) {
                viewModel.searchVacancies(inputValue)
            } else {
                viewModel.clearSearch()
            }
        }
    }

    TextField(
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
        trailingIcon = {
            if (inputValue.isNotEmpty()) {
                IconButton(
                    onClick = {
                        inputValue = ""
                        query = ""
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
