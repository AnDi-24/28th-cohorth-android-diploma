package ru.practicum.android.diploma.domain.network.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.network.models.VacancyDto

interface FindVacancyInteractor {
    fun getVacancyDetails(expression: String): Flow<VacancyDto>
}
