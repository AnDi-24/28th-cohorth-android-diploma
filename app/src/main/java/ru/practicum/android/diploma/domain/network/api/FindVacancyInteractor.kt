package ru.practicum.android.diploma.domain.network.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.network.models.VacancyDto
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.util.Resource

interface FindVacancyInteractor {
    suspend fun getVacancyDetails(expression: String): Resource<VacancyDetailsModel>

    fun getListVacancies(
        area: Int?,
        industry: Int?,
        text: String,
        salary: Int?,
        page: Int,
        onlyWithSalary: Boolean
    ): Flow<Resource<Pair<List<VacancyDto>, Int>>>
}
