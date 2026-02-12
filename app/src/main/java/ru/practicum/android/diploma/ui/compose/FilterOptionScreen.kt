package ru.practicum.android.diploma.ui.compose

import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val filterUiState = viewModel.filterUiState.value
    val selectedIndustry by viewModel.selectedIndustry.collectAsState()
    var isButtonVisible by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadSavedIndustry()
    }

    LaunchedEffect(selectedIndustry) {
        isButtonVisible = selectedIndustry != null
        selectedIndustry?.let {
            filterViewModel.updateIndustry(it.id)
            filterViewModel.updateIndustryName(it.name)
        }
    }

    fun resetSelection() {
        viewModel.resetChoice()
        filterViewModel.updateIndustry("")
        filterViewModel.updateIndustryName("")
        isButtonVisible = false
    }

    val selectedIndex = remember(filterUiState, selectedIndustry, isSearching) {
        if (isSearching) -1
        else (filterUiState as? IndustryUiState.OnSelect)
            ?.industries
            ?.indexOfFirst { it.id == selectedIndustry?.id }
            ?: -1
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.padding(16.dp)) {
            IndustrySearchField(
                label = stringResource(R.string.request_placeholder),
                viewModel = viewModel,
                onTextChanged = { isTyping ->
                    isSearching = isTyping
                    if (isTyping) resetSelection()
                }
            )
        }

        when (filterUiState) {
            is IndustryUiState.OnSelect -> {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    itemsIndexed(filterUiState.industries) { index, item ->
                        IndustriesItem(
                            item = item,
                            isSelected = index == selectedIndex && selectedIndex != -1,
                            onSelect = {
                                viewModel.selectedIndustry(item)
                                isSearching = false
                                isButtonVisible = true
                                filterViewModel.updateIndustry(item.id)
                                filterViewModel.updateIndustryName(item.name)
                            }
                        )
                    }
                }

                if (isButtonVisible) {
                    PositiveButton(
                        text = R.string.select,
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }

            is IndustryUiState.Empty -> {
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
            .padding(horizontal = 2.dp),
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
