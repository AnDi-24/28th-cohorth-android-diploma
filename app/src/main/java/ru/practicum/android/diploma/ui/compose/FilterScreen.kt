package ru.practicum.android.diploma.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.prefs.FilterSettingsModel
import ru.practicum.android.diploma.presentation.FilterViewModel
import ru.practicum.android.diploma.presentation.SearchViewModel
import ru.practicum.android.diploma.ui.compose.components.HideSalaryFilterCheckbox
import ru.practicum.android.diploma.ui.compose.components.IndustrySelectionButton
import ru.practicum.android.diploma.ui.compose.components.NegativeButton
import ru.practicum.android.diploma.ui.compose.components.PositiveButton
import ru.practicum.android.diploma.ui.compose.components.SalaryInputField
import ru.practicum.android.diploma.ui.theme.Spacing16
import ru.practicum.android.diploma.ui.theme.Spacing8

@Composable
fun FilterScreen(
    navController: NavController,
    viewModel: FilterViewModel,
    searchViewModel: SearchViewModel
) {
    val uiState by viewModel.filterState.collectAsState()
    val appliedFilters = viewModel.getAppliedFilters()
    val isFirstComposition = rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (isFirstComposition.value) {
            viewModel.loadFilters()
            viewModel.takeSessionSnapshot()
            isFirstComposition.value = false
        }
    }

    BackHandler {
        viewModel.restoreSessionSnapshot()
        navController.popBackStack()
    }

    FilterScreenContent(
        filterState = uiState,
        appliedFilters = appliedFilters,
        onIndustryClick = {
            navController.navigate(OPTION)
        },
        onIndustryClear = {
            viewModel.updateIndustry("")
            viewModel.updateIndustryName("")
        },
        onSalaryChanged = { salaryText ->
            val salary = salaryText.toIntOrNull() ?: 0
            viewModel.updateSalary(salary)
        },
        onSalaryClear = {
            viewModel.updateSalary(0)
        },
        onApplyFilters = {
            viewModel.applyFilters()
            searchViewModel.searchWithFilters()
            navController.popBackStack()
        },
        onResetFilters = {
            viewModel.resetFilters()
        },
        onShowSalaryChanged = { showSalary ->
            viewModel.updateShowSalary(showSalary)
        }
    )
}

@Composable
fun FilterScreenContent(
    filterState: FilterSettingsModel,
    appliedFilters: FilterSettingsModel,
    onIndustryClick: () -> Unit,
    onIndustryClear: () -> Unit,
    onSalaryChanged: (String) -> Unit,
    onSalaryClear: () -> Unit,
    onShowSalaryChanged: (Boolean) -> Unit,
    onApplyFilters: () -> Unit,
    onResetFilters: () -> Unit
) {
    val hasChanges = filterState != appliedFilters
    val showButtons = hasChanges ||
        filterState.industry.isNotEmpty() ||
        filterState.salary > 0 ||
        filterState.showSalary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(Spacing16),
            verticalArrangement = Arrangement.spacedBy(Spacing8)
        ) {
            IndustrySelectionButton(
                modifier = Modifier.fillMaxWidth(),
                isIndustrySelected = filterState.industry.isNotEmpty(),
                selectedIndustryName = filterState.industryName.takeIf { it.isNotEmpty() },
                onClearClick = onIndustryClear,
                onClick = onIndustryClick
            )

            Spacer(modifier = Modifier.height(Spacing16))

            SalaryInputField(
                modifier = Modifier.fillMaxWidth(),
                salaryText = if (filterState.salary > 0) filterState.salary.toString() else "",
                onSalaryChanged = onSalaryChanged,
                onClearClick = onSalaryClear
            )

            Spacer(modifier = Modifier.height(Spacing16))

            HideSalaryFilterCheckbox(
                modifier = Modifier.fillMaxWidth(),
                isChecked = filterState.showSalary,
                onCheckedChange = onShowSalaryChanged
            )

        }

        if (showButtons) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Spacing16),
                verticalArrangement = Arrangement.spacedBy(Spacing8)
            ) {
                PositiveButton(
                    text = R.string.select,
                    onClick = onApplyFilters,
                    modifier = Modifier.fillMaxWidth()
                )

                NegativeButton(
                    text = R.string.reset,
                    onClick = onResetFilters,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
