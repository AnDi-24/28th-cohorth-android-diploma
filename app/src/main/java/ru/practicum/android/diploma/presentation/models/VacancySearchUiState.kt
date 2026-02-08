package ru.practicum.android.diploma.presentation.models

import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel

sealed interface VacancySearchUiState {
    object Idle : VacancySearchUiState
    object Loading : VacancySearchUiState
    data class Success(
        val vacancies: List<VacancyDetailsModel>,
        val totalFound: Int,
        val isLoadingMore: Boolean = false,
        val isLastPage: Boolean = false
    ) : VacancySearchUiState

    object Empty : VacancySearchUiState
    object NetworkError : VacancySearchUiState
    object UnknownError : VacancySearchUiState
}
