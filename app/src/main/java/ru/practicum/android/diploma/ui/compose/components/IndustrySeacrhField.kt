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
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.presentation.FilterOptionViewModel
import ru.practicum.android.diploma.ui.theme.InputFieldHeight
import ru.practicum.android.diploma.ui.theme.Spacing8

@Composable
fun IndustrySearchField(
    label: String,
    viewModel: FilterOptionViewModel
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
            viewModel.setSearchQuery(inputValue)
            if (inputValue.isNotEmpty()) {
                viewModel.searchIndustries(inputValue)
            } else {
                viewModel.searchIndustries("")
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
        trailingIcon = {
            if (inputValue.isNotEmpty()) {
                IconButton(
                    onClick = {
                        inputValue = ""
                        viewModel.setSearchQuery("")
                        viewModel.searchIndustries("")
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
