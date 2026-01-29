package ru.practicum.android.diploma.domain.network

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.network.models.VacancyDto
import ru.practicum.android.diploma.domain.network.api.FindVacancyInteractor
import ru.practicum.android.diploma.domain.network.api.FindVacancyRepository

class FindVacancyInteractorImpl(
    val repository: FindVacancyRepository
) : FindVacancyInteractor {

    override fun getVacancyDetails(expression: String): Flow<VacancyDto> {
        return repository.getVacancyDetails(expression)
    }
}
