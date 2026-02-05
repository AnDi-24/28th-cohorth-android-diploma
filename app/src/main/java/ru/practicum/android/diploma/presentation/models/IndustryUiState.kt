package ru.practicum.android.diploma.presentation.models

import ru.practicum.android.diploma.presentation.SearchViewModel

sealed interface IndustryUiState {

    object Empty: IndustryUiState

    data class Selected(
        val industries: List<SearchViewModel.IndustriesExample>,
        val selectedFlag: Boolean
    ) : IndustryUiState
}
