package ru.practicum.android.diploma.domain.network.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.network.models.VacancyDto
import ru.practicum.android.diploma.util.Resource

interface FindVacancyRepository {
    suspend fun getVacancyDetails(expression: String): Resource<VacancyDto>

    fun getListVacancies(
        area: Int?,
        industry: Int?,
        text: String,
        salary: Int?,
        page: Int,
        onlyWithSalary: Boolean
    ): Flow<Resource<Pair<List<VacancyDto>, Int>>>

}
