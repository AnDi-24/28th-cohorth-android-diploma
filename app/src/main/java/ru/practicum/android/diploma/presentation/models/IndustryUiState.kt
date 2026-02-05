package ru.practicum.android.diploma.presentation.models

import ru.practicum.android.diploma.domain.network.models.industries.IndustryModel

sealed interface IndustryUiState {

    object Empty : IndustryUiState

    data class OnSelect(
        val industries: List<IndustryModel>
    ) : IndustryUiState

    data class Selected(
        val industry: IndustryModel
    ) : IndustryUiState


}
