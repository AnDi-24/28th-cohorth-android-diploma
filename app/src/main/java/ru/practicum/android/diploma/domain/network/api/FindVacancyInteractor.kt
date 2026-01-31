package ru.practicum.android.diploma.domain.network.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.network.models.SearchParams
import ru.practicum.android.diploma.domain.network.models.VacancyDetailsModel
import ru.practicum.android.diploma.util.Resource

interface FindVacancyInteractor {
    suspend fun getVacancyDetails(expression: String): Resource<VacancyDetailsModel>
    suspend fun getListVacancies(params: SearchParams): Flow<Resource<Pair<List<VacancyDetailsModel>, Int>>>
}
