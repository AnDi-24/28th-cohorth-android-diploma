package ru.practicum.android.diploma.domain.network.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.network.models.VacancyDto
import ru.practicum.android.diploma.domain.network.models.SearchParams
import ru.practicum.android.diploma.util.Resource

interface FindVacancyRepository {
    suspend fun getVacancyDetails(expression: String): Resource<VacancyDto>

    fun getListVacancies(
        params: SearchParams
    ): Flow<Resource<Pair<List<VacancyDto>, Int>>>

}
