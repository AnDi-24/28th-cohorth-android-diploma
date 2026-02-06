package ru.practicum.android.diploma.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.room.util.query
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.network.models.industries.IndustryModel
import ru.practicum.android.diploma.presentation.FilterViewModel
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.presentation.models.IndustryUiState
import ru.practicum.android.diploma.ui.compose.components.GetListFailedEmptyState
import ru.practicum.android.diploma.ui.compose.components.SearchField

@Composable
fun FilterOptionScreen(
    viewModel: SearchViewModel,
    filterViewModel: FilterViewModel
) {
    val filterUiState by viewModel.filterUiState
    var selectedIndex by remember { mutableStateOf(-1) }

    val industrySearchQuery by viewModel.industrySearchQuery.collectAsState()

    Column() {
        Box(modifier = Modifier.padding(16.dp)) {
            SearchField(
                label = stringResource(R.string.request_placeholder),
                viewModel = viewModel,
                screenTag = OPTION,
                query = industrySearchQuery,
                onQueryChange = {
                    viewModel.setIndustrySearchQuery(it)
                    viewModel.searchIndustries(it)
                },
                onSearch = {}
            )
        }
        when (filterUiState) {
            is IndustryUiState.OnSelect -> {
                val selectedState = filterUiState as IndustryUiState.OnSelect
                LazyColumn() {
                    itemsIndexed(selectedState.industries) { index, item ->
                        IndustriesItem(
                            item = item,
                            isSelected = index == selectedIndex,
                            onSelect = {
                                selectedIndex = index
                                viewModel.selectedIndustry(item)
                                filterViewModel.updateIndustry(item.id)
                                filterViewModel.updateIndustryName(item.name)
                            }
                        )
                    }
                }
            }

            is IndustryUiState.Selected -> {
                val selectedState = filterUiState as IndustryUiState.Selected
                IndustriesItem(
                    selectedState.industry,
                    true,
                    {}
                )
            }

            else ->
                GetListFailedEmptyState()
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
            .height(56.dp)
            .clickable { onSelect() }
            .padding(start = 2.dp, end = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
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
